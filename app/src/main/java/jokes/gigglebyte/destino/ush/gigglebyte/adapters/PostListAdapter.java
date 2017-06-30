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
import jokes.gigglebyte.destino.ush.gigglebyte.enums.FromScreen;
import jokes.gigglebyte.destino.ush.gigglebyte.enums.OpenScreen;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Post;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.PostType;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.NotificationFollowViewHolder;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.NotificationImagePostViewHolder;
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
      final PostTextViewHolder holder;
      convertView = mInflater.inflate(R.layout.post_text_item, parent, false);
      holder = new PostTextViewHolder();
      holder.userName = (TextView) convertView.findViewById(R.id.userName);
      holder.profileImage = (ImageView) convertView.findViewById(R.id.pic);
      holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
      holder.followButton = (ImageView) convertView.findViewById(R.id.followButton);

      holder.postInfo = (TextView) convertView.findViewById(R.id.postInfo);
      holder.tags =  (TextView) convertView.findViewById(R.id.tags);
      holder.postText = (TextView) convertView.findViewById(R.id.postText);
      holder.layout = (LinearLayout) convertView.findViewById(R.id.layout);
      holder.likeImage = (ImageView) convertView.findViewById(R.id.likeImage);
      holder.favoriteImage = (ImageView) convertView.findViewById(R.id.favoriteImage);
      holder.shareImage = (ImageView) convertView.findViewById(R.id.shareImage);
      holder.menuImage = (ImageView) convertView.findViewById(R.id.menuImage);
      convertView.setTag(holder);

      Post post = posts.get(position);
      holder.setUserData(activity, post.getUser(), OpenScreen.PROFILE);
      holder.setTextPostData(activity,convertView,post, fromScreen);

    } else if (posts.get(position).getType() == PostType.IMAGE_POST) {
      final PostImageViewHolder holder;
      convertView = mInflater.inflate(R.layout.post_image_item, parent, false);
      holder = new PostImageViewHolder();
      holder.userName = (TextView) convertView.findViewById(R.id.userName);
      holder.profileImage = (ImageView) convertView.findViewById(R.id.pic);
      holder.followButton = (ImageView) convertView.findViewById(R.id.followButton);
      holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);

      holder.title = (TextView) convertView.findViewById(R.id.title);
      holder.postImage = (ImageView) convertView.findViewById(R.id.postImage);
      holder.postInfo = (TextView) convertView.findViewById(R.id.postInfo);
      holder.tags =  (TextView) convertView.findViewById(R.id.tags);
      holder.likeImage = (ImageView) convertView.findViewById(R.id.likeImage);
      holder.favoriteImage = (ImageView) convertView.findViewById(R.id.favoriteImage);
      holder.layout = (LinearLayout) convertView.findViewById(R.id.layout);
      holder.imageProgressBar = (ProgressBar) convertView.findViewById(R.id.imageProgressBar);
      holder.shareImage = (ImageView) convertView.findViewById(R.id.shareImage);
      holder.menuImage = (ImageView) convertView.findViewById(R.id.menuImage);
      convertView.setTag(holder);

      Post post = posts.get(position);

      holder.setUserData(activity, post.getUser(), OpenScreen.PROFILE);
      holder.setImagePostData(activity, this, convertView, post, fromScreen);
    } else if (posts.get(position).getType() == PostType.INFO_POST) {
      final PostInfoViewHolder holder;
      convertView = mInflater.inflate(R.layout.post_info_item, parent, false);
      holder = new PostInfoViewHolder();

      holder.title = (TextView) convertView.findViewById(R.id.title);
      holder.postImage = (ImageView) convertView.findViewById(R.id.postImage);
      convertView.setTag(holder);

      Post post = posts.get(position);

      holder.setPostData(activity, post, fromScreen);
    }else if (posts.get(position).getType() == PostType.FOLLOWING_NOTIFICATION) {
      final NotificationFollowViewHolder holder;
      convertView = mInflater.inflate(R.layout.notification_follow_item, parent, false);
      holder = new NotificationFollowViewHolder();

      holder.profileImage = (ImageView) convertView.findViewById(R.id.pic);
      holder.userName = (TextView) convertView.findViewById(R.id.userName);
      holder.followButton = (ImageView) convertView.findViewById(R.id.followButton);
      holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
      holder.informationTextView = (TextView) convertView.findViewById(R.id.userName);
      holder.followeesName = (TextView) convertView.findViewById(R.id.followeesName);

      convertView.setTag(holder);

      Post post = posts.get(position);

      holder.setUserData(activity, post.getUser(), OpenScreen.PROFILE);
      holder.setData(activity, convertView, post, OpenScreen.PROFILE);
    }else if (posts.get(position).getType() == PostType.LIKE_TEXT_POST_NOTIFICATION || posts.get(position).getType() == PostType.COMMENT_TEXT_POST_NOTIFICATION) {
      final NotificationTextPostViewHolder holder;
      convertView = mInflater.inflate(R.layout.notification_on_text_post_item, parent, false);
      holder = new NotificationTextPostViewHolder();

      holder.profileImage = (ImageView) convertView.findViewById(R.id.pic);
      holder.userName = (TextView) convertView.findViewById(R.id.userName);
      holder.followButton = (ImageView) convertView.findViewById(R.id.followButton);
      holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
      holder.informationTextView = (TextView) convertView.findViewById(R.id.userName);

      convertView.setTag(holder);

      Post post = posts.get(position);

      holder.setUserData(activity, post.getUser(), OpenScreen.PROFILE);
      holder.setData(activity, convertView, post, OpenScreen.PROFILE);
    }else if (posts.get(position).getType() == PostType.LIKE_IMAGE_POST_NOTIFICATION || posts.get(position).getType() == PostType.COMMENT_IMAGE_POST_NOTIFICATION) {
      final NotificationImagePostViewHolder holder;
      convertView = mInflater.inflate(R.layout.notification_on_image_post_item, parent, false);
      holder = new NotificationImagePostViewHolder();

      holder.profileImage = (ImageView) convertView.findViewById(R.id.pic);
      holder.userName = (TextView) convertView.findViewById(R.id.userName);
      holder.followButton = (ImageView) convertView.findViewById(R.id.followButton);
      holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
      holder.informationTextView = (TextView) convertView.findViewById(R.id.userName);

      convertView.setTag(holder);

      Post post = posts.get(position);

      holder.setUserData(activity, post.getUser(), OpenScreen.PROFILE);
      holder.setData(activity, this, convertView, post, OpenScreen.PROFILE);
    }
    return convertView;
  }

}