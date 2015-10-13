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
