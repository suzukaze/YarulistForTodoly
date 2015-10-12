package com.github.suzukaze.yarulistfortodoly.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
import com.github.suzukaze.yarulistfortodoly.model.ProjectMode;
import com.github.suzukaze.yarulistfortodoly.model.SharedPreferencesManager;
import com.github.suzukaze.yarulistfortodoly.model.api.ServiceGenerator;
import com.github.suzukaze.yarulistfortodoly.model.api.TodolyClient;
import com.github.suzukaze.yarulistfortodoly.view.EditItemView;
import com.github.suzukaze.yarulistfortodoly.view.ProjectView;
import com.github.suzukaze.yarulistfortodoly.view.ProjectsPageAdapter;
import com.github.suzukaze.yarulistfortodoly.viewmodel.ProjectViewModel;
import com.github.suzukaze.yarulistfortodoly.viewmodel.TodoViewModel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import rx.Observable;
import rx.Observer;
import rx.schedulers.Schedulers;

public class ToDoFragment extends Fragment implements HistoryManager.OnSyncFinishListener {

  private static final String TAG = ToDoFragment.class.getSimpleName();

  private static final int TABS_TEXT_SIZE = 20; // sp
  @Bind(R.id.tabs)
  PagerTabStrip tabs;

  @Bind(R.id.progress_bar)
  SmoothProgressBar smoothProgressBar;

  @Bind(R.id.tags_viewpager)
  ViewPager tagsViewPager;

  @Bind(R.id.edit_item)
  EditItemView editItemView;

  @Bind(R.id.project)
  ProjectView projectView;

  private final AndroidCompositeSubscription compositeSubscription = new AndroidCompositeSubscription();

  private ProjectsPageAdapter adapter;
  private SharedPreferencesManager sharedPreferencesManager;
  private AccountManager accountManager;
  private DataManager dataManager;
  private HistoryManager historyManager;
  private TodoViewModel todoViewModel;
  private boolean underCommunication;
  private Project currentProject;
  private boolean sync;
  private LinearLayout syncLayout;

  public ToDoFragment() {
  }

  public static ToDoFragment newInstance() {
    return new ToDoFragment();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    final MainActivity mainActivity = (MainActivity) getActivity();
    sharedPreferencesManager = mainActivity.getSharedPreferencesManager();
    accountManager = mainActivity.getAccountManager();
    dataManager = mainActivity.getDataManager();
    historyManager = mainActivity.getHistoryManager();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.todo, container, false);
    ButterKnife.bind(this, view);

    final MainActivity mainActivity = (MainActivity) getActivity();
    todoViewModel = new TodoViewModel(accountManager, dataManager, historyManager);
    todoViewModel.setOnSyncFinishListener(this);
    Toolbar toolbar = mainActivity.getToolbar();
    toolbar.setTitle(getResources().getString(R.string.yarulist));
    mainActivity.showToolbar();

    if (dataManager.getProjects().size() > 0) {
      currentProject = dataManager.getProjects().get(0);
    }

