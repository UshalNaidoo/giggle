package jokes.gigglebyte.destino.ush.gigglebyte.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.List;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.adapters.PostListAdapter;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.JsonParser;
import jokes.gigglebyte.destino.ush.gigglebyte.enums.FromScreen;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Post;
import jokes.gigglebyte.destino.ush.gigglebyte.server.ConnectToServer;

import static jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.PostHelper.getPostStatus;

public class TagActivity extends Activity {

  private String tag;
  private Activity activity;
  private ListView listView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.fragment_post_list);
    activity = this;
    listView = (ListView) findViewById(R.id.listView);

    tag = getIntent().getStringExtra("tag");

    final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe);
    swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        swipeView.setRefreshing(true);
        (new Handler()).postDelayed(new Runnable() {
          @Override
          public void run() {
            swipeView.setRefreshing(false);
            new GetTags().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
          }
        }, 200);
      }
    });

    new GetTags().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    listView.setOnScrollListener(new AbsListView.OnScrollListener() {
      @Override
      public void onScrollStateChanged(AbsListView view, int scrollState) {
      }

      @Override
      public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
          int totalItemCount) {
        int topRowVerticalPosition =
            (listView == null || listView.getChildCount() == 0) ?
            0 : listView.getChildAt(0).getTop();
        swipeView.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
      }
    });
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

  private class GetTags extends AsyncTask<Integer, Integer, String> {

    @Override
    protected String doInBackground(Integer... params) {
      Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
      if (tag.length() > 1) {
        return ConnectToServer.getTagPosts(tag.substring(1));
      } else {
        return null;
      }
    }

    @Override
    protected void onPostExecute(String result) {
      if (result != null) {
        result = "{\"posts\":" + result + "}";
        List<Post> posts = JsonParser.GetPosts(result);
        posts = getPostStatus(activity, posts);
        listView.setAdapter(new PostListAdapter(activity, posts, FromScreen.TAG));
      }
    }
  }
}
