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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataManager {

  private static final String TAG = DataManager.class.getSimpleName();

  private PersistentDataManager persistentDataManager;
  private HashMap<Long, Project> hashProjects = new HashMap<>();
  private List<Project> projects = new ArrayList<>();
  private List<Item> items = new ArrayList<>();

  public DataManager(PersistentDataManager persistentDataManager) {
    this.persistentDataManager = persistentDataManager;
  }

  public void clear() {
    hashProjects.clear();
    projects.clear();
    items.clear();
  }

  public void addProjectToFirst(Project newProject) {
    hashProjects.put(newProject.getId(), newProject);
    projects.add(0, newProject);
  }

  public void putProject(Project newProject) {
    hashProjects.put(newProject.getId(), newProject);
    for (int i = 0, size = projects.size(); i < size; i++) {
      Project project = projects.get(i);
      if (project.getId() == newProject.getId()) {
        projects.set(i, newProject);
        return;
      }
    }
    projects.add(newProject);
  }

  public Project getProject(long id) {
    return hashProjects.get(id);
  }

  public List<Project> getProjects() {
    return projects;
  }

  public void setItems(List<Item> items) {
    this.items = items;
  }

  public void addItem(Item item) {
    items.add(item);
  }

  public void putItem(Item targetItem) {
    for (int i = 0, size = items.size(); i < size; i++) {
      Item item = items.get(i);
      if (item.getId() == targetItem.getId()) {
        items.set(i, targetItem);
        return;
      }
    }
    items.add(targetItem);
  }

  public void deleteItem(Item targetItem) {
    for (Item item : items) {
      if (item.getId() == targetItem.getId()) {
        items.remove(item);
        break;
      }
    }
  }

  public void deleteProject(Project targetProject) {
    for (Project project : projects) {
      if (project.getId() == targetProject.getId()) {
        projects.remove(project);
        hashProjects.remove(project.getId());
        break;
      }
    }
  }

  public void parse() {
    Set<Map.Entry<Long, Project>> set = hashProjects.entrySet();
    Iterator<Map.Entry<Long, Project>> iterator = set.iterator();
    while (iterator.hasNext()) {
      Map.Entry<Long, Project> entry = iterator.next();
      Project project = entry.getValue();
      project.clearItems();
    }

    for (Item item : items) {
      Project project = hashProjects.get(item.getProjectId());
      if (project != null) {
        project.addItem(item);
      }
    }
  }

  public void save() {
    persistentDataManager.setProjects(projects);
    persistentDataManager.setItems(items);
    persistentDataManager.save();
  }

  public void load() {
    clear();
    persistentDataManager.load();
    List<Project> projects = persistentDataManager.getProjects();
    for (Project project : projects) {
      putProject(project);
    }
    items = persistentDataManager.getItems();
  }

  public Item getItem(long itemId) {
    for (Item item : items) {
      if (item.getId() == itemId) {
        return item;
      }
    }
    return null;
  }
}
