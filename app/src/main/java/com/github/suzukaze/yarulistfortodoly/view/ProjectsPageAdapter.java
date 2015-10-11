package com.github.suzukaze.yarulistfortodoly.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.github.suzukaze.yarulistfortodoly.fragment.ItemListFragment;
import com.github.suzukaze.yarulistfortodoly.model.Project;

import java.util.ArrayList;
import java.util.List;

public class ProjectsPageAdapter extends FragmentPagerAdapter {

  private static final String TAG = ProjectsPageAdapter.class.getSimpleName();

  private List<Project> projects = new ArrayList<>();

  public ProjectsPageAdapter(FragmentManager fragmentManager) {
    super(fragmentManager);
  }

  public void clear() {
    projects.clear();
  }

  public void addAll(List<Project> projects) {
    this.projects.addAll(projects);
  }

  @Override
  public Fragment getItem(int position) {
    return ItemListFragment.newInstance(projects.get(position));
  }

  @Override
  public int getCount() {
    return projects.size();
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return projects.get(position).getContent();
  }

  @Override
  public int getItemPosition(Object object) {
    return POSITION_NONE;
  }

  public void destroyAllItem(ViewPager pager) {
    for (int i = 0, size = getCount(); i < size - 1; i++) {
      Object obj = instantiateItem(pager, i);
      if (obj != null)
        destroyItem(pager, i, obj);
    }
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    super.destroyItem(container, position, object);

    if (position <= getCount()) {
      FragmentManager manager = ((Fragment) object).getFragmentManager();
      FragmentTransaction trans = manager.beginTransaction();
      trans.remove((Fragment) object);
      trans.commit();
    }
  }

  public Fragment findFragmentByPosition(ViewPager viewPager, int position) {
    return (Fragment) instantiateItem(viewPager, position);
  }
}
