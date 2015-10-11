package com.github.suzukaze.yarulistfortodoly.viewmodel;

import com.github.suzukaze.yarulistfortodoly.model.Account;
import com.github.suzukaze.yarulistfortodoly.model.AccountManager;
import com.github.suzukaze.yarulistfortodoly.model.DataManager;
import com.github.suzukaze.yarulistfortodoly.model.HistoryManager;
import com.github.suzukaze.yarulistfortodoly.model.Item;
import com.github.suzukaze.yarulistfortodoly.model.Project;
import com.github.suzukaze.yarulistfortodoly.model.SharedPreferencesManager;

import java.util.List;

public class ItemListViewModel {

  private final static String TAG = ItemListViewModel.class.getSimpleName();


  private SharedPreferencesManager sharedPreferencesManager;
  private AccountManager accountManager;
  private DataManager dataManager;
  private HistoryManager historyManager;
  private long projectId;
  private boolean showCompletedItems;

  public ItemListViewModel(SharedPreferencesManager sharedPreferencesManager,
                           AccountManager accountManager, DataManager dataManager,
                           HistoryManager historyManager) {
    this.sharedPreferencesManager = sharedPreferencesManager;
    this.accountManager = accountManager;
    this.dataManager = dataManager;
    this.historyManager = historyManager;
  }

  public Account getAccount() {
    return accountManager.getAccount();
  }

  public AccountManager getAccountManager() {
    return accountManager;
  }

  public DataManager getDataManager() {
    return dataManager;
  }

  public HistoryManager getHistoryManager() {
    return historyManager;
  }

  public List<Item> makeUnCompletedItems() {
    Project project = dataManager.getProject(projectId);
    return project.makeUncompletedItems();
  }

  public List<Item> makeCompletedItems() {
    Project project = dataManager.getProject(projectId);
    return project.makeCompletedItems();
  }

  public void setProjectId(long projectId) {
    this.projectId = projectId;
  }

  public long getProjectId() {
    return projectId;
  }

  public Project getProject() {
    return dataManager.getProject(projectId);
  }

  public void putProject(Project project) {
    dataManager.putProject(project);
  }

  public void setItems(List<Item> items) {
    dataManager.setItems(items);
  }

  public void addItem(Item item) {
    historyManager.recordAddingItem(item);
    dataManager.addItem(item);
  }

  public void updateItem(Item item) {
    historyManager.recordUpdatingItem(item);
  }

  public void parse() {
    dataManager.parse();
  }

  public void save() {
    dataManager.save();
    historyManager.save();
  }

  public void toggleShowCompletedItems() {
    showCompletedItems = !showCompletedItems;
  }

  public boolean isShowCompletedItems() {
    return showCompletedItems;
  }

  public void saveShowCompletedItems() {
    sharedPreferencesManager.putShowCompletedItems(showCompletedItems);
  }

  public void loadShowCompletedItems() {
    showCompletedItems = sharedPreferencesManager.getShowCompletedItems();
  }

  public void setOnSyncFinishListener(HistoryManager.OnSyncFinishListener onSyncFinishListener) {
    historyManager.setOnSyncFinishListener(onSyncFinishListener);
  }
}
