package jokes.gigglebyte.destino.ush.gigglebyte.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.UIHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.fragments.Fragment_Search_User;
import jokes.gigglebyte.destino.ush.gigglebyte.interfaces.FragmentLifecycle;

public class SearchActivity extends FragmentActivity {
  private SwipePagerAdapter swipePagerAdapter;
  private Fragment_Search_User search_user;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    search_user = new Fragment_Search_User();
    setContentView(R.layout.activity_search);

    ViewPager pager = (ViewPager) findViewById(R.id.pager);
    swipePagerAdapter = new SwipePagerAdapter(getSupportFragmentManager());
    pager.setAdapter(swipePagerAdapter);
    pager.setOnPageChangeListener(pageChangeListener);

    UIHelper.setActionBar(this, "Search", true);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        this.finish();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  public class SwipePagerAdapter extends FragmentStatePagerAdapter {

    public SwipePagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int i) {
      switch (i) {
        case 0:
          return search_user;
        default:
          return null;
      }
    }

    @Override
    public int getCount() {
      return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      switch (position) {
        case 0:
          return "Users";
        default:
          return "";
      }
    }
  }

  private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
    int currentPosition = 0;

    @Override
    public void onPageSelected(int newPosition) {
      FragmentLifecycle fragmentToShow = (FragmentLifecycle) swipePagerAdapter.getItem(newPosition);
      fragmentToShow.onResumeFragment();
      FragmentLifecycle fragmentToHide = (FragmentLifecycle) swipePagerAdapter.getItem(currentPosition);
      fragmentToHide.onPauseFragment();
      currentPosition = newPosition;
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    public void onPageScrollStateChanged(int arg0) {
    }
  };

}