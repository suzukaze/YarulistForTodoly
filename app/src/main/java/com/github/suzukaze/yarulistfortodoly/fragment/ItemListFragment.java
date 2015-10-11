package com.github.suzukaze.yarulistfortodoly.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.cookpad.android.rxt4a.subscriptions.AndroidCompositeSubscription;
import com.github.suzukaze.yarulistfortodoly.R;
import com.github.suzukaze.yarulistfortodoly.activity.MainActivity;
import com.github.suzukaze.yarulistfortodoly.model.AccountManager;
import com.github.suzukaze.yarulistfortodoly.model.DataManager;
import com.github.suzukaze.yarulistfortodoly.model.HistoryManager;
import com.github.suzukaze.yarulistfortodoly.model.Project;
import com.github.suzukaze.yarulistfortodoly.model.SharedPreferencesManager;
import com.github.suzukaze.yarulistfortodoly.view.ItemListView;
import com.github.suzukaze.yarulistfortodoly.viewmodel.ItemListViewModel;

import butterknife.ButterKnife;

public class ItemListFragment extends Fragment {

  private static final String TAG = ItemListFragment.class.getSimpleName();

  public static final String KEY_PROJECT_ID = "keyProjectId";

  private final AndroidCompositeSubscription compositeSubscription = new AndroidCompositeSubscription();

  private SharedPreferencesManager sharedPreferencesManager;
  private AccountManager accountManager;
  private DataManager dataManager;
  private HistoryManager historyManager;
  private Project project;

  public ItemListFragment() {
  }

  public static ItemListFragment newInstance(Project project) {
    ItemListFragment itemListFragment = new ItemListFragment();
    Bundle arguments = new Bundle();
    arguments.putLong(KEY_PROJECT_ID, project.getId());
    itemListFragment.setArguments(arguments);
    return itemListFragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    MainActivity mainActivity = (MainActivity) getActivity();
    sharedPreferencesManager = mainActivity.getSharedPreferencesManager();
    accountManager = mainActivity.getAccountManager();
    dataManager = mainActivity.getDataManager();
    historyManager = mainActivity.getHistoryManager();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    ItemListView itemListView = (ItemListView) inflater.inflate(R.layout.item_list, container, false);
    ButterKnife.bind(this, itemListView);

    MainActivity mainActivity = (MainActivity) getActivity();
    InputMethodManager inputMethodManager = mainActivity.getInputMethodManager();

    Bundle bundle = getArguments();
    if (bundle != null) {
      long projectId = bundle.getLong(KEY_PROJECT_ID);
      project = dataManager.getProject(projectId);

      ItemListViewModel itemListViewModel = new ItemListViewModel(sharedPreferencesManager,
          accountManager, dataManager, historyManager);
      itemListViewModel.setProjectId(projectId);
        itemListView.init(itemListViewModel, mainActivity, inputMethodManager,
            mainActivity.getEditItemView(), compositeSubscription);
      itemListView.show();
    }

    return itemListView;
  }

  @Override
  public void onStop() {
    compositeSubscription.unsubscribe();

    super.onStop();
  }

  public Project getProject() {
    return project;
  }

  public void show() {
    ItemListView itemListView = (ItemListView) getView();
    if (itemListView != null) {
      itemListView.show();
    }
  }
}
