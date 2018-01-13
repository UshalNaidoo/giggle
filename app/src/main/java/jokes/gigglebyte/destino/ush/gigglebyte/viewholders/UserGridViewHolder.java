package jokes.gigglebyte.destino.ush.gigglebyte.viewholders;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.FollowHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.UserHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.enums.OpenScreen;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;
import jokes.gigglebyte.destino.ush.gigglebyte.widgets.ToastWithImage;

public class UserGridViewHolder extends UserProfilePictureHolder{

  public TextView userName;
  public TextView followButton;

  public void setUserData(final Activity activity, final User user, OpenScreen screenToOpen) {
    setUserProfile(activity, user, screenToOpen);
    userName.setText(user.getName() == null || user.getName().isEmpty() ? activity.getResources().getString(R.string.unknown) : user.getName() );

    if(followButton != null) {
      if (user.getId() == UserHelper.getUsersId(activity)) {
        followButton.setVisibility(View.INVISIBLE);
      }
      else {
        followButton.setText(FollowHelper.isFollowingUser(user.getId()) ? "Following" : "Follow");
        followButton.setBackgroundColor(FollowHelper.isFollowingUser(user.getId()) ? activity.getResources().getColor(R.color.button_ripple) : activity.getResources().getColor(R.color.button_color));
        followButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            if (FollowHelper.isFollowingUser(user.getId())) {
              FollowHelper.unfollowUser(activity, user);
              new ToastWithImage(activity).show(activity.getResources().getString(R.string.unfollowing) + " " + user.getName(), null);
              followButton.setText("Follow");
              followButton.setBackgroundColor(activity.getResources().getColor(R.color.button_color));

            } else {
              FollowHelper.followUser(activity, user);
              new ToastWithImage(activity).show(activity.getResources().getString(R.string.following) + " " + user.getName(), null);
              followButton.setText("Following");
              followButton.setBackgroundColor(activity.getResources().getColor(R.color.button_ripple));
            }
          }
        });
      }
    }
  }

}
