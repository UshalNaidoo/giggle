package jokes.gigglebyte.destino.ush.gigglebyte.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MenuItem;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.adapters.UserGridAdapter;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.UIHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.UserHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.enums.FromScreen;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Post;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.PostType;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;

public class FollowersActivity extends Activity {

  private static Activity activity;
  private static UserGridAdapter[] gridAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_followers);
    activity = this;

    Intent intent = getIntent();
    final boolean showFollowing = intent.getBooleanExtra("showFollowing", true);
    final boolean userList = intent.getBooleanExtra("userList", false);

    UIHelper.setActionBar(this, showFollowing ? getResources().getString(R.string.following) : getResources().getString(R.string.followers), true);

    final GridView gridView = (GridView) findViewById(R.id.gridView);
    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View v,
          int position, long id) {
        User selectedUser = (User) parent.getItemAtPosition(position);
        if (selectedUser != null) {
          Intent myIntent = new Intent(activity, PosterProfileActivity.class);
          myIntent.putExtra("userId", selectedUser.getId());
          activity.startActivity(myIntent);
        }
      }
    });

    Post infoPost = new Post();
    if(userList) {
      if (showFollowing) {
        infoPost = new Post(activity.getResources().getString(R.string.info_follow),
                              BitmapFactory.decodeResource(activity.getResources(), R.drawable.follow), PostType.INFO_POST);
      }
      else {
        infoPost = new Post(activity.getResources().getString(R.string.info_add_post),
                              BitmapFactory.decodeResource(activity.getResources(), R.drawable.plus), PostType.INFO_POST);
      }
    }

    final FromScreen fromScreen = !userList ? null : showFollowing ? FromScreen.FOLLOWING : FromScreen.FOLLOWERS;
    List<User> users = showFollowing ? UserHelper.selectedUser.getFollowing() : UserHelper.selectedUser.getFollowers();
    gridAdapter =
        new UserGridAdapter[] { new UserGridAdapter(activity, users, userList, infoPost, fromScreen) };
    gridView.setAdapter(gridAdapter[0]);

    final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe);
    final Post finalInfoPost = infoPost;
    swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        swipeView.setRefreshing(true);
        ( new Handler()).postDelayed(new Runnable() {
          @Override
          public void run() {
            swipeView.setRefreshing(false);

            Thread thread = new Thread() {
              @Override
              public void run() {
                List<User> users = showFollowing ? UserHelper.selectedUser.getFollowing() : UserHelper.selectedUser.getFollowers();
                gridAdapter[0] = new UserGridAdapter(activity, users, userList, finalInfoPost, fromScreen);
              }
            };
            thread.start();
          }
        }, 200);
        gridView.setAdapter(gridAdapter[0]);
      }
    });

  }

  public static void refresh() {
    if (gridAdapter != null && gridAdapter[0] != null) {
      gridAdapter[0].notifyDataSetChanged();
    }
  }

  public static void closeActivity() {
    activity.finish();
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

}