package jokes.gigglebyte.destino.ush.gigglebyte.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.HashMap;
import java.util.Map;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.PostHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.UIHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.UserHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.dialogs.AddTextByteDialog;
import jokes.gigglebyte.destino.ush.gigglebyte.dialogs.ImageByteOptionsDialog;
import jokes.gigglebyte.destino.ush.gigglebyte.fragments.Fragment_Favorite;
import jokes.gigglebyte.destino.ush.gigglebyte.fragments.Fragment_Feed;
import jokes.gigglebyte.destino.ush.gigglebyte.fragments.Fragment_Hot;
import jokes.gigglebyte.destino.ush.gigglebyte.fragments.Fragment_New;
import jokes.gigglebyte.destino.ush.gigglebyte.fragments.Fragment_Search_Tag;
import jokes.gigglebyte.destino.ush.gigglebyte.interfaces.FragmentLifecycle;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Post;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;

public class MainActivity extends FragmentActivity {

  private Activity activity;
  public static Post selectedPost;
  public static Map<Integer, Bitmap> cachedProfilePictures = new HashMap<>();
  private static FloatingActionMenu menuDown;
  private SwipePagerAdapter swipePagerAdapter;
  private static ViewPager pager;
  private Fragment_Feed fragment_feed;
  private Fragment_New fragment_new;
  private Fragment_Hot fragment_hot;
  private Fragment_Favorite fragment_favorite;
  private Fragment_Search_Tag fragment_tags;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = this;
    fragment_feed = new Fragment_Feed();
    fragment_new = new Fragment_New();
    fragment_hot = new Fragment_Hot();
    fragment_favorite = new Fragment_Favorite();
    fragment_tags = new Fragment_Search_Tag();

    setContentView(R.layout.activity_main);
    pager = (ViewPager) findViewById(R.id.pager);
    swipePagerAdapter = new SwipePagerAdapter(getSupportFragmentManager());
    pager.setAdapter(swipePagerAdapter);
    pager.setOnPageChangeListener(pageChangeListener);
    menuDown = (FloatingActionMenu) findViewById(R.id.menu_down);

    UIHelper.setActionBar(this);

    pager.setCurrentItem(1, true);
    pager.postDelayed(new Runnable() {
      @Override
      public void run() {
        pager.setCurrentItem(0);
      }
    },100);

    FloatingActionButton addPost = (FloatingActionButton) findViewById(R.id.addPost);
    FloatingActionButton addImage = (FloatingActionButton) findViewById(R.id.addImage);
    FloatingActionButton searchUser = (FloatingActionButton) findViewById(R.id.searchUser);
    FloatingActionButton viewProfile = (FloatingActionButton) findViewById(R.id.viewProfile);

    addPost.setOnClickListener(clickListener);
    addImage.setOnClickListener(clickListener);
    searchUser.setOnClickListener(clickListener);
    viewProfile.setOnClickListener(clickListener);

    // Get intent, action and MIME type
    Intent intent = getIntent();
    String action = intent.getAction();
    String type = intent.getType();

    if (Intent.ACTION_SEND.equals(action) && type != null) {
      if ("text/plain".equals(type)) {
        handleSendText(intent); // Handle text being sent
      } else if (type.startsWith("image/")) {
        handleSendImage(intent); // Handle single image being sent
      }
    }this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
  }

  void handleSendText(Intent intent) {
    String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
    if (sharedText != null) {
      // Update UI to reflect text being shared
      User user = UserHelper.getUserDetails(activity);
      AddTextByteDialog addTextByteDialog = new AddTextByteDialog();
      addTextByteDialog.setListener(new PostHelper());
      addTextByteDialog.setUserId(user.getId());
      addTextByteDialog.setUserName(user.getName());
      Bundle args = new Bundle();
      args.putString("text", sharedText);
      addTextByteDialog.setArguments(args);
      addTextByteDialog.show(getFragmentManager(), "");
    }
  }

  void handleSendImage(Intent intent) {
    Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
    if (imageUri != null) {
      Intent imagePostIntent = new Intent(activity, UploadImageActivity.class);
      imagePostIntent.putExtra("uri", imageUri.toString());
      imagePostIntent.putExtra("camera_request", false);
      startActivity(imagePostIntent);
    }
  }

  private View.OnClickListener clickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      User user = UserHelper.getUserDetails(activity);
      switch (v.getId()) {
        case R.id.addPost:
          AddTextByteDialog addTextByteDialog = new AddTextByteDialog();
          addTextByteDialog.setListener(new PostHelper());
          addTextByteDialog.setUserId(user.getId());
          addTextByteDialog.setUserName(user.getName());
          addTextByteDialog.show(getFragmentManager(), "");
          break;
        case R.id.addImage:
          ImageByteOptionsDialog imageByteOptionsDialog = new ImageByteOptionsDialog();
          imageByteOptionsDialog.show(getFragmentManager(), "");
          break;
        case R.id.searchUser:
          Intent intent = new Intent(activity, SearchActivity.class);
          intent.putExtra("useSearchBar", true);
          startActivity(intent);
          break;
        case R.id.viewProfile:
          viewProfile(activity);
          break;
      }
    }
  };

  public static void viewProfile(Activity activity) {
    Intent profileIntent = new Intent(activity, UserProfileActivity.class);
    activity.startActivity(profileIntent);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    return item.getItemId() == R.id.action_settings || super.onOptionsItemSelected(item);
  }

  private class SwipePagerAdapter extends FragmentStatePagerAdapter {

    SwipePagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int i) {
      switch (i) {
        case 0:
          return fragment_feed;
        case 1:
          return fragment_tags;
        case 2:
          return fragment_hot;
        case 3:
          return fragment_new;
        case 4:
          return fragment_favorite;
        default:
          return null;
      }
    }

    @Override
    public int getCount() {
      return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      switch (position) {
        case 0:
          return activity.getResources().getString(R.string.tab_feed);
        case 1:
          return activity.getResources().getString(R.string.tab_tags);
        case 2:
          return activity.getResources().getString(R.string.tab_hot);
        case 3:
          return activity.getResources().getString(R.string.tab_new);
        case 4:
          return activity.getResources().getString(R.string.tab_favourite);
        default:
          return "";
      }
    }
  }

  @Override
  public void onBackPressed() {
    if (menuDown.isOpened()) {
      menuDown.hideMenuButton(false);
      new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
          menuDown.showMenuButton(true);
        }
      }, 300);
    }
    else
      if (pager.getCurrentItem() != 0) {
      pager.setCurrentItem(0, true);
    } else {
      if (SplashScreenActivity.serverCall != null) {
        SplashScreenActivity.serverCall.cancel(true);
      }
      for (Post p : PostHelper.getFavoritePosts()) {
        p.cancelLoadingImages();
      }
      for (Post p : PostHelper.getHotPosts()) {
        p.cancelLoadingImages();
      }
        for (Post p : PostHelper.getNewPosts()) {
          p.cancelLoadingImages();
        }
        for (Post p : PostHelper.getFeedPosts()) {
          p.cancelLoadingImages();
        }
      cachedProfilePictures = new HashMap<>();
      MainActivity.super.onBackPressed();
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

  @Override
  protected void onResume() {
    super.onResume();
    if (menuDown != null) {
      menuDown.hideMenuButton(false);
      new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
          menuDown.showMenuButton(true);
        }
      }, 300);
    }
  }
}
