package com.github.suzukaze.yarulistfortodoly.model;

import com.cookpad.android.rxt4a.operators.OperatorAddToCompositeSubscription;
import com.cookpad.android.rxt4a.schedulers.AndroidSchedulers;
import com.cookpad.android.rxt4a.subscriptions.AndroidCompositeSubscription;
import com.github.suzukaze.yarulistfortodoly.model.api.ServiceGenerator;
import com.github.suzukaze.yarulistfortodoly.model.api.TodolyClient;
import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import rx.Observable;
import rx.Observer;
import rx.schedulers.Schedulers;

public class HistoryManager {

  private static final String TAG = HistoryManager.class.getSimpleName();

  private AccountManager accountManager;
  private DataManager dataManager;
  private PersistentDataManager persistentDataManager;
  private LinkedList<Command> commands;
  private Gson gson;
  private OnSyncFinishListener onSyncFinishListener;
  private AndroidCompositeSubscription compositeSubscription;

  public HistoryManager(AccountManager accountManager, DataManager dataManager,
                        PersistentDataManager persistentDataManager,
                        AndroidCompositeSubscription compositeSubscription) {
    this.dataManager = dataManager;
    this.accountManager = accountManager;
    this.persistentDataManager = persistentDataManager;
    this.compositeSubscription = compositeSubscription;
    commands = new LinkedList<>();
    gson = new Gson();
  }

  public void addCommand(Command command) {
    commands.add(command);
  }

  public void load() {
    persistentDataManager.loadCommands();
    commands = persistentDataManager.getCommands();
  }

  public void save() {
    persistentDataManager.setCommands(commands);
    persistentDataManager.saveCommands();
  }

  public void sync() {
    save();

    executeCommandIfCommandExist();
  }

  public void recordAddingItem(Item item) {
    addCommand(new Command(Command.COMMAND_ADD_ITEM, gson.toJson(item)));
  }

  public void recordUpdatingItem(Item item) {
    addCommand(new Command(Command.COMMAND_UPDATE_ITEM, gson.toJson(item)));
  }

  public void recordDeletingItem(Item item) {
    addCommand(new Command(Command.COMMAND_DELETE_ITEM, gson.toJson(item)));
  }

  public void recordAddingProject(Project project) {
    addCommand(new Command(Command.COMMAND_ADD_PROJECT, gson.toJson(project)));
  }

  public void recordUpdatingProject(Project project) {
    addCommand(new Command(Command.COMMAND_UPDATE_PROJECT, gson.toJson(project)));
  }

  public void recordDeleteProject(Project project) {
    addCommand(new Command(Command.COMMAND_DELETE_PROJECT, gson.toJson(project)));
  }

  public void setOnSyncFinishListener(OnSyncFinishListener onSyncFinishListener) {
    this.onSyncFinishListener = onSyncFinishListener;
  }

  public interface OnSyncFinishListener {
    void onSyncFinish();
  }

  public void executeCommandsAtLocal() {
    for (Command command : commands) {
      executeCommandAtLocal(command);
    }

    dataManager.parse();
  }

  private void executeCommandIfCommandExist() {
    onSyncFinishListener.onSyncFinish();

    try {
      Command command = commands.getFirst();
      executeCommand(command);
    } catch (NoSuchElementException e) {
      if (onSyncFinishListener != null) {
        onSyncFinishListener.onSyncFinish();
      }
    }
  }

  private void executeCommandAtLocal(Command command) {
    switch (command.getCommand()) {
      case Command.COMMAND_ADD_PROJECT:
        executeCommandAddProjectAtLocal(command.getJson());
        break;
      case Command.COMMAND_UPDATE_PROJECT:
        executeCommandUpdateProjectAtLocal(command.getJson());
        break;
      case Command.COMMAND_DELETE_PROJECT:
        executeCommandDeleteProjectAtLocal(command.getJson());
        break;
      case Command.COMMAND_ADD_ITEM:
        executeCommandAddItemAtLocal(command.getJson());
        break;
      case Command.COMMAND_UPDATE_ITEM:
        executeCommandUpdateItemAtLocal(command.getJson());
        break;
      case Command.COMMAND_DELETE_ITEM:
        executeCommandDeleteItemAtLocal(command.getJson());
        break;
      default:
        throw new IllegalStateException("Not Found Command : " + command.getCommand());
    }
  }

