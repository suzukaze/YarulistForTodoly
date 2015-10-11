package com.github.suzukaze.yarulistfortodoly.viewmodel;

import com.github.suzukaze.yarulistfortodoly.model.Account;
import com.github.suzukaze.yarulistfortodoly.model.AccountManager;
import com.github.suzukaze.yarulistfortodoly.model.DataManager;
import com.github.suzukaze.yarulistfortodoly.model.HistoryManager;
import com.github.suzukaze.yarulistfortodoly.model.Item;
import com.github.suzukaze.yarulistfortodoly.model.Project;

import java.util.List;

public class TodoViewModel {

  private static final String TAG = TodoViewModel.class.getSimpleName();

  private AccountManager accountManager;
  private DataManager dataManager;
  private HistoryManager historyManager;

  public TodoViewModel(AccountManager accountManager, DataManager dataManager, HistoryManager historyManager) {
    this.accountManager = accountManager;
    this.dataManager = dataManager;
    this.historyManager = historyManager;
  }

  public void putProject(Project project) {
    dataManager.putProject(project);
  }

  public Project getProject(long id) {
    return dataManager.getProject(id);
  }

  public Account getAccount() {
    return accountManager.getAccount();
  }

  public void setItems(List<Item> items) {
    dataManager.setItems(items);
  }

  public void clear() {
    dataManager.clear();
  }

  public void parse() {
    dataManager.parse();
  }

  public List<Project> getProjects() {
    return dataManager.getProjects();
  }

  public void save() {
    dataManager.save();
    historyManager.save();
  }

  public void load() {
    dataManager.load();
    historyManager.load();
  }

  public void sync() {
    historyManager.sync();
  }

  public void setOnSyncFinishListener(HistoryManager.OnSyncFinishListener onSyncFinishListener) {
    historyManager.setOnSyncFinishListener(onSyncFinishListener);
  }
}
