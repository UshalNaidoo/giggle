package jokes.gigglebyte.destino.ush.gigglebyte.viewholders;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.activities.FollowersActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.enums.OpenScreen;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;

public class UserListViewHolder extends UserProfilePictureHolder{

  public TextView description;
  public Button viewFollowers;
  public Button viewFollowing;

  public void setUserData(final Activity activity, final User user) {
    setUserProfile(activity, user, OpenScreen.LARGE_IMAGE);
    description.setText((user.getDescription() == null || user.getDescription()
        .isEmpty()) ? activity.getResources().getString(R.string.newUserDescription): user.getDescription());
    viewFollowers.setText(user.getFollowers().size() + " " + activity.getResources().getString(R.string.followers));
    viewFollowing.setText(user.getFollowing().size() + " " + activity.getResources().getString(R.string.following));

    viewFollowers.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(activity, FollowersActivity.class);
        intent.putExtra("showFollowing", false);
        activity.startActivity(intent);
      }
    });

    viewFollowing.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(activity, FollowersActivity.class);
        intent.putExtra("showFollowing", true);
        activity.startActivity(intent);
      }
    });
  }

}
