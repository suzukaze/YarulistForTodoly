package com.github.suzukaze.yarulistfortodoly.model;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;

public class CommandHistory {

  @SerializedName("commands")
  private LinkedList<Command> commands;

  public CommandHistory(LinkedList<Command> commands) {
    this.commands = commands;
  }

  public LinkedList<Command> getCommands() {
    return commands;
  }
}
