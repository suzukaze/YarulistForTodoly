package com.github.suzukaze.yarulistfortodoly.model;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

  private static final String TAG = SharedPreferencesManager.class.getSimpleName();


  private static final String SHARED_PREFERENCES = "sharedPreferences";

  private static final String KEY_CURRENT_TABS_ITEM = "keyCurrentTabItem";
  private static final String KEY_SHOW_COMPLETED_ITEMS = "keyShowCompletedItems";

  private SharedPreferences sharedPreferences;

  public SharedPreferencesManager(Context context) {
    sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
  }

  public void putCurrentTabsItem(int currentTabIndex) {
    putInt(KEY_CURRENT_TABS_ITEM, currentTabIndex);
  }

  public int getCurrentTabsItem() {
    return getInt(KEY_CURRENT_TABS_ITEM, 0);
  }

  public void putShowCompletedItems(boolean showCompletedItems) {
    putBoolean(KEY_SHOW_COMPLETED_ITEMS, showCompletedItems);
  }

  public boolean getShowCompletedItems() {
    return getBoolean(KEY_SHOW_COMPLETED_ITEMS, false);
  }

  public void putInt(String key, int value) {
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putInt(key, value).apply();
  }

  public int getInt(String key, int defaultValue) {
    return sharedPreferences.getInt(key, defaultValue);
  }

  public void putBoolean(String key, boolean value) {
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putBoolean(key, value).apply();
  }

  public boolean getBoolean(String key, boolean defaultValue) {
    return sharedPreferences.getBoolean(key, defaultValue);
  }
}