  private void executeCommandAddProjectAtLocal(String json) {
    dataManager.addProjectToFirst(gson.fromJson(json, Project.class));
  }

  private void executeCommandUpdateProjectAtLocal(String json) {
    dataManager.putProject(gson.fromJson(json, Project.class));
  }

  private void executeCommandDeleteProjectAtLocal(String json) {
    dataManager.deleteProject(gson.fromJson(json, Project.class));
  }

  private void executeCommandAddItemAtLocal(String json) {
    dataManager.addItem(gson.fromJson(json, Item.class));
  }

  private void executeCommandUpdateItemAtLocal(String json) {
    dataManager.putItem(gson.fromJson(json, Item.class));
  }

  private void executeCommandDeleteItemAtLocal(String json) {
    dataManager.deleteItem(gson.fromJson(json, Item.class));
  }

  private void executeCommand(Command command) {
    switch (command.getCommand()) {
      case Command.COMMAND_ADD_PROJECT:
        executeCommandAddProject(command.getJson());
        break;
      case Command.COMMAND_UPDATE_PROJECT:
        executeCommandUpdateProject(command.getJson());
        break;
      case Command.COMMAND_DELETE_PROJECT:
        executeCommandDeleteProject(command.getJson());
        break;
      case Command.COMMAND_ADD_ITEM:
        executeCommandAddItem(command.getJson());
        break;
      case Command.COMMAND_UPDATE_ITEM:
        executeCommandUpdateItem(command.getJson());
        break;
      case Command.COMMAND_DELETE_ITEM:
        executeCommandDeleteItem(command.getJson());
        break;
      default:
        throw new IllegalStateException("Not Found Command : " + command.getCommand());
    }
  }

  private void nextCommand() {
    commands.pop();
    executeCommandIfCommandExist();
  }

  private void executeCommandAddProject(String json) {
    createProject(gson.fromJson(json, Project.class));
  }

  private void executeCommandUpdateProject(String json) {
    updateProject(gson.fromJson(json, Project.class));
  }

  private void executeCommandDeleteProject(String json) {
    deleteProject(gson.fromJson(json, Project.class));
  }

  private void executeCommandAddItem(String json) {
    createItem(gson.fromJson(json, Item.class));
  }

  private void executeCommandUpdateItem(String json) {
    updateItem(gson.fromJson(json, Item.class));
  }

  private void executeCommandDeleteItem(String json) {
    deleteItem(gson.fromJson(json, Item.class));
  }

