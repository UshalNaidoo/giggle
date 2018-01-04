package jokes.gigglebyte.destino.ush.gigglebyte.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.github.clans.fab.FloatingActionButton;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.adapters.CommentListAdapter;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.CommentHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.JsonParser;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.UIHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.UserHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.dialogs.AddCommentDialog;
import jokes.gigglebyte.destino.ush.gigglebyte.interfaces.onSubmitListener;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Comment;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;
import jokes.gigglebyte.destino.ush.gigglebyte.server.ConnectToServer;

public class CommentActivity extends Activity implements onSubmitListener {

  private static CommentListAdapter commentListAdapter;
  private static Activity activity;
  private static int postId;
  private int posterId;
  private FloatingActionButton floatingActionButton;
  private static ListView listView;
  private static List<Comment> comments;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = this;
    setContentView(R.layout.activity_comments);

    listView = (ListView) findViewById(R.id.listView);

    Intent intent = getIntent();
    postId = intent.getIntExtra("postId", 0);
    posterId = intent.getIntExtra("posterId", 0);

    reload();

    final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe);
    swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        swipeView.setRefreshing(true);
        ( new Handler()).postDelayed(new Runnable() {
          @Override
          public void run() {
            swipeView.setRefreshing(false);
            reload();
          }
        }, 200);
      }
    });

    UIHelper.setActionBar(this, getResources().getString(R.string.comments), true);

    floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
    floatingActionButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        User user = UserHelper.getUserDetails(activity);
        AddCommentDialog addCommentDialog = new AddCommentDialog();
        addCommentDialog.setListener(CommentActivity.this);
        addCommentDialog.setPostId(postId);
        addCommentDialog.setPosterId(posterId);
        addCommentDialog.setUserId(user.getId());
        addCommentDialog.setUserName(user.getName());
        addCommentDialog.show(getFragmentManager(), "");
      }
    });
    floatingActionButton.hide(false);

    listView.setOnScrollListener(new AbsListView.OnScrollListener() {
      @Override
      public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == 0) {
          commentListAdapter.notifyDataSetChanged();
        }
      }

      @Override
      public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                           int totalItemCount) {
      }
    });
  }

  public static void reload() {

    new GetPostComments(activity, postId).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
  }
  private static class GetPostComments extends AsyncTask<Integer, Integer, List<Comment>> {

    Activity activity;
    int postId;

    GetPostComments(Activity activity, int postId) {
      this.postId = postId;
      this.activity = activity;
    }

    @Override
    protected List<Comment> doInBackground(Integer... params) {
      return JsonParser.GetComments(
          "{\"comments\":" + ConnectToServer.getComments(this.postId) + "}");
    }

    @Override
    protected void onPostExecute(List<Comment> comm) {
      commentListAdapter = new CommentListAdapter(activity, comm, MainActivity.selectedPost);
      listView.setAdapter(commentListAdapter);

      SharedPreferences sharedPreferences = activity.getSharedPreferences("COMMENT_LIKES", MODE_PRIVATE);
      Set<String> likes = sharedPreferences.getStringSet("COMMENT_LIKES", new HashSet<String>());
      for (Comment c : comm) {
        for (String s : likes) {
          if (c.getCommentId() == Integer.parseInt(s)) {
            c.setUserLike(true);
          }
        }
      }
      comments = comm;
      Handler handler = new Handler();
      handler.post(new Runnable() {
        public void run() {
          commentListAdapter.notifyDataSetChanged();
        }
      });
    }

    @Override
    protected void onPreExecute() {
    }
  }

  public static List<Comment> getComments() {
    return comments;
  }

  @Override
  protected void onResume() {
    animateFloatingActionButton();
    super.onResume();
  }

  private void animateFloatingActionButton() {
    floatingActionButton.hide(false);
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        floatingActionButton.show(true);
      }
    }, 300);
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

  @Override
  public void setOnSubmitListener(Object arg) {
    Comment comment = (Comment) arg;
    getComments().add(comment);
    MainActivity.selectedPost.setCommentCount(MainActivity.selectedPost.getCommentCount() + 1);
    commentListAdapter.updateAdapter(getComments());
    CommentHelper.addComment(comment);
    commentListAdapter.notifyDataSetChanged();
  }

  @Override
  public void setOnSubmitListener(Activity activity, Object arg) {
  }
}