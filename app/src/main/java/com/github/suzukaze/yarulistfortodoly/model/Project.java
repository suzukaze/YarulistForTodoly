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

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Project {

  public static final long PROJECT_ID_NONE = -1;

  @SerializedName("Id")
  private long id;

  @SerializedName("Content")
  private String content;

  @SerializedName("ItemsCount")
  private int itemsCount;

  @SerializedName("Icon")
  private int icon;

  @SerializedName("ItemType")
  private int itemType;

  @SerializedName("ParentId")
  private long parentId;

  @SerializedName("Collapsed")
  private boolean collapsed;

  @SerializedName("ItemOrder")
  private int itemOrder;

  @SerializedName("Children")
  private List<Project> children;

  @SerializedName("IsProjectShared")
  private boolean projectShared;

  @SerializedName("ProjectShareOwnerName")
  private String projectShareOwnerName;

  @SerializedName("ProjectShareOwnerEmail")
  private String projectShareOwnerEmail;

  @SerializedName("IsShareApproved")
  private boolean shareApproved;

  @SerializedName("IsOwnProject")
  private boolean ownProject;

  private List<Item> items;

  public Project() {
    items = new ArrayList<>();
    id = PROJECT_ID_NONE;
  }

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

  public void setItemsCount(int itemsCount) {
    this.itemsCount = itemsCount;
  }

  public int getItemsCount() {
    return itemsCount;
  }

  public void setIcon(int icon) {
    this.icon = icon;
  }

  public int getIcon() {
    return icon;
  }

  public void setItemType(int itemType) {
    this.itemType = itemType;
  }

  public int getItemType() {
    return itemType;
  }

  public void setParentId(long parentId) {
    this.parentId = parentId;
  }

  public long getPrantId() {
    return parentId;
  }

  public void setCollapsed(boolean collapsed) {
    this.collapsed = collapsed;
  }

  public boolean isCollapsed() {
    return collapsed;
  }

  public void setItemOrder(int itemOrder) {
    this.itemOrder = itemOrder;
  }

  public int getItemOrder() {
    return itemOrder;
  }

  public void setChildren(List<Project> children) {
    this.children = children;
  }

  public List<Project> getChildren() {
    return children;
  }

  public void setProjectShared(boolean projectShared) {
    this.projectShared = projectShared;
  }

  public boolean isProjectShared() {
    return projectShared;
  }

  public void setProjectShareOwnerName(String projectShareOwnerName) {
    this.projectShareOwnerName = projectShareOwnerName;
  }

  public String getProjectShareOwnerName() {
    return projectShareOwnerName;
  }

  public void setProjectShareOwnerEmail(String projectShareOwnerEmail) {
    this.projectShareOwnerEmail = projectShareOwnerEmail;
  }

  public String getProjectShareOwnerEmail() {
    return projectShareOwnerEmail;
  }

  public void setShareApproved(boolean shareApproved ) {
    this.shareApproved = shareApproved;
  }

  public boolean isShareApproved() {
    return shareApproved;
  }

  public void setOwnProject(boolean ownProject) {
    this.ownProject = ownProject;
  }

  public boolean isOwnProject() {
    return ownProject;
  }

  public void clearItems() {
    items.clear();
  }

  public void addItem(Item item) {
    items.add(item);
  }

  public List<Item> makeUncompletedItems() {
    List<Item> uncompletedItems = new ArrayList<>();
    for (Item item : items) {
      if (!item.isChecked()) {
        uncompletedItems.add(item);
      }
    }
    return uncompletedItems;
  }

  public List<Item> makeCompletedItems() {
    List<Item> completedItems = new ArrayList<>();
    for (Item item : items) {
      if (item.isChecked()) {
        completedItems.add(item);
      }
    }
    return completedItems;
  }
}
