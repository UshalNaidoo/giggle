package jokes.gigglebyte.destino.ush.gigglebyte.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.List;
import java.util.Set;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.adapters.PosterProfileListAdapter;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.JsonParser;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.SharedPrefHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.UIHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.UserHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.dialogs.OptionsPostDialog;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Post;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;
import jokes.gigglebyte.destino.ush.gigglebyte.server.ConnectToServer;
import jokes.gigglebyte.destino.ush.gigglebyte.widgets.ToastWithImage;

import static jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.PostHelper.getPostStatus;

public class PosterProfileActivity extends Activity {

  private int userId;
  private Activity activity;
  private boolean following;
  private ListView listView;
  public static User poster;
  private Menu menu;
  private String postString;
  private ToastWithImage toastWithImage;
  private Set<String> followingUsers;
  private BaseAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_poster_profile);
    activity = this;
    listView = (ListView) findViewById(R.id.listView);

    userId = getIntent().getIntExtra("userId", 0);

    followingUsers = SharedPrefHelper.getUserFollows(this);
    following = followingUsers.contains(String.valueOf(userId));

    toastWithImage = new ToastWithImage(this);

    UIHelper.setActionBar(activity);
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

  private class GetProfile extends AsyncTask<Integer, Integer, String> {

    @Override
    protected String doInBackground(Integer... params) {
      Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
      postString = ConnectToServer.getUsersPosts(userId);
      return ConnectToServer.getUserDetails(userId);
    }

    @Override
    protected void onPostExecute(String result) {
      poster = JsonParser.GetUser(result);
      poster.setId(userId);
      UIHelper.setActionBar(activity, (poster.getName() == null || poster.getName()
          .isEmpty()) ? getResources().getString(R.string.unknown) : poster.getName(), true);

      postString = "{\"posts\":" + postString + "}";
      List<Post> posts = JsonParser.GetPosts(postString);
      posts = getPostStatus(activity, posts);
      adapter = new PosterProfileListAdapter(activity, posts, poster, OptionsPostDialog.FromScreen.POSTER);
      listView.setAdapter(adapter);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    this.menu = menu;
    getMenuInflater().inflate(R.menu.followbutton, menu);
    getMenuInflater().inflate(R.menu.unfollowbutton, menu);

    //XXX This is just for the release 1.2

    menu.getItem(0).setVisible(false);
    menu.getItem(1).setVisible(false);
    if (UserHelper.getUserDetails(activity).getId() == userId) {
      menu.getItem(0).setVisible(false);
      menu.getItem(1).setVisible(false);
    }else {
      if (following) {
        menu.getItem(0).setVisible(false);
      } else {
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
        toastWithImage.show(getResources().getString(R.string.following) + " " + poster.getName(), R.drawable.star_like);
        menu.getItem(0).setVisible(false);
        menu.getItem(1).setVisible(true);
        followingUsers.add(String.valueOf(userId));
        SharedPrefHelper.saveUserFollows(this, followingUsers);
        return true;
      case R.id.action_unfollow:
        toastWithImage.show(getResources().getString(R.string.unfollowing) + " " + poster.getName(), R.drawable.star_like);
        menu.getItem(0).setVisible(true);
        menu.getItem(1).setVisible(false);
        followingUsers.remove(String.valueOf(userId));
        SharedPrefHelper.saveUserFollows(this, followingUsers);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }
}
