package com.github.suzukaze.yarulistfortodoly.viewmodel;

import com.github.suzukaze.yarulistfortodoly.model.DataManager;
import com.github.suzukaze.yarulistfortodoly.model.HistoryManager;
import com.github.suzukaze.yarulistfortodoly.model.Project;

public class ProjectViewModel {

  private static final String TAG = ProjectViewModel.class.getSimpleName();

  private DataManager dataManager;
  private HistoryManager historyManager;
  private Project project;

  public ProjectViewModel(DataManager dataManager, HistoryManager historyManager) {
    this.dataManager = dataManager;
    this.historyManager = historyManager;
  }

  public void addProjectToFirst(Project project) {
    dataManager.addProjectToFirst(project);
  }

  public void save() {
    dataManager.save();
    historyManager.save();
  }

  public void setProjectId(long projectId) {
    if (projectId == Project.PROJECT_ID_NONE) {
      this.project = null;
    } else {
      this.project = dataManager.getProject(projectId);
    }
  }

  public Project getProject() {
    return project;
  }

  public void updateProject(Project project) {
    historyManager.recordUpdatingProject(project);
  }

  public void deleteProject(Project project) {
    historyManager.recordDeleteProject(project);
    dataManager.deleteProject(project);
  }
}
