package com.github.suzukaze.yarulistfortodoly.model;

import com.google.gson.annotations.SerializedName;

public class Account {

  private static final String TAG  = Account.class.getSimpleName();

  @SerializedName("username")
  private String username;

  @SerializedName("password")
  private String password;

  @SerializedName("authCompletion")
  private boolean authCompletion;

  public Account() {
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getUsername() {
    return username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPassword() {
    return password;
  }

  public void setAuthCompletion(boolean authCompletion) {
    this.authCompletion = authCompletion;
  }

  public boolean isAuthCompletion() {
    return authCompletion;
  }
}
