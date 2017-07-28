package jokes.gigglebyte.destino.ush.gigglebyte.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.PopulateViewHolderHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.enums.FromScreen;
import jokes.gigglebyte.destino.ush.gigglebyte.enums.OpenScreen;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Post;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.PostType;
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
    if (posts.get(position).getType() == PostType.TEXT_POST) {
      final PostTextViewHolder holder = new PostTextViewHolder();
      convertView = mInflater.inflate(R.layout.post_text_item, parent, false);
      PopulateViewHolderHelper.populatePostTextViewHolder(convertView, holder);
      convertView.setTag(holder);
      Post post = posts.get(position);
      holder.setUserData(activity, post.getUser(), OpenScreen.PROFILE);
      holder.setTextPostData(activity,convertView,post, fromScreen);
    }
    else if (posts.get(position).getType() == PostType.IMAGE_POST) {
      final PostImageViewHolder holder = new PostImageViewHolder();
      convertView = mInflater.inflate(R.layout.post_image_item, parent, false);
      PopulateViewHolderHelper.populatePostImageViewHolder(convertView, holder);
      convertView.setTag(holder);
      Post post = posts.get(position);
      holder.setUserData(activity, post.getUser(), OpenScreen.PROFILE);
      holder.setImagePostData(activity, this, convertView, post, fromScreen);
    }
    else if (posts.get(position).getType() == PostType.INFO_POST) {
      final PostInfoViewHolder holder = new PostInfoViewHolder();
      convertView = mInflater.inflate(R.layout.post_info_item, parent, false);
      PopulateViewHolderHelper.populatePostInfoViewHolder(convertView, holder);
      convertView.setTag(holder);
      holder.setPostData(activity, posts.get(position), fromScreen);
    }
    else if (posts.get(position).getType() == PostType.FOLLOWING_NOTIFICATION) {
      final NotificationFollowViewHolder holder = new NotificationFollowViewHolder();
      convertView = mInflater.inflate(R.layout.notification_follow_item, parent, false);
      PopulateViewHolderHelper.populateNotificationFollowViewHolder(convertView, holder);
      convertView.setTag(holder);
      Post post = posts.get(position);
      holder.setUserData(activity, post.getUser(), OpenScreen.PROFILE);
      holder.setData(activity, convertView, post, OpenScreen.PROFILE);
    }
    else if (posts.get(position).getType() == PostType.LIKE_TEXT_POST_NOTIFICATION || posts.get(position).getType() == PostType.COMMENT_TEXT_POST_NOTIFICATION) {
      final NotificationTextPostViewHolder holder = new NotificationTextPostViewHolder();
      convertView = mInflater.inflate(R.layout.notification_on_text_post_item, parent, false);
      PopulateViewHolderHelper.populateNotificationTextPostViewHolder(convertView, holder);
      convertView.setTag(holder);
      Post post = posts.get(position);
      holder.setUserData(activity, post.getUser(), OpenScreen.PROFILE);
      holder.setData(activity, convertView, post, OpenScreen.PROFILE);
    }
    else if (posts.get(position).getType() == PostType.LIKE_IMAGE_POST_NOTIFICATION || posts.get(position).getType() == PostType.COMMENT_IMAGE_POST_NOTIFICATION) {
      final NotificationImagePostViewHolder holder = new NotificationImagePostViewHolder();
      convertView = mInflater.inflate(R.layout.notification_on_image_post_item, parent, false);
      PopulateViewHolderHelper.populateNotificationImagePostViewHolder(convertView, holder);
      convertView.setTag(holder);
      Post post = posts.get(position);
      holder.setUserData(activity, post.getUser(), OpenScreen.PROFILE);
      holder.setData(activity, this, convertView, post, OpenScreen.PROFILE);
    }
    else if (posts.get(position).getType() == PostType.MENTION_TEXT_POST_NOTIFICATION) {
      final NotificationMentionTextViewHolder holder = new NotificationMentionTextViewHolder();
      convertView = mInflater.inflate(R.layout.notification_on_text_post_item, parent, false);
      PopulateViewHolderHelper.populateNotificationMentionTextViewHolder(convertView, holder);
      convertView.setTag(holder);
      Post post = posts.get(position);
      holder.setUserData(activity, post.getUser(), OpenScreen.PROFILE);
      holder.setData(activity, convertView, post, OpenScreen.PROFILE);
    }
    else if (posts.get(position).getType() == PostType.MENTION_IMAGE_POST_NOTIFICATION) {
      final NotificationMentionImageViewHolder holder = new NotificationMentionImageViewHolder();
      convertView = mInflater.inflate(R.layout.notification_on_image_post_item, parent, false);
      PopulateViewHolderHelper.populateNotificationMentionImageViewHolder(convertView, holder);
      convertView.setTag(holder);
      Post post = posts.get(position);
      holder.setUserData(activity, post.getUser(), OpenScreen.PROFILE);
      holder.setData(activity, this, convertView, post, OpenScreen.PROFILE);
    }

    return convertView;
  }

}