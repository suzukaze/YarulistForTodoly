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

public class Command {

  private static String TAG = Command.class.getCanonicalName();

  public static final int COMMAND_ADD_PROJECT = 0;
  public static final int COMMAND_UPDATE_PROJECT = 1;
  public static final int COMMAND_DELETE_PROJECT = 2;
  public static final int COMMAND_ADD_ITEM = 3;
  public static final int COMMAND_UPDATE_ITEM = 4;
  public static final int COMMAND_DELETE_ITEM = 5;

  @SerializedName("command")
  private int command;

  @SerializedName("json")
  private String json;

  public Command(int command, String json) {
    this.command = command;
    this.json = json;
  }

  public int getCommand() {
    return command;
  }

  public String getJson() {
    return json;
  }

}
