package com.github.suzukaze.yarulistfortodoly.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Item {

  @SerializedName("Id")
  private long id;

  @SerializedName("Content")
  private String content;

  @SerializedName("ItemType")
  private int itemType;

  @SerializedName("Checked")
  private boolean checked;

  @SerializedName("ProjectId")
  private long projectId;

  @SerializedName("ParentId")
  private long parentId;

  @SerializedName("Path")
  private String path;

  @SerializedName("Collapsed")
  private boolean collapsed;

  @SerializedName("DateString")
  private String dateString;

  @SerializedName("DateStringPriority")
  private int dateStringPriority;

  @SerializedName("DueDated")
  private String dueDated;

  @SerializedName("ItemOrder")
  private int itemOrder;

  @SerializedName("Priority")
  private int priority;

  @SerializedName("LastSyncedDateTime")
  private String lastSyncedDateTime;

  @SerializedName("Children")
  private List<Item> children;

  @SerializedName("CreatedDate")
  private String createdDate;

  @SerializedName("LastCheckedDate")
  private String lastCheckedDate;

  @SerializedName("LastUpdatedDated")
  private String lastUpdatedDate;

  @SerializedName("Deleted")
  private boolean deleted;

  public void setId(long id) {
    this.id = id;
  }

  public long getId() {
    return id;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getContent() {
    return content;
  }

  public void setItemType(int itemType) {
    this.itemType = itemType;
  }

  public int getItemType() {
    return itemType;
  }

  public void setChecked(boolean checked) {
    this.checked = checked;
  }

  public boolean isChecked() {
    return checked;
  }

  public void setParentId(long parentId) {
    this.parentId = parentId;
  }

  public long getParentId() {
    return parentId;
  }

  public void setProjectId(long projectId) {
    this.projectId = projectId;
  }

  public long getProjectId() {
    return projectId;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getPath() {
    return path;
  }

  public void setCollapsed(boolean collapsed) {
    this.collapsed = collapsed;
  }

  public boolean isCollapsed() {
    return collapsed;
  }

  public void setDateString(String dateString) {
    this.dateString = dateString;
  }

  public String getDateString() {
    return dateString;
  }

  public void setDateStringPriority(int dateStringPriority) {
    this.dateStringPriority = dateStringPriority;
  }

  public int getDateStringPriority() {
    return dateStringPriority;
  }

  public void setDueDateD(String dueDated) {
    this.dueDated = dueDated;
  }

  public String getDueDated() {
    return dueDated;
  }

  public void setItemOrder(int itemOrder) {
    this.itemOrder = itemOrder;
  }

  public int getItemOrder() {
    return itemOrder;
  }

  public void setPriority(int priority) {
    this.priority = priority;
  }

  public int getPriority() {
    return priority;
  }

  public void setLastSyncedDateTime(String lastSyncedDateTime) {
    this.lastSyncedDateTime = lastSyncedDateTime;
  }

  public String getLastSyncedDateTime() {
    return lastSyncedDateTime;
  }

  public void setChildren(List<Item> children) {
    this.children = children;
  }

  public List<Item> getChildren() {
    return children;
  }

  public void setCreatedDate(String createdDate) {
    this.createdDate = createdDate;
  }

  public String getCreatedDate() {
    return createdDate;
  }

  public void setLastCheckedDate(String lastCheckedDate) {
    this.lastCheckedDate = lastCheckedDate;
  }

  public String getLastCheckedDate() {
    return lastCheckedDate;
  }

  public void setLastUpdatedDate(String lastUpdatedDate) {
    this.lastUpdatedDate = lastUpdatedDate;
  }

  public String getLastUpdatedDate() {
    return lastUpdatedDate;
  }

  public void setDueDated(boolean deleted) {
    this.deleted = deleted;
  }

  public boolean isDeleted() {
    return deleted;
  }
}
