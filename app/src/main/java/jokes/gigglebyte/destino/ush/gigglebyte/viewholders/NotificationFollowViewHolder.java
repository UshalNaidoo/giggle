package jokes.gigglebyte.destino.ush.gigglebyte.viewholders;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.UserHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.enums.OpenScreen;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Post;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;

public class NotificationFollowViewHolder extends UserGridViewHolder {

  public TextView informationTextView;
  public TextView followeesName;


  public void setData(final Activity activity, View convertView,  final Post post, OpenScreen screenToOpen) {
    User user = post.getUser();
    User following = post.getFollowingUser();
    setUserProfile(activity, user, screenToOpen);
    String infoText = (user.getName() == null || user.getName().isEmpty() ? activity.getResources().getString(R.string.unknown) : user.getName())
                      + " " + (following.getId() == UserHelper.getUsersId(activity) ? activity.getResources().getString(R.string.started_following_you) : activity.getResources().getString(R.string.started_following));

    LinearLayout userFollowingPanel = (LinearLayout) convertView.findViewById(R.id.userFollowingPanel);
    if(following.getId() == UserHelper.getUsersId(activity) ) {
      userFollowingPanel.setVisibility(View.GONE);
    }else {
      UserGridViewHolder holder = new UserGridViewHolder();
      holder.profileImage = (ImageView) convertView.findViewById(R.id.profileImage);
      holder.followButton = (ImageView) convertView.findViewById(R.id.followeesButton);
      holder.progressBar = (ProgressBar) convertView.findViewById(R.id.followeesProgressBar);
      holder.userName = (TextView) convertView.findViewById(R.id.followeesName);
      holder.setUserData(activity, following, screenToOpen);
      followeesName.setText((following.getName() == null || following.getName().isEmpty() ? activity.getResources().getString(R.string.unknown) : following.getName()));
    }
    informationTextView.setText(infoText);
  }

}