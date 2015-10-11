package com.github.suzukaze.yarulistfortodoly.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cookpad.android.rxt4a.operators.OperatorAddToCompositeSubscription;
import com.cookpad.android.rxt4a.schedulers.AndroidSchedulers;
import com.cookpad.android.rxt4a.subscriptions.AndroidCompositeSubscription;
import com.github.suzukaze.yarulistfortodoly.R;
import com.github.suzukaze.yarulistfortodoly.activity.MainActivity;
import com.github.suzukaze.yarulistfortodoly.fragment.ToDoFragment;
import com.github.suzukaze.yarulistfortodoly.model.Account;
import com.github.suzukaze.yarulistfortodoly.model.AccountManager;
import com.github.suzukaze.yarulistfortodoly.model.Project;
import com.github.suzukaze.yarulistfortodoly.model.ProjectMode;
import com.github.suzukaze.yarulistfortodoly.model.api.ServiceGenerator;
import com.github.suzukaze.yarulistfortodoly.model.api.TodolyClient;
import com.github.suzukaze.yarulistfortodoly.viewmodel.ProjectViewModel;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import me.drakeet.materialdialog.MaterialDialog;
import rx.Observable;
import rx.Observer;
import rx.schedulers.Schedulers;

public class ProjectView extends LinearLayout {

  private static final String TAG = ProjectView.class.getSimpleName();

  @Bind(R.id.back)
  LinearLayout back;

  @Bind(R.id.progress_bar)
  SmoothProgressBar smoothProgressBar;

  @Bind(R.id.project_name)
  EditText projectName;

  @Bind(R.id.delete_project)
  LinearLayout deleteProject;

  private ProjectViewModel projectViewModel;
  private ToDoFragment toDoFragment;
  private MainActivity mainActivity;
  private AccountManager accountManager;
  private AndroidCompositeSubscription compositeSubscription;
  private boolean underCommunication;

  public ProjectView(Context context) {
    super(context);
  }

  public ProjectView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public ProjectView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public void init(final ProjectViewModel projectViewModel, final InputMethodManager inputMethodManager,
                   final ToDoFragment toDoFragment, MainActivity mainActivity, Project currentProject,
                   final ProjectMode projectMode, AccountManager accountManager, AndroidCompositeSubscription compositeSubscription) {
    this.projectViewModel = projectViewModel;
    this.toDoFragment = toDoFragment;
    this.mainActivity = mainActivity;
    this.accountManager = accountManager;
    this.compositeSubscription = compositeSubscription;

    ButterKnife.bind(this);

    if (currentProject != null) {
      projectName.setText(currentProject.getContent());
    }

    switch (projectMode) {
      case ADD_PROJECT:
        deleteProject.setVisibility(GONE);
        break;
      case UPDATE_PROJECT:
        deleteProject.setVisibility(VISIBLE);
        break;
    }

    back.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        hide();
      }
    });

    projectName.setOnKeyListener(new OnKeyListener() {
      @Override
      public boolean onKey(View view, int keyCode, KeyEvent event) {
        if (underCommunication) return false;

        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
          // Close software keyboard.
          inputMethodManager.hideSoftInputFromWindow(projectName.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

          switch (projectMode) {
            case ADD_PROJECT: {
              Project project = new Project();
              project.setContent(projectName.getText().toString());
              createProject(project);
            }
            return true;

            case UPDATE_PROJECT: {
              Project project = projectViewModel.getProject();
              project.setContent(projectName.getText().toString());
              projectViewModel.updateProject(project);
              toDoFragment.updateFragment();
              hide();
            }
            return true;
          }
        }
        return false;
      }
    });

    deleteProject.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        Resources r = getContext().getResources();
        final MaterialDialog materialDialog = new MaterialDialog(getContext())
            .setTitle(r.getString(R.string.confirm_deleting))
            .setMessage("");
        materialDialog.setPositiveButton(r.getString(R.string.ok), new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            projectViewModel.deleteProject(projectViewModel.getProject());
            toDoFragment.changeTabs();
            materialDialog.dismiss();
            hide();
          }
        });
        materialDialog.setNegativeButton(r.getString(R.string.cancel), new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            materialDialog.dismiss();
          }
        });

        materialDialog.show();
      }
    });
  }

  public void show() {
    mainActivity.hideToolbar();
    setVisibility(View.VISIBLE);
    Project project = projectViewModel.getProject();
    if (project != null) {
      setProjectName(project.getContent());
    } else {
      setProjectName("");
    }
  }

  public void hide() {
    mainActivity.showToolbar();
    setVisibility(View.INVISIBLE);
  }

  public void setProjectName(String projectName) {
    this.projectName.setText(projectName);
  }

  private void createProject(final Project originalProject) {
    if (!accountManager.isCompleteAccount()) return;

    startProgress();

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
            projectViewModel.addProjectToFirst(project);
          }

          @Override
          public void onCompleted() {
            stopProgress();
            toDoFragment.updateFragment();
            hide();
          }

          @Override
          public void onError(Throwable e) {
            stopProgress();
            projectName.setText("");
            Toast.makeText(getContext(), getResources().getString(R.string.failure_of_creating_project),
                Toast.LENGTH_SHORT).show();
          }
        });
  }

  private void startProgress() {
    underCommunication = true;
    smoothProgressBar.setVisibility(View.VISIBLE);
    smoothProgressBar.progressiveStart();
  }

  private void stopProgress() {
    underCommunication = false;
    smoothProgressBar.progressiveStop();
    smoothProgressBar.setVisibility(View.GONE);
  }
}
