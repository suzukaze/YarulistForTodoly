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

package com.github.suzukaze.yarulistfortodoly.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.cookpad.android.rxt4a.subscriptions.AndroidCompositeSubscription;
import com.github.suzukaze.yarulistfortodoly.R;
import com.github.suzukaze.yarulistfortodoly.fragment.SettingsFragment;
import com.github.suzukaze.yarulistfortodoly.fragment.ToDoFragment;
import com.github.suzukaze.yarulistfortodoly.model.AccountManager;
import com.github.suzukaze.yarulistfortodoly.model.DataManager;
import com.github.suzukaze.yarulistfortodoly.model.HistoryManager;
import com.github.suzukaze.yarulistfortodoly.model.PersistentDataManager;
import com.github.suzukaze.yarulistfortodoly.model.SharedPreferencesManager;
import com.github.suzukaze.yarulistfortodoly.view.EditItemView;
import com.github.suzukaze.yarulistfortodoly.view.ProjectView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

  static final String TAG = MainActivity.class.getSimpleName();

  @Bind(R.id.toolbar)
  Toolbar toolbar;

  @Bind(R.id.settings)
  LinearLayout settingsLayout;

  @Bind(R.id.add_project)
  LinearLayout addProjectLayout;

  @Bind(R.id.edit_project)
  LinearLayout editProjectLayout;

  @Bind(R.id.sync)
  LinearLayout syncLayout;

  private final AndroidCompositeSubscription compositeSubscription = new AndroidCompositeSubscription();

  private SharedPreferencesManager sharedPreferencesManager;
  private AccountManager accountManager;
  private DataManager dataManager;
  private HistoryManager historyManager;
  private boolean init;

  private ToDoFragment toDoFragment;
  private SettingsFragment settingsFragment;
  private EditItemView editItemView;
  private ProjectView projectView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ButterKnife.bind(this);

    sharedPreferencesManager = new SharedPreferencesManager(getApplicationContext());

    PersistentDataManager persistentDataManager = new PersistentDataManager(getApplicationContext());
    accountManager = new AccountManager(persistentDataManager);

    accountManager.load();

    dataManager = new DataManager(persistentDataManager);

    historyManager = new HistoryManager(accountManager, dataManager, persistentDataManager, compositeSubscription);

    ButterKnife.bind(this);

    setSupportActionBar(toolbar);
  }

  @Override
  public void onStart() {
    super.onStart();
  }

  @Override
  public void onPause() {
    super.onPause();
  }

  @Override
  public void onStop() {
    compositeSubscription.unsubscribe();

    super.onStop();
  }

  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);

    if (init) return;

    init = true;

    dataManager.load();

    historyManager.load();

    settingsLayout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        changeSettingFragment();
      }
    });

    if (accountManager.isCompleteAccount()) {
      changeTodoFragment(false);
    } else {
      changeSettingFragment();
    }
  }

  public SharedPreferencesManager getSharedPreferencesManager() {
    return sharedPreferencesManager;
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

  public InputMethodManager getInputMethodManager() {
    return (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
  }

  public void setEditItemView(EditItemView editItemView) {
    this.editItemView = editItemView;
  }

  public EditItemView getEditItemView() {
    return editItemView;
  }

  public void setProjectView(ProjectView projectView) {
    this.projectView = projectView;
  }

  public ProjectView getProjectView() {
    return projectView;
  }

  public Toolbar getToolbar() {
    return toolbar;
  }

  public LinearLayout getAddProjectLayout() {
    return addProjectLayout;
  }

  public LinearLayout getEditProjectLayout() {
    return editProjectLayout;
  }

  public LinearLayout getSyncLayout() {
    return syncLayout;
  }

  public void showToolbar() {
    toolbar.setVisibility(View.VISIBLE);
  }

  public void hideToolbar() {
    toolbar.setVisibility(View.GONE);
  }

  public void changeTodoFragment(boolean sync) {
    if (toDoFragment == null) {
      toDoFragment = ToDoFragment.newInstance();
    }
    // See sync only after ToDoFragment#onCreatedView() then doesn't see it.
    // ToDoFragment#setArguements only passes when ToDoFragment.newInstance() called.
    toDoFragment.setSync(sync);
    replaceMainFragment(toDoFragment);
  }

  public void changeSettingFragment() {
    if (settingsFragment == null) {
      settingsFragment = SettingsFragment.newInstance();
    }
    replaceMainFragment(settingsFragment);
  }

  private void replaceMainFragment(Fragment fragment) {
    FragmentManager manager = getSupportFragmentManager();
    manager.beginTransaction()
        .replace(R.id.main_container, fragment)
        .commit();
  }
}
