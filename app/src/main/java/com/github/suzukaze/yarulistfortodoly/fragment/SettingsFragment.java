package com.github.suzukaze.yarulistfortodoly.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cookpad.android.rxt4a.subscriptions.AndroidCompositeSubscription;
import com.github.suzukaze.yarulistfortodoly.R;
import com.github.suzukaze.yarulistfortodoly.activity.MainActivity;
import com.github.suzukaze.yarulistfortodoly.view.SettingsView;
import com.github.suzukaze.yarulistfortodoly.viewmodel.SettingsViewModel;

public class SettingsFragment extends Fragment {

  private static final String TAG = SettingsFragment.class.getSimpleName();

  private final AndroidCompositeSubscription compositeSubscription = new AndroidCompositeSubscription();

  public SettingsFragment() {
  }

  public static SettingsFragment newInstance() {
    return new SettingsFragment();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    SettingsView settingsView = (SettingsView) inflater.inflate(R.layout.settings, container, false);
    MainActivity mainActivity = (MainActivity) getActivity();
    SettingsViewModel settingsViewModel = new SettingsViewModel(mainActivity.getAccountManager());
    settingsView.init(settingsViewModel, compositeSubscription, mainActivity);
    settingsView.show();
    return settingsView;
  }

  @Override
  public void onStop() {
    compositeSubscription.unsubscribe();

    super.onStop();
  }
}
