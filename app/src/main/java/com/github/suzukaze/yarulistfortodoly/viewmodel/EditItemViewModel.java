package com.github.suzukaze.yarulistfortodoly.viewmodel;


import com.github.suzukaze.yarulistfortodoly.model.AccountManager;
import com.github.suzukaze.yarulistfortodoly.model.DataManager;
import com.github.suzukaze.yarulistfortodoly.model.HistoryManager;
import com.github.suzukaze.yarulistfortodoly.model.Item;

public class EditItemViewModel {
  private static final String TAG = EditItemViewModel.class.getSimpleName();

  private AccountManager accountManager;
  private DataManager dataManager;
  private HistoryManager historyManager;

  public EditItemViewModel(AccountManager accountManager, DataManager dataManager,
                           HistoryManager historyManager) {
    this.accountManager = accountManager;
    this.dataManager = dataManager;
    this.historyManager = historyManager;
  }

  public void updateItem(Item item) {
    historyManager.recordUpdatingItem(item);
  }

  public void deleteItem(Item item) {
    historyManager.recordDeletingItem(item);
    dataManager.deleteItem(item);
  }

  public void parse() {
    dataManager.parse();
  }

  public void save() {
    dataManager.save();
    historyManager.save();
  }
}
