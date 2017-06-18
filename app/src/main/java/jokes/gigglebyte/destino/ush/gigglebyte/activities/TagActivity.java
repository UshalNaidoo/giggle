package jokes.gigglebyte.destino.ush.gigglebyte.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.List;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.adapters.PostListAdapter;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.JsonParser;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.UIHelper;
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
    setContentView(R.layout.activity_tags);
    activity = this;
    listView = (ListView) findViewById(R.id.listView);

    tag = getIntent().getStringExtra("tag");
    UIHelper.setActionBar(activity);
    new GetTags().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
        UIHelper.setActionBar(activity, tag, true);
        result = "{\"posts\":" + result + "}";
        List<Post> posts = JsonParser.GetPosts(result);
        posts = getPostStatus(activity, posts);
        listView.setAdapter(new PostListAdapter(activity, posts));
      }
    }
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
