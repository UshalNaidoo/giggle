package jokes.gigglebyte.destino.ush.gigglebyte.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.PopulateViewHolderHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.enums.FromScreen;
import jokes.gigglebyte.destino.ush.gigglebyte.enums.OpenScreen;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Post;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.NotificationFollowViewHolder;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.NotificationImagePostViewHolder;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.NotificationMentionImageViewHolder;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.NotificationMentionTextViewHolder;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.NotificationTextPostViewHolder;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.PostImageViewHolder;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.PostInfoViewHolder;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.PostTextViewHolder;

public class PostListAdapter extends BaseAdapter {

  private List<Post> posts;
  private LayoutInflater mInflater;
  private Activity activity;
  private FromScreen fromScreen;

  public PostListAdapter(Activity activity, List<Post> results, FromScreen fromScreen) {
    this.activity = activity;
    posts = results;
    this.fromScreen = fromScreen;
    mInflater = LayoutInflater.from(activity);
  }

  @Override
  public int getCount() {
    return posts.size();
  }

  @Override
  public Object getItem(int arg0) {
    return posts.get(arg0);
  }

  @Override
  public long getItemId(int arg0) {
    return arg0;
  }

  public View getView(final int position, View convertView, ViewGroup parent) {
    Post post = posts.get(position);
    switch (post.getType()) {
      case TEXT_POST:
        final PostTextViewHolder postTextViewHolder = new PostTextViewHolder();
        convertView = mInflater.inflate(R.layout.post_text_item, parent, false);
        PopulateViewHolderHelper.populatePostTextViewHolder(convertView, postTextViewHolder);
        convertView.setTag(postTextViewHolder);
        postTextViewHolder.setUserData(activity, post.getUser(), OpenScreen.PROFILE);
        postTextViewHolder.setTextPostData(activity,convertView,post, fromScreen);
        break;
      case IMAGE_POST:
        final PostImageViewHolder postImageViewHolder = new PostImageViewHolder();
        convertView = mInflater.inflate(R.layout.post_image_item, parent, false);
        PopulateViewHolderHelper.populatePostImageViewHolder(convertView, postImageViewHolder);
        convertView.setTag(postImageViewHolder);
        postImageViewHolder.setUserData(activity, post.getUser(), OpenScreen.PROFILE);
        postImageViewHolder.setImagePostData(activity, this, convertView, post, fromScreen);
        break;
      case FOLLOWING_NOTIFICATION:
        final NotificationFollowViewHolder notificationFollowViewHolder = new NotificationFollowViewHolder();
        convertView = mInflater.inflate(R.layout.notification_follow_item, parent, false);
        PopulateViewHolderHelper.populateNotificationFollowViewHolder(convertView, notificationFollowViewHolder);
        convertView.setTag(notificationFollowViewHolder);
        notificationFollowViewHolder.setUserData(activity, post.getUser(), OpenScreen.PROFILE);
        notificationFollowViewHolder.setData(activity, convertView, post, OpenScreen.PROFILE);
        break;
      case LIKE_TEXT_POST_NOTIFICATION:
      case COMMENT_TEXT_POST_NOTIFICATION:
        final NotificationTextPostViewHolder notificationTextPostViewHolder = new NotificationTextPostViewHolder();
        convertView = mInflater.inflate(R.layout.notification_on_text_post_item, parent, false);
        PopulateViewHolderHelper.populateNotificationTextPostViewHolder(convertView, notificationTextPostViewHolder);
        convertView.setTag(notificationTextPostViewHolder);
        notificationTextPostViewHolder.setUserData(activity, post.getUser(), OpenScreen.PROFILE);
        notificationTextPostViewHolder.setData(activity, convertView, post, OpenScreen.PROFILE);
        break;
      case LIKE_IMAGE_POST_NOTIFICATION:
      case COMMENT_IMAGE_POST_NOTIFICATION:
        final NotificationImagePostViewHolder notificationImagePostViewHolder = new NotificationImagePostViewHolder();
        convertView = mInflater.inflate(R.layout.notification_on_image_post_item, parent, false);
        PopulateViewHolderHelper.populateNotificationImagePostViewHolder(convertView, notificationImagePostViewHolder);
        convertView.setTag(notificationImagePostViewHolder);
        notificationImagePostViewHolder.setUserData(activity, post.getUser(), OpenScreen.PROFILE);
        notificationImagePostViewHolder.setData(activity, this, convertView, post, OpenScreen.PROFILE);
        break;
      case MENTION_TEXT_POST_NOTIFICATION:
        final NotificationMentionTextViewHolder notificationMentionTextViewHolder = new NotificationMentionTextViewHolder();
        convertView = mInflater.inflate(R.layout.notification_on_text_post_item, parent, false);
        PopulateViewHolderHelper.populateNotificationMentionTextViewHolder(convertView, notificationMentionTextViewHolder);
        convertView.setTag(notificationMentionTextViewHolder);
        notificationMentionTextViewHolder.setUserData(activity, post.getUser(), OpenScreen.PROFILE);
        notificationMentionTextViewHolder.setData(activity, convertView, post, OpenScreen.PROFILE);
        break;
      case MENTION_IMAGE_POST_NOTIFICATION:
        final NotificationMentionImageViewHolder notificationMentionImageViewHolder = new NotificationMentionImageViewHolder();
        convertView = mInflater.inflate(R.layout.notification_on_image_post_item, parent, false);
        PopulateViewHolderHelper.populateNotificationMentionImageViewHolder(convertView, notificationMentionImageViewHolder);
        convertView.setTag(notificationMentionImageViewHolder);
        notificationMentionImageViewHolder.setUserData(activity, post.getUser(), OpenScreen.PROFILE);
        notificationMentionImageViewHolder.setData(activity, this, convertView, post, OpenScreen.PROFILE);
        break;
      case INFO_POST:
        final PostInfoViewHolder holder = new PostInfoViewHolder();
        convertView = mInflater.inflate(R.layout.post_info_item, parent, false);
        PopulateViewHolderHelper.populatePostInfoViewHolder(convertView, holder);
        convertView.setTag(holder);
        holder.setPostData(activity, post, fromScreen);
        break;
    }
    return convertView;
  }

}