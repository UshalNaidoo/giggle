package jokes.gigglebyte.destino.ush.gigglebyte.viewholders;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.PopulateViewHolderHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.enums.FromScreen;
import jokes.gigglebyte.destino.ush.gigglebyte.enums.OpenScreen;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Post;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.PostType;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;

public class NotificationTextPostViewHolder extends UserGridViewHolder {

  public TextView informationTextView;

  public void setData(final Activity activity, View convertView, final Post post,
                      OpenScreen screenToOpen) {
    User user = post.getUser();
    setUserProfile(activity, user, screenToOpen);
    String infoText = (user.getName() == null || user.getName().isEmpty() ? activity.getResources()
        .getString(R.string.unknown) : user.getName()) + " " + (PostType.LIKE_TEXT_POST_NOTIFICATION
                                                                    .equals(post.getType())
                                                                ?
                                                                activity.getResources()
                                                                    .getString(R.string.liked_post)
                                                                : activity.getResources()
                                                                    .getString(R.string.commented_on));
    PostTextViewHolder holder = new PostTextViewHolder();
    PopulateViewHolderHelper.populatePostTextViewHolder(convertView, holder, true);
    convertView.setTag(holder);
    holder.setUserData(activity, post.getInnerPost().getUser(), OpenScreen.PROFILE);
    holder.setTextPostData(activity, convertView, post.getInnerPost(), FromScreen.NOTIFICATIONS);
    informationTextView.setText(infoText);
  }

}