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
