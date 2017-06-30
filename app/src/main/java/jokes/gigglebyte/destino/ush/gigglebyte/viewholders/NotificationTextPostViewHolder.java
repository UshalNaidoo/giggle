package jokes.gigglebyte.destino.ush.gigglebyte.viewholders;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.enums.FromScreen;
import jokes.gigglebyte.destino.ush.gigglebyte.enums.OpenScreen;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Post;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.PostType;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;

public class NotificationTextPostViewHolder extends UserGridViewHolder {

  public TextView informationTextView;

  public void setData(final Activity activity, View convertView,  final Post post, OpenScreen screenToOpen) {
    User user = post.getUser();
    setUserProfile(activity, user, screenToOpen);
    String infoText = (user.getName() == null || user.getName().isEmpty() ? activity.getResources().getString(R.string.unknown) : user.getName()) + " " + (PostType.LIKE_TEXT_POST_NOTIFICATION.equals(post.getType()) || PostType.LIKE_IMAGE_POST_NOTIFICATION.equals(post.getType()) ?
                                                                                                                                                           activity.getResources().getString(R.string.liked_post) : activity.getResources().getString(R.string.commented_on));

    PostTextViewHolder holder = new PostTextViewHolder();
    holder.userName = (TextView) convertView.findViewById(R.id.content_userName);
    holder.profileImage = (ImageView) convertView.findViewById(R.id.content_pic);
    holder.progressBar = (ProgressBar) convertView.findViewById(R.id.content_progressBar);
    holder.followButton = (ImageView) convertView.findViewById(R.id.content_followButton);

    holder.postInfo = (TextView) convertView.findViewById(R.id.postInfo);
    holder.tags =  (TextView) convertView.findViewById(R.id.tags);
    holder.postText = (TextView) convertView.findViewById(R.id.postText);
    holder.layout = (LinearLayout) convertView.findViewById(R.id.layoutView);
    holder.likeImage = (ImageView) convertView.findViewById(R.id.likeImage);
    holder.favoriteImage = (ImageView) convertView.findViewById(R.id.favoriteImage);
    holder.shareImage = (ImageView) convertView.findViewById(R.id.shareImage);
    holder.menuImage = (ImageView) convertView.findViewById(R.id.menuImage);
    convertView.setTag(holder);

    holder.setUserData(activity, post.getInnerPost().getUser(), OpenScreen.PROFILE);
    holder.setTextPostData(activity, convertView, post.getInnerPost(), FromScreen.FEED);

    informationTextView.setText(infoText);
  }

}