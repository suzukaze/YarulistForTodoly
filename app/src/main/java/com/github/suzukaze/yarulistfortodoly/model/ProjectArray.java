package com.github.suzukaze.yarulistfortodoly.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProjectArray {

  @SerializedName("Projects")
  private List<Project> projects;

  public ProjectArray(List<Project> projects) {
    this.projects = projects;
  }

  public List<Project> getProjects() {
    return projects;
  }
}
