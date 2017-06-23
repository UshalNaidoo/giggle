package jokes.gigglebyte.destino.ush.gigglebyte.viewholders;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.FollowHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.enums.OpenScreen;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;
import jokes.gigglebyte.destino.ush.gigglebyte.widgets.ToastWithImage;

public class UserGridViewHolder extends UserProfilePictureHolder{

  public TextView userName;
  public ImageView followButton;

  public void setUserData(final Activity activity, final User user, OpenScreen screenToOpen) {
    setUserProfile(activity, user, screenToOpen);
    userName.setText(user.getName() == null || user.getName().isEmpty() ? activity.getResources().getString(R.string.unknown) : user.getName() );

    if(followButton != null) {
      followButton.setImageResource(FollowHelper.isFollowingUser(user.getId()) ? R.drawable.following : R.drawable.follow);

      followButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (FollowHelper.isFollowingUser(user.getId())) {
            FollowHelper.unfollowUser(activity, user);
            new ToastWithImage(activity).show(activity.getResources().getString(R.string.unfollowing) + " " + user.getName(), R.drawable.follow);
          }
          else {
            FollowHelper.followUser(activity, user);
            new ToastWithImage(activity).show(activity.getResources().getString(R.string.following) + " " + user.getName(), R.drawable.following);
          }
          followButton.setImageResource(FollowHelper.isFollowingUser(user.getId()) ? R.drawable.following : R.drawable.follow);
        }
      });
    }
  }

}
