package com.github.suzukaze.yarulistfortodoly.viewmodel;

import com.github.suzukaze.yarulistfortodoly.model.Account;
import com.github.suzukaze.yarulistfortodoly.model.AccountManager;
import com.github.suzukaze.yarulistfortodoly.model.Config;

public class SettingsViewModel {

  private static final String TAG = SettingsViewModel.class.getSimpleName();

  private AccountManager accountManager;

  public SettingsViewModel(AccountManager accountManager) {
    this.accountManager = accountManager;
  }

  public void saveAccount(Account account) {
    accountManager.setAccount(account);
    if (Config.SAVE_PASSWORD) {
      accountManager.save();
    }
  }

  public void loadAccount() {
    accountManager.load();
  }

  public boolean isCompleteAccount() {
    return accountManager.isCompleteAccount();
  }
}
