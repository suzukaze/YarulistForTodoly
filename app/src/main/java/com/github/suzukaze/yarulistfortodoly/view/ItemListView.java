package com.github.suzukaze.yarulistfortodoly.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cookpad.android.rxt4a.operators.OperatorAddToCompositeSubscription;
import com.cookpad.android.rxt4a.schedulers.AndroidSchedulers;
import com.cookpad.android.rxt4a.subscriptions.AndroidCompositeSubscription;
import com.github.suzukaze.yarulistfortodoly.R;
import com.github.suzukaze.yarulistfortodoly.activity.MainActivity;
import com.github.suzukaze.yarulistfortodoly.model.Account;
import com.github.suzukaze.yarulistfortodoly.model.AccountManager;
import com.github.suzukaze.yarulistfortodoly.model.DataManager;
import com.github.suzukaze.yarulistfortodoly.model.HistoryManager;
import com.github.suzukaze.yarulistfortodoly.model.Item;
import com.github.suzukaze.yarulistfortodoly.model.Project;
import com.github.suzukaze.yarulistfortodoly.model.api.ServiceGenerator;
import com.github.suzukaze.yarulistfortodoly.model.api.TodolyClient;
import com.github.suzukaze.yarulistfortodoly.viewmodel.EditItemViewModel;
import com.github.suzukaze.yarulistfortodoly.viewmodel.ItemListViewModel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.schedulers.Schedulers;

public class ItemListView extends FrameLayout implements HistoryManager.OnSyncFinishListener {

  private static final String TAG = ItemListView.class.getSimpleName();

  @Bind(R.id.add_task)
  EditText addTask;

  @Bind(R.id.item_list)
  ListView itemList;

  @Bind(R.id.show_completed_items)
  Button showCompletedItemsButton;

  private ItemListViewModel itemListViewModel;
  private MainActivity mainActivity;
  private AndroidCompositeSubscription compositeSubscription;
  private EditItemView editItemView;

  public ItemListView(Context context) {
    super(context);
  }

  public ItemListView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public ItemListView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public void init(final ItemListViewModel itemListViewModel, MainActivity mainActivity, final InputMethodManager inputMethodManager,
                   final EditItemView editItemView,
                   AndroidCompositeSubscription compositeSubscription) {
    this.itemListViewModel = itemListViewModel;
    this.mainActivity = mainActivity;
    this.compositeSubscription = compositeSubscription;
    this.editItemView = editItemView;

    ButterKnife.bind(this);

    addTask.setOnKeyListener(new OnKeyListener() {
      @Override
      public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
          // Close software keyboard.
          inputMethodManager.hideSoftInputFromWindow(addTask.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

          Item item = new Item();
          item.setContent(addTask.getText().toString());
          item.setProjectId(itemListViewModel.getProjectId());
          itemListViewModel.addItem(item);
          itemListViewModel.save();
          itemListViewModel.parse();
          addTask.setText("");
          show();

          return true;
        }
        return false;
      }
    });

    showCompletedItemsButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        itemListViewModel.toggleShowCompletedItems();
        itemListViewModel.saveShowCompletedItems();
        show();
      }
    });
  }

  private void fetchProjects() {
    Account account = itemListViewModel.getAccount();
    TodolyClient.TodolyService TodolyService = ServiceGenerator.createService(
        TodolyClient.TodolyService.class, TodolyClient.ENDPOINT, account.getUsername(), account.getPassword());

    Observable<List<Project>> observable = TodolyService.getProjects();

    observable
        .lift(new OperatorAddToCompositeSubscription<List<Project>>(compositeSubscription))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<List<Project>>() {
          @Override
          public void onNext(List<Project> projects) {
            for (Project project : projects) {
              itemListViewModel.putProject(project);
            }
          }

          @Override
          public void onCompleted() {
            fetchItems();
          }

          @Override
          public void onError(Throwable e) {
            show();
          }
        });
  }

  private void fetchItems() {
    Account account = itemListViewModel.getAccount();
    TodolyClient.TodolyService todolyService = ServiceGenerator.createService(
        TodolyClient.TodolyService.class, TodolyClient.ENDPOINT, account.getUsername(), account.getPassword());

    Observable<List<Item>> observable = todolyService.getItems();

    observable
        .lift(new OperatorAddToCompositeSubscription<List<Item>>(compositeSubscription))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<List<Item>>() {
          @Override
          public void onNext(List<Item> items) {
            itemListViewModel.setItems(items);
            itemListViewModel.parse();
          }

          @Override
          public void onCompleted() {
            show();
          }

          @Override
          public void onError(Throwable e) {
            show();
          }
        });
  }

  public void show() {
    itemListViewModel.loadShowCompletedItems();
    showOrDontCompletedItems();

    Project project = itemListViewModel.getProject();
    if (project == null) return;

    ItemsAdapter itemsAdapter = new ItemsAdapter(getContext(), this, itemListViewModel, editItemView, mainActivity);
    itemsAdapter.addAll(itemListViewModel.makeUnCompletedItems());
    if (itemListViewModel.isShowCompletedItems()) {
      itemsAdapter.addAll(itemListViewModel.makeCompletedItems());
    }
    itemList.setAdapter(itemsAdapter);
  }

  public void onSyncFinish() {
    fetchProjects();
  }

  private void showOrDontCompletedItems() {
    Resources r = getResources();
    if (itemListViewModel.isShowCompletedItems()) {
      showCompletedItemsButton.setText(r.getString(R.string.dont_show_completed_items));
    } else {
      showCompletedItemsButton.setText(r.getString(R.string.show_completed_items));
    }
  }

  static class ItemsAdapter extends ArrayAdapter<Item> {

    private ItemListView itemListView;
    private ItemListViewModel itemListViewModel;
    private EditItemView editItemView;
    private MainActivity mainActivity;

    public ItemsAdapter(Context context, ItemListView itemListView, ItemListViewModel itemListViewModel,
                        EditItemView editItemView, MainActivity mainActivity) {
      super(context, 0);
      this.itemListView = itemListView;
      this.itemListViewModel = itemListViewModel;
      this.editItemView = editItemView;
      this.mainActivity = mainActivity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      ViewHolder holder;
      if (convertView == null) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.item_list_row, null);
        holder = new ViewHolder(convertView);
        convertView.setTag(holder);
      } else {
        holder = (ViewHolder) convertView.getTag();
      }

      Item item = getItem(position);
      holder.checkBox.setTag(item);
      holder.checkBox.setChecked(item.isChecked());
      holder.checkBox.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
          Item item = (Item) view.getTag();
          item.setChecked(!item.isChecked());
          itemListViewModel.updateItem(item);
          itemListView.show();
        }
      });
      holder.content.setText(item.getContent());
      holder.content.setGravity(Gravity.LEFT);
      holder.content.setTag(item);
      holder.content.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
          Item item = (Item) view.getTag();
          AccountManager accountManager = itemListViewModel.getAccountManager();
          DataManager dataManager = itemListViewModel.getDataManager();
          HistoryManager historyManager = itemListViewModel.getHistoryManager();
          editItemView.init(new EditItemViewModel(accountManager, dataManager, historyManager),
              mainActivity.getInputMethodManager(), mainActivity, itemListView, mainActivity.getFragmentManager());
          editItemView.setItem(item);
          editItemView.show();
        }
      });

      return convertView;
    }
  }

  static final class ViewHolder {

    @Bind(R.id.checkBox)
    CheckBox checkBox;

    @Bind(R.id.content)
    TextView content;

    public ViewHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }
}
