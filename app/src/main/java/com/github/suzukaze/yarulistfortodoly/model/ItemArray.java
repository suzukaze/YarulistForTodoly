package com.github.suzukaze.yarulistfortodoly.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ItemArray {

  @SerializedName("Items")
  private List<Item> items;

  public ItemArray(List items) {
    this.items = items;
  }

  public List<Item> getItems() {
    return items;
  }
}
