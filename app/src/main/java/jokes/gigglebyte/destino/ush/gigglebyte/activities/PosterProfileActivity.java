package jokes.gigglebyte.destino.ush.gigglebyte.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.adapters.PosterProfileListAdapter;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.FollowHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.JsonParser;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.UserHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.enums.FromScreen;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Post;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;
import jokes.gigglebyte.destino.ush.gigglebyte.server.ConnectToServer;
import jokes.gigglebyte.destino.ush.gigglebyte.widgets.ToastWithImage;

import static jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.PostHelper.getPostStatus;

public class PosterProfileActivity extends Activity {

  public static User poster;
  private static TextView userName;
  private Button followButton;
  private int userId;
  private Activity activity;
  private ListView listView;
  private Menu menu;
  private ToastWithImage toastWithImage;
  private List<Post> posts;
  private List<User> following;
  private List<User> followers;
  private BaseAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_poster_profile);
    activity = this;
    userName = (TextView) findViewById(R.id.userName);
    listView = (ListView) findViewById(R.id.listView);
    followButton = (Button) findViewById(R.id.followButton);
    followButton.setVisibility(View.INVISIBLE);
    userId = getIntent().getIntExtra("userId", 0);

    toastWithImage = new ToastWithImage(this);

    final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe);
    swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        swipeView.setRefreshing(true);
        (new Handler()).postDelayed(new Runnable() {
          @Override
          public void run() {
            swipeView.setRefreshing(false);
            new GetProfile().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
          }
        }, 200);
      }
    });

    new GetProfile().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    listView.setOnScrollListener(new AbsListView.OnScrollListener() {
      @Override
      public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == 0) {
          adapter.notifyDataSetChanged();
        }
      }

      @Override
      public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                           int totalItemCount) {
      }
    });
  }

  @Override
  public void onResume() {
    super.onResume();
    if (adapter != null) {
      adapter.notifyDataSetChanged();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    this.menu = menu;
    getMenuInflater().inflate(R.menu.followbutton, menu);
    getMenuInflater().inflate(R.menu.unfollowbutton, menu);

    menu.getItem(0).setVisible(false);
    menu.getItem(1).setVisible(false);
    if (UserHelper.getUsersId(activity) == userId) {
      menu.getItem(0).setVisible(false);
      menu.getItem(1).setVisible(false);
    } else {
      if (FollowHelper.isFollowingUser(userId)) {
        menu.getItem(0).setVisible(false);
        menu.getItem(1).setVisible(true);
      } else {
        menu.getItem(0).setVisible(true);
        menu.getItem(1).setVisible(false);
      }
    }

    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        this.finish();
        return true;
      case R.id.action_follow:
        toastWithImage.show(getResources().getString(R.string.following) + " "
                            + poster.getName(), R.drawable.following);
        menu.getItem(0).setVisible(false);
        menu.getItem(1).setVisible(true);
        FollowHelper.followUser(activity, poster);
        return true;
      case R.id.action_unfollow:
        toastWithImage.show(getResources().getString(R.string.unfollowing) + " "
                            + poster.getName(), R.drawable.follow);
        menu.getItem(0).setVisible(true);
        menu.getItem(1).setVisible(false);
        FollowHelper.unfollowUser(activity, poster);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private class GetProfile extends AsyncTask<Integer, Integer, String> {

    @Override
    protected String doInBackground(Integer... params) {
      Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
      posts = JsonParser.GetPosts("{\"posts\":" + ConnectToServer.getUsersPosts(userId) + "}");
      following = JsonParser.GetUsers(
          "{\"users\":" + ConnectToServer.getUserFollowing(userId) + "}");
      followers = JsonParser.GetUsers(
          "{\"users\":" + ConnectToServer.getUserFollowers(userId) + "}");
      return ConnectToServer.getUserDetails(userId);
    }

    @Override
    protected void onPostExecute(String result) {
      poster = JsonParser.GetUser(result);
      poster.setId(userId);
      poster.setFollowing(following);
      poster.setFollowers(followers);
      UserHelper.selectedUser = poster;
      userName.setText((poster.getName() == null || poster.getName().isEmpty())
                       ? getResources().getString(R.string.unknown)
                       : poster.getName());

      posts = getPostStatus(activity, posts);
      adapter = new PosterProfileListAdapter(activity, posts, poster, FromScreen.POSTER);


      followButton.setText(FollowHelper.isFollowingUser(userId) ? "Following" : "Follow");
      followButton.setBackgroundColor(FollowHelper.isFollowingUser(userId)
                                      ? activity.getResources().getColor(R.color.button_ripple)
                                      : activity.getResources().getColor(R.color.button_color));
      followButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (FollowHelper.isFollowingUser(userId)) {
            FollowHelper.unfollowUser(activity, poster);
            new ToastWithImage(activity).show(
                activity.getResources().getString(R.string.unfollowing) + " "
                + poster.getName(), null);
            followButton.setText("Follow");
            followButton.setBackgroundColor(activity.getResources()
                                                .getColor(R.color.button_color));

          } else {
            FollowHelper.followUser(activity, poster);
            new ToastWithImage(activity).show(
                activity.getResources().getString(R.string.following) + " "
                + poster.getName(), null);
            followButton.setText("Following");
            followButton.setBackgroundColor(activity.getResources()
                                                .getColor(R.color.button_ripple));
          }
        }
      });
      followButton.setVisibility(View.VISIBLE);
      listView.setAdapter(adapter);
    }
  }

}
