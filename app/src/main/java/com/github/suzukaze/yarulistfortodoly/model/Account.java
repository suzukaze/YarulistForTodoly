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