  private void createProject(final Project originalProject) {
    if (!accountManager.isCompleteAccount()) return;

    Account account = accountManager.getAccount();

    final TodolyClient.TodolyService service = ServiceGenerator.createService(
        TodolyClient.TodolyService.class, TodolyClient.ENDPOINT, account.getUsername(), account.getPassword());

    Observable<Project> observable = service.createProject(originalProject);

    observable
        .lift(new OperatorAddToCompositeSubscription<Project>(compositeSubscription))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Project>() {

          @Override
          public void onNext(Project project) {
          }

          @Override
          public void onCompleted() {
            nextCommand();
          }

          @Override
          public void onError(Throwable e) {
            if (onSyncFinishListener != null) {
              onSyncFinishListener.onSyncFinish();
            }
          }
        });
  }

  private void updateProject(final Project originalProject) {
    if (!accountManager.isCompleteAccount()) return;

    Account account = accountManager.getAccount();

    final TodolyClient.TodolyService service = ServiceGenerator.createService(
        TodolyClient.TodolyService.class, TodolyClient.ENDPOINT, account.getUsername(), account.getPassword());

    Observable<Project> observable = service.updateProject(originalProject.getId(), originalProject);

    observable
        .lift(new OperatorAddToCompositeSubscription<Project>(compositeSubscription))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Project>() {

          @Override
          public void onNext(Project project) {
          }

          @Override
          public void onCompleted() {
            nextCommand();
          }

          @Override
          public void onError(Throwable e) {
            if (onSyncFinishListener != null) {
              onSyncFinishListener.onSyncFinish();
            }
          }
        });
  }

  private void deleteProject(final Project originalProject) {
    if (!accountManager.isCompleteAccount()) return;

    Account account = accountManager.getAccount();

    final TodolyClient.TodolyService service = ServiceGenerator.createService(
        TodolyClient.TodolyService.class, TodolyClient.ENDPOINT, account.getUsername(), account.getPassword());

    Observable<Project> observable = service.deleteProject(originalProject.getId());

    observable
        .lift(new OperatorAddToCompositeSubscription<Project>(compositeSubscription))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Project>() {

          @Override
          public void onNext(Project project) {
          }

          @Override
          public void onCompleted() {
            nextCommand();
          }

          @Override
          public void onError(Throwable e) {
            if (onSyncFinishListener != null) {
              onSyncFinishListener.onSyncFinish();
            }
          }
        });
  }

  private void createItem(final Item originalItem) {
    if (!accountManager.isCompleteAccount()) return;

    Account account = accountManager.getAccount();

    final TodolyClient.TodolyService service = ServiceGenerator.createService(
        TodolyClient.TodolyService.class, TodolyClient.ENDPOINT, account.getUsername(), account.getPassword());

    Observable<Item> observable = service.createItem(originalItem);

    observable
        .lift(new OperatorAddToCompositeSubscription<Item>(compositeSubscription))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Item>() {

          @Override
          public void onNext(Item item) {
            item.setProjectId(originalItem.getProjectId());
            updateItem(item);
          }

          @Override
          public void onCompleted() {
            nextCommand();
          }

          @Override
          public void onError(Throwable e) {
            if (onSyncFinishListener != null) {
              onSyncFinishListener.onSyncFinish();
            }
          }

        });
  }

  private void updateItem(Item item) {
    if (!accountManager.isCompleteAccount()) return;

    Account account = accountManager.getAccount();

    final TodolyClient.TodolyService service = ServiceGenerator.createService(
        TodolyClient.TodolyService.class, TodolyClient.ENDPOINT, account.getUsername(), account.getPassword());
    Observable<Item> observable = service.updateItem(item.getId(), item);
    observable
        .lift(new OperatorAddToCompositeSubscription<Item>(compositeSubscription))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Item>() {
          @Override
          public void onNext(Item item) {
          }

          @Override
          public void onCompleted() {
            nextCommand();
          }

          @Override
          public void onError(Throwable e) {
            if (onSyncFinishListener != null) {
              onSyncFinishListener.onSyncFinish();
            }
          }
        });
  }

  private void deleteItem(Item item) {
    if (!accountManager.isCompleteAccount()) return;

    Account account = accountManager.getAccount();

    final TodolyClient.TodolyService service = ServiceGenerator.createService(
        TodolyClient.TodolyService.class, TodolyClient.ENDPOINT, account.getUsername(), account.getPassword());
    Observable<Item> observable = service.deleteItem(item.getId());
    observable
        .lift(new OperatorAddToCompositeSubscription<Item>(compositeSubscription))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Item>() {
          @Override
          public void onNext(Item item) {
          }

          @Override
          public void onCompleted() {
            nextCommand();
          }

          @Override
          public void onError(Throwable e) {
            if (onSyncFinishListener != null) {
              onSyncFinishListener.onSyncFinish();
            }
          }
        });
  }
}
