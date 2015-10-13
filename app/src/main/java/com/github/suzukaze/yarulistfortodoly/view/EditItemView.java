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

package com.github.suzukaze.yarulistfortodoly.view;

import android.app.FragmentManager;
import android.content.Context;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.github.suzukaze.yarulistfortodoly.R;
import com.github.suzukaze.yarulistfortodoly.activity.MainActivity;
import com.github.suzukaze.yarulistfortodoly.model.Item;
import com.github.suzukaze.yarulistfortodoly.viewmodel.EditItemViewModel;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EditItemView extends LinearLayout {

  private static final String TAG = EditItemView.class.getSimpleName();

  @Bind(R.id.back)
  LinearLayout back;

  @Bind(R.id.item_name)
  EditText itemName;

  @Bind(R.id.delete)
  LinearLayout delete;

  private MainActivity mainActivity;
  private Item item;

  public EditItemView(Context context) {
    super(context);
  }

  public EditItemView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public EditItemView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public void init(final EditItemViewModel editItemViewModel, final InputMethodManager inputMethodManager,
                   MainActivity mainActivity, final ItemListView itemListView, final FragmentManager fragmentManager
  ) {
    this.mainActivity = mainActivity;

    ButterKnife.bind(this);

    back.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        itemListView.show();
        hide();
      }
    });

    itemName.setOnKeyListener(new OnKeyListener() {
      @Override
      public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
          // close software keyboard
          inputMethodManager.hideSoftInputFromWindow(itemName.getWindowToken(),
              InputMethodManager.RESULT_UNCHANGED_SHOWN);

          SpannableStringBuilder sb = (SpannableStringBuilder) itemName.getText();
          item.setContent(sb.toString());
          editItemViewModel.updateItem(item);
          editItemViewModel.save();
          editItemViewModel.parse();
          show();

          return true;
        }
        return false;
      }
    });

    delete.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        editItemViewModel.deleteItem(item);

        editItemViewModel.parse();

        hide();
        itemListView.show();
      }
    });
  }

  public void show() {
    mainActivity.hideToolbar();
    setVisibility(View.VISIBLE);
    if (item != null && item.getContent() != null) {
      itemName.setText(item.getContent());
    }
  }

  public void hide() {
    mainActivity.showToolbar();
    setVisibility(View.INVISIBLE);
  }

  public void setItem(Item item) {
    this.item = item;
  }
}
