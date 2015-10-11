package com.github.suzukaze.yarulistfortodoly;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

public class YarulistApplication extends Application {
  
  @Override public void onCreate() {
    super.onCreate();

    JodaTimeAndroid.init(getApplicationContext());
  }
}
