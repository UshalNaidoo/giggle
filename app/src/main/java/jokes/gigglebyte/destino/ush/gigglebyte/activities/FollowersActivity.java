package jokes.gigglebyte.destino.ush.gigglebyte.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.adapters.UserGridAdapter;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.UIHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.UserHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;

public class FollowersActivity extends Activity {

  private Activity activity;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.fragment_followers);
    activity = this;

    Intent intent = getIntent();
    boolean showFollowing = intent.getBooleanExtra("showFollowing", true);

    UIHelper.setActionBar(this, showFollowing ? getResources().getString(R.string.following) : getResources().getString(R.string.followers), true);

    GridView gridView = (GridView) findViewById(R.id.gridView);
    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View v,
          int position, long id) {
        User selectedUser = (User) parent.getItemAtPosition(position);
        Intent myIntent = new Intent(activity, PosterProfileActivity.class);
        myIntent.putExtra("userId", selectedUser.getId());
        activity.startActivity(myIntent);
      }
    });

    UserGridAdapter gridAdapter = new UserGridAdapter(activity, showFollowing ? UserHelper.selectedUser.getFollowing() : UserHelper.selectedUser.getFollowers());
    gridView.setAdapter(gridAdapter);

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