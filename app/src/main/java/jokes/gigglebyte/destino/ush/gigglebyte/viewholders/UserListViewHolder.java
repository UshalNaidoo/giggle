package jokes.gigglebyte.destino.ush.gigglebyte.viewholders;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.activities.FollowersActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.UserHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.enums.OpenScreen;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;

public class UserListViewHolder extends UserProfilePictureHolder {

  public TextView description;
  public Button viewFollowers;
  public Button viewFollowing;

  public void setUserData(final Activity activity, final User user) {
    setUserProfile(activity, user, OpenScreen.LARGE_IMAGE);
    description.setText((user.getDescription() == null || user.getDescription()
        .isEmpty())
                        ? activity.getResources().getString(R.string.newUserDescription)
                        : user.getDescription());
    int followersSize = user.getFollowers() == null ? 0 : user.getFollowers().size();
    int followingSize = user.getFollowing() == null ? 0 : user.getFollowing().size();
    viewFollowers.setText(
        new StringBuilder().append(followersSize).append(" ").append(activity.getResources().getString(R.string.followers)).toString());
    viewFollowing.setText(
        new StringBuilder().append(followingSize).append(" ").append(activity.getResources().getString(R.string.following)).toString());

    viewFollowers.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(activity, FollowersActivity.class);
        intent.putExtra("showFollowing", false);
        intent.putExtra("userList", user.getId() == UserHelper.getUsersId(activity));
        activity.startActivity(intent);
      }
    });

    viewFollowing.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(activity, FollowersActivity.class);
        intent.putExtra("showFollowing", true);
        intent.putExtra("userList", user.getId() == UserHelper.getUsersId(activity));
        activity.startActivity(intent);
      }
    });
  }

}
