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
