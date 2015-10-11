package com.github.suzukaze.yarulistfortodoly.model;

public class AccountManager {

  private static final String TAG = Account.class.getSimpleName();

  private PersistentDataManager persistentDataManager;
  private Account account;

  public AccountManager(PersistentDataManager persistentDataManager) {
    this.persistentDataManager = persistentDataManager;
  }

  public void setAccount(Account account) {
    this.account = account;
  }

  public Account getAccount() {
    return account;
  }

  public void save() {
    persistentDataManager.saveAccount(account);
  }

  public void load() {
    account = persistentDataManager.loadAccount();
  }

  public boolean isCompleteAccount() {
    if (account == null) return false;

    if (account.getUsername() == null || account.getUsername().equals("")) {
      return false;
    }

    if (account.getPassword() == null || account.getPassword().equals("")) {
      return false;
    }

    return true;
  }
}
