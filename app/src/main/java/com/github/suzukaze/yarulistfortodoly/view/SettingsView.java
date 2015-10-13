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

package com.github.suzukaze.yarulistfortodoly.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cookpad.android.rxt4a.operators.OperatorAddToCompositeSubscription;
import com.cookpad.android.rxt4a.schedulers.AndroidSchedulers;
import com.cookpad.android.rxt4a.subscriptions.AndroidCompositeSubscription;
import com.github.suzukaze.yarulistfortodoly.R;
import com.github.suzukaze.yarulistfortodoly.activity.MainActivity;
import com.github.suzukaze.yarulistfortodoly.model.Account;
import com.github.suzukaze.yarulistfortodoly.model.api.ServiceGenerator;
import com.github.suzukaze.yarulistfortodoly.model.api.TodolyClient;
import com.github.suzukaze.yarulistfortodoly.viewmodel.SettingsViewModel;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import rx.Observable;
import rx.Observer;
import rx.schedulers.Schedulers;

public class SettingsView extends LinearLayout {

  private static final String TAG = SettingsView.class.getSimpleName();

  @Bind(R.id.back)
  LinearLayout back;

  @Bind(R.id.progress_bar)
  SmoothProgressBar smoothProgressBar;

  @Bind(R.id.login_layout)
  LinearLayout loginLayout;

  @Bind(R.id.login_id)
  EditText loginIdEdit;

  @Bind(R.id.password)
  EditText passwordEdit;

  @Bind(R.id.login)
  Button login;

  @Bind(R.id.logout_layout)
  LinearLayout logoutLayout;

  @Bind(R.id.logout)
  Button logout;

  private MainActivity mainActivity;
  private boolean underCommunication;

  public SettingsView(Context context) {
    super(context);
  }

  public SettingsView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public SettingsView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public void init(final SettingsViewModel settingsViewModel, final AndroidCompositeSubscription compositeSubscription,
                   final MainActivity mainActivity) {
    this.mainActivity = mainActivity;

    ButterKnife.bind(this);

    if (settingsViewModel.isCompleteAccount()) {
      showLogoutLayout();
    } else {
      showLoginLayout();
    }

    login.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        login.setVisibility(GONE);
        InputMethodManager inputMethodManager = mainActivity.getInputMethodManager();
        inputMethodManager.hideSoftInputFromWindow(loginIdEdit.getWindowToken(),
            InputMethodManager.RESULT_UNCHANGED_SHOWN);

        startProgress();

        final Account account = new Account();
        account.setUsername(loginIdEdit.getText().toString());
        account.setPassword(passwordEdit.getText().toString());

        final TodolyClient.TodolyService service = ServiceGenerator.createService(
            TodolyClient.TodolyService.class, TodolyClient.ENDPOINT, account.getUsername(),
            account.getPassword());

        Observable<Boolean> observable = service.isAuthenticated();

        observable
            .lift(new OperatorAddToCompositeSubscription<Boolean>(compositeSubscription))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<Boolean>() {

              @Override
              public void onNext(Boolean auth) {
                stopProgress();
                if (auth) {
                  settingsViewModel.saveAccount(account);
                  mainActivity.changeTodoFragment(true);
                } else {
                  Toast.makeText(getContext(), getContext().getResources().getString(R.string.login_failure_message),
                      Toast.LENGTH_LONG).show();
                  showLoginLayout();
                }
              }

              @Override
              public void onCompleted() {
              }

              @Override
              public void onError(Throwable e) {
                stopProgress();
                Toast.makeText(getContext(), getContext().getResources().getString(R.string.error_message),
                    Toast.LENGTH_LONG).show();
                showLoginLayout();
              }
            });
      }
    });

    logout.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        Account account = new Account();
        account.setUsername("");
        account.setPassword("");
        settingsViewModel.saveAccount(account);
        showLoginLayout();
      }
    });
  }

  public void show() {
    mainActivity.hideToolbar();
  }

  private void showLoginLayout() {
    back.setVisibility(GONE);
    loginIdEdit.setText("");
    passwordEdit.setText("");
    loginLayout.setVisibility(View.VISIBLE);
    logoutLayout.setVisibility(View.GONE);
    login.setVisibility(VISIBLE);
  }

  private void showLogoutLayout() {
    back.setVisibility(VISIBLE);
    back.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        mainActivity.changeTodoFragment(false);
      }
    });
    loginLayout.setVisibility(View.GONE);
    logoutLayout.setVisibility(View.VISIBLE);
  }

  private void startProgress() {
    underCommunication = true;
    smoothProgressBar.setVisibility(View.VISIBLE);
    smoothProgressBar.progressiveStart();
  }

  private void stopProgress() {
    underCommunication = false;
    smoothProgressBar.progressiveStop();
    smoothProgressBar.setVisibility(View.GONE);
  }
}
