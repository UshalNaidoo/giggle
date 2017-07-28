package jokes.gigglebyte.destino.ush.gigglebyte.viewholders;

import android.app.Activity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.PopulateViewHolderHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.enums.FromScreen;
import jokes.gigglebyte.destino.ush.gigglebyte.enums.OpenScreen;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Post;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;

public class NotificationMentionImageViewHolder extends UserGridViewHolder {

  public TextView informationTextView;

  public void setData(final Activity activity,BaseAdapter adapter, View convertView,  final Post post, OpenScreen screenToOpen) {
    User user = post.getUser();
    setUserProfile(activity, user, screenToOpen);
    String infoText = (user.getName() == null || user.getName().isEmpty() ? activity.getResources().getString(R.string.unknown) : user.getName()) + " " + activity.getResources().getString(R.string.mentioned_on);

    final PostImageViewHolder holder = new PostImageViewHolder();
    PopulateViewHolderHelper.populatePostImageViewHolder(convertView, holder, true);
    convertView.setTag(holder);

    holder.setUserData(activity, post.getInnerPost().getUser(), OpenScreen.PROFILE);
    holder.setImagePostData(activity, adapter, convertView, post.getInnerPost(), FromScreen.NOTIFICATIONS);

    informationTextView.setText(infoText);
  }

}