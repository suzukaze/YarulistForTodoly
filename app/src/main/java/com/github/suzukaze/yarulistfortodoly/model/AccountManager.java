/*
 * Copyright (C) 2015 Jun Hiroe
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
