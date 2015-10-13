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
