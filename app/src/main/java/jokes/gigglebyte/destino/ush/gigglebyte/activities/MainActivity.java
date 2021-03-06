package jokes.gigglebyte.destino.ush.gigglebyte.activities;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.PostHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.UserHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.dialogs.AddTextByteDialog;
import jokes.gigglebyte.destino.ush.gigglebyte.dialogs.ImageByteOptionsDialog;
import jokes.gigglebyte.destino.ush.gigglebyte.fragments.Fragment_Feed;
import jokes.gigglebyte.destino.ush.gigglebyte.fragments.Fragment_Profile;
import jokes.gigglebyte.destino.ush.gigglebyte.fragments.Tabs_Posts;
import jokes.gigglebyte.destino.ush.gigglebyte.fragments.Tabs_Profile;
import jokes.gigglebyte.destino.ush.gigglebyte.fragments.Tabs_Search;
import jokes.gigglebyte.destino.ush.gigglebyte.interfaces.FragmentLifecycle;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Post;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Tag;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;

import static jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.FollowHelper.getFollowing;

public class MainActivity extends FragmentActivity {

  public static Post selectedPost;
  public static List<String> allTags;
  public static Map<Integer, Bitmap> cachedProfilePictures = new HashMap<>();
  public static List<Tag> loadedTags = new ArrayList<>();
  public static List<User> loadedUsers = new ArrayList<>();
  private static Activity activity;
  private static FloatingActionMenu menuDown;
  private static ViewPager pager;
  private AdView adView;
  private InterstitialAd interstitialAd;
  private SwipePagerAdapter swipePagerAdapter;
  private Tabs_Profile tabs_profile;
  private Fragment_Feed fragment_feed;
  private Tabs_Posts tabs_posts;
  private Tabs_Search tabs_search;
  private int currentPosition = 0;
  private View.OnClickListener clickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      switch (v.getId()) {
        case R.id.addPost:
          popUpAddText(activity);
          break;
        case R.id.addImage:
          ImageByteOptionsDialog imageByteOptionsDialog = new ImageByteOptionsDialog();
          imageByteOptionsDialog.show(getFragmentManager(), "");
          break;
      }
    }
  };
  private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {

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

  public static void popUpAddText(Activity activity) {
    User user = UserHelper.getUserDetails(activity);
    AddTextByteDialog addTextByteDialog = new AddTextByteDialog();
    addTextByteDialog.setListener(new PostHelper());
    addTextByteDialog.setUserId(user.getId());
    addTextByteDialog.setUserName(user.getName());
    addTextByteDialog.show(activity.getFragmentManager(), "");
  }

  public static void changeTab(int position) {
    pager.setCurrentItem(position, true);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = this;
    tabs_profile = new Tabs_Profile();
    fragment_feed = new Fragment_Feed();
    tabs_posts = new Tabs_Posts();
    tabs_search = new Tabs_Search();

    setContentView(R.layout.activity_main);

    adView = (AdView) findViewById(R.id.adView);
    AdRequest adRequest = new AdRequest.Builder().build();
    adView.setBackgroundColor(Color.TRANSPARENT);
    adView.loadAd(adRequest);

    adView.setAdListener(new AdListener() {
      @Override
      public void onAdClosed() {
      }

      @Override
      public void onAdFailedToLoad(int var1) {
        adView.setVisibility(View.GONE);
      }

      @Override
      public void onAdLeftApplication() {
      }

      @Override
      public void onAdOpened() {
      }

      @Override
      public void onAdLoaded() {
        adView.setVisibility(View.VISIBLE);
      }
    });

    interstitialAd = new InterstitialAd(this);
    interstitialAd.setAdUnitId("ca-app-pub-8178977353276350/7496991820");
    interstitialAd.loadAd(new AdRequest.Builder().build());

    PagerTabStrip pagerTabStrip = (PagerTabStrip) findViewById(R.id.pager_title_strip);
    pagerTabStrip.setTabIndicatorColor(activity.getResources().getColor(R.color.text_tab_selected));

    pager = (ViewPager) findViewById(R.id.pager);
    swipePagerAdapter = new SwipePagerAdapter(getSupportFragmentManager());
    pager.setAdapter(swipePagerAdapter);
    pager.setOnPageChangeListener(pageChangeListener);
    menuDown = (FloatingActionMenu) findViewById(R.id.menu_down);

    pager.setCurrentItem(1, true);
    pager.postDelayed(new Runnable() {
      @Override
      public void run() {
        currentPosition = (getFollowing() != null && getFollowing().size() > 0) ? 0 : 2;
        pager.setCurrentItem(currentPosition, true);
      }
    }, 100);

    FloatingActionButton addPost = (FloatingActionButton) findViewById(R.id.addPost);
    FloatingActionButton addImage = (FloatingActionButton) findViewById(R.id.addImage);

    addPost.setOnClickListener(clickListener);
    addImage.setOnClickListener(clickListener);

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
    }
    this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    return item.getItemId() == R.id.action_settings || super.onOptionsItemSelected(item);
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
    } else if (currentPosition == 0 && Fragment_Profile.menuDown.isOpened()) {
      Fragment_Profile.menuDown.hideMenuButton(false);
      new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
          Fragment_Profile.menuDown.showMenuButton(true);
        }
      }, 300);
    } else if (getFollowing().size() > 0 && pager.getCurrentItem() != 0
               || getFollowing().size() == 0 && pager.getCurrentItem() != 2) {
      pager.setCurrentItem(getFollowing().size() > 0 ? 0 : 2, true);
    } else {
      //Show Full screen ad
      if (interstitialAd.isLoaded()) {
        interstitialAd.show();
        interstitialAd.setAdListener(new AdListener() {
          @Override
          public void onAdClosed() {
            super.onAdClosed();
            finish();
          }
        });
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
  }

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

  private class SwipePagerAdapter extends FragmentStatePagerAdapter {

    SwipePagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int i) {
      switch (i) {
        case 0:
          return tabs_profile;
        case 1:
          return fragment_feed;
        case 2:
          return tabs_posts;
        case 3:
          return tabs_search;
        default:
          return null;
      }
    }

    @Override
    public int getCount() {
      return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      switch (position) {
        case 0:
          return activity.getResources().getString(R.string.tab_profile);
        case 1:
          return activity.getResources().getString(R.string.tab_feed);
        case 2:
          return activity.getResources().getString(R.string.tab_posts);
        case 3:
          return activity.getResources().getString(R.string.tab_search);
        default:
          return "";
      }
    }
  }

}
