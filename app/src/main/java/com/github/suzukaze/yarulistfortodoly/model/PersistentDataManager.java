package com.github.suzukaze.yarulistfortodoly.model;

import android.content.Context;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PersistentDataManager {
  
  private static final String TAG = PersistentDataManager.class.getSimpleName();

  public static final String ACCOUNT_FILENAME = "account.json";
  public static final String PROJECT_FILENAME = "project.json";
  public static final String ITEM_FILENAME = "item.json";
  public static final String COMMAND_FILENAME = "command.json";

  private FileManager fileManager;
  private List<Project> projects;
  private List<Item> items;
  private LinkedList<Command> commands;

  public PersistentDataManager(Context context) {
    fileManager = new FileManager(context);
  }

  public void setProjects(List<Project> projects) {
    this.projects = projects;
  }

  public List<Project> getProjects() {
    return projects;
  }

  public void setItems(List<Item> items) {
    this.items = items;
  }

  public List<Item> getItems() {
    return items;
  }

  public void save() {
    saveProjects();
    saveItems();
    saveCommands();
  }

  public void load() {
    loadProjects();
    loadItems();
    loadCommands();
  }

  public void saveProjects() {
    ProjectArray projectArray = new ProjectArray(projects);

    Gson gson = new Gson();
    fileManager.saveFile(PROJECT_FILENAME, gson.toJson(projectArray));
  }

  public void loadProjects() {
    Gson gson = new Gson();
    ProjectArray projectArray = gson.fromJson(fileManager.loadFile(PROJECT_FILENAME), ProjectArray.class);
    if (projectArray != null) {
      projects = projectArray.getProjects();
    } else {
      projects = new ArrayList<>();
    }
  }

  public void saveItems() {
    saveItems(items, ITEM_FILENAME);
  }

  public void saveItems(List<Item> items, String filename) {
    ItemArray itemArray = new ItemArray(items);

    Gson gson = new Gson();
    String json = gson.toJson(itemArray);
    fileManager.saveFile(filename, json);
  }

  public void loadItems() {
    items = loadItems(ITEM_FILENAME);
  }

  public List<Item> loadItems(String filename) {
    String json = fileManager.loadFile(filename);

    Gson gson = new Gson();
    ItemArray itemArray = gson.fromJson(json, ItemArray.class);

    if (itemArray != null) {
      return itemArray.getItems();
    } else {
      return new ArrayList<>();
    }
  }

  public void setCommands(LinkedList<Command> commands) {
    this.commands = commands;
  }

  public void saveCommands() {
    CommandHistory commandHistory = new CommandHistory(commands);

    fileManager.saveFile(COMMAND_FILENAME, new Gson().toJson(commandHistory));
  }

  public void loadCommands() {
    Gson gson = new Gson();
    CommandHistory commandHistory = gson.fromJson(fileManager.loadFile(COMMAND_FILENAME), CommandHistory.class);
    if (commandHistory != null) {
      commands = commandHistory.getCommands();
    } else {
      commands = new LinkedList<>();
    }
  }

  public LinkedList<Command> getCommands() {
    return commands;
  }

  public void saveAccount(Account account) {
    Gson gson = new Gson();
    String json = gson.toJson(account);
    fileManager.saveFile(ACCOUNT_FILENAME, json);
  }

  public Account loadAccount() {
    Gson gson = new Gson();
    return gson.fromJson(fileManager.loadFile(ACCOUNT_FILENAME), Account.class);
  }
}
