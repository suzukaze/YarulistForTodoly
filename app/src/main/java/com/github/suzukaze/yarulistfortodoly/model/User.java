package com.github.suzukaze.yarulistfortodoly.model;

import com.google.gson.annotations.SerializedName;

public class User {

  @SerializedName("Id")
  private long id;

  @SerializedName("Email")
  private String email;

  @SerializedName("Password")
  private String password;

  @SerializedName("FullName")
  private String fullName;

  @SerializedName("TimeZone")
  private double timeZone;

  @SerializedName("IsProUser")
  private boolean proUser;

  @SerializedName("DefaultProjectId")
  private long defaultProjectId;

  @SerializedName("AddItemMoreExpanded")
  private boolean addItemMoreExpanded;

  @SerializedName("EditDueDateMoreExpanded")
  private boolean editDueDateMoreExpanded;

  @SerializedName("ListSortType")
  private int listSortType;

  @SerializedName("FirstDayOfWee")
  private int firstDayOfWeek;

  @SerializedName("NewTaskDueDate")
  private int newTaskDueDate;

  public void setId(long id) {
    this.id = id;
  }

  public long getId() {
    return id;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getEmail() {
    return email;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPassword() {
    return password;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getFullName() {
    return fullName;
  }

  public void setTimeZone(double timeZone) {
    this.timeZone = timeZone;
  }

  public double getTimeZone() {
    return timeZone;
  }

  public void setProUser(boolean proUser) {
    this.proUser = proUser;
  }

  public boolean isProUser() {
    return proUser;
  }

  public void setDefaultProjectId(long defaultProjectId) {
    this.defaultProjectId = defaultProjectId;
  }

  public long getDefaultProjectId() {
    return defaultProjectId;
  }

  public void setAddItemMoreExpanded(boolean addItemMoreExpanded) {
    this.addItemMoreExpanded = addItemMoreExpanded;
  }

  public boolean isAddItemMoreExpanded() {
    return addItemMoreExpanded;
  }

  public void setEditDueDateMoreExpanded(boolean editDueDateMoreExpanded) {
    this.editDueDateMoreExpanded = editDueDateMoreExpanded;
  }

  public boolean isEditDueDateMoreExpanded() {
    return editDueDateMoreExpanded;
  }

  public void setListSortType(int listSortType) {
    this.listSortType = listSortType;
  }

  public int getListSortType() {
    return listSortType;
  }

  public void setFirstDayOfWeek(int firstDayOfWeek) {
    this.firstDayOfWeek = firstDayOfWeek;
  }

  public int getFirstDayOfWeek() {
    return firstDayOfWeek;
  }

  public void setNewTaskDueDate(int newTaskDueDate) {
    this.newTaskDueDate = newTaskDueDate;
  }

  public int getNewTaskDueDate() {
    return newTaskDueDate;
  }
}