    syncLayout = mainActivity.getSyncLayout();
    final LinearLayout finalSyncLayout = syncLayout;
    finalSyncLayout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (underCommunication) return;
        finalSyncLayout.setVisibility(View.INVISIBLE);
        todoViewModel.sync();
      }
    });

    final ToDoFragment toDoFragment = this;

    LinearLayout addProject = mainActivity.getAddProjectLayout();
    addProject.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        ProjectViewModel projectViewModel = new ProjectViewModel(dataManager, historyManager);
        projectViewModel.setProjectId(Project.PROJECT_ID_NONE);
        projectView.init(projectViewModel, mainActivity.getInputMethodManager(), toDoFragment,
            mainActivity, currentProject, ProjectMode.ADD_PROJECT, accountManager, compositeSubscription);
        projectView.show();
      }
    });

    LinearLayout editProjectLayout = mainActivity.getEditProjectLayout();
    editProjectLayout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        ProjectViewModel projectViewModel = new ProjectViewModel(dataManager, historyManager);
        projectViewModel.setProjectId(currentProject.getId());
        projectView.init(projectViewModel, mainActivity.getInputMethodManager(), toDoFragment,
            mainActivity, currentProject, ProjectMode.UPDATE_PROJECT, accountManager, compositeSubscription);
        projectView.show();
      }
    });

    mainActivity.setEditItemView(editItemView);
    mainActivity.setProjectView(null);

    Resources r = getResources();
    tabs.setTextColor(r.getColor(R.color.white));
    tabs.setTextSize(TypedValue.COMPLEX_UNIT_DIP, TABS_TEXT_SIZE);
    tabs.setTabIndicatorColor(r.getColor(R.color.white));

    dataManager = mainActivity.getDataManager();

    dataManager.clear();
    dataManager.load();
    if (sync) {
      todoViewModel.sync();
    } else {
      changeTabs();
    }

    return view;
  }

  @Override
  public void onStop() {
    compositeSubscription.unsubscribe();

    sharedPreferencesManager.putCurrentTabsItem(tagsViewPager.getCurrentItem());

    todoViewModel.save();

    super.onStop();
  }

  @Override
  public void onResume() {
    super.onResume();

    tagsViewPager.setCurrentItem(sharedPreferencesManager.getCurrentTabsItem());
  }

  public void setSync(boolean sync) {
    this.sync = sync;
  }

  public void changeTabs() {
    adapter = new ProjectsPageAdapter(getChildFragmentManager());
    adapter.addAll(dataManager.getProjects());
    tagsViewPager.setAdapter(adapter);
    tagsViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
      }

      @Override
      public void onPageSelected(int position) {
        ItemListFragment itemListFragment = (ItemListFragment) adapter.findFragmentByPosition(tagsViewPager, position);
        currentProject = dataManager.getProjects().get(position);
        itemListFragment.show();
      }

      @Override
      public void onPageScrollStateChanged(int state) {
      }
    });
  }

  public void updateFragment() {
    adapter.destroyAllItem(tagsViewPager);
    adapter.notifyDataSetChanged();
    changeTabs();
  }

  private void fetchProjects() {
    startProgress();

    Account account = todoViewModel.getAccount();
    TodolyClient.TodolyService TodolyService = ServiceGenerator.createService(
        TodolyClient.TodolyService.class, TodolyClient.ENDPOINT, account.getUsername(), account.getPassword());

    dataManager.clear();

    Observable<List<Project>> observable = TodolyService.getProjects();

    observable
        .lift(new OperatorAddToCompositeSubscription<List<Project>>(compositeSubscription))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<List<Project>>() {
          @Override
          public void onNext(List<Project> projects) {
            for (Project project : projects) {
              todoViewModel.putProject(project);
            }
          }

          @Override
          public void onCompleted() {
            List<Project> projects = dataManager.getProjects();
            for (Project project : projects) {
              if (project.getId() == Project.PROJECT_ID_NONE) {
                projects.remove(project);
              }
            }
            fetchItems();
          }

          @Override
          public void onError(Throwable e) {
            dataManager.load();

            historyManager.executeCommandsAtLocal();

            stopProgress();
          }
        });
  }

  private void fetchItems() {
    Account account = todoViewModel.getAccount();
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
            todoViewModel.setItems(items);
            todoViewModel.parse();

            todoViewModel.save();
          }

          @Override
          public void onCompleted() {
            historyManager.executeCommandsAtLocal();

            changeTabs();

            stopProgress();
          }

          @Override
          public void onError(Throwable e) {
            dataManager.load();

            historyManager.executeCommandsAtLocal();

            stopProgress();
          }
        });
  }

  public void onSyncFinish() {
    fetchProjects();
  }

  private void startProgress() {
    underCommunication = true;
    smoothProgressBar.setVisibility(View.VISIBLE);
    smoothProgressBar.progressiveStart();
  }

  private void stopProgress() {
    smoothProgressBar.progressiveStop();
    smoothProgressBar.setVisibility(View.GONE);
    underCommunication = false;
    syncLayout.setVisibility(View.VISIBLE);
  }
}
