package jokes.gigglebyte.destino.ush.gigglebyte.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.enums.FromScreen;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Post;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.PostType;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.PostImageViewHolder;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.PostInfoViewHolder;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.PostTextViewHolder;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.UserListViewHolder;

public class PosterProfileListAdapter extends BaseAdapter {

  private static List<Post> posts;
  private static User poster;
  private LayoutInflater mInflater;
  private Activity activity;
  private FromScreen fromScreen;

  public PosterProfileListAdapter(Activity activity, List<Post> results, User user, FromScreen fromScreen) {
    posts = results;
    poster = user;
    this.fromScreen = fromScreen;
    mInflater = LayoutInflater.from(activity);
    this.activity = activity;
  }

  @Override
  public int getCount() {
    return posts.size() + 1;
  }

  @Override
  public Object getItem(int arg0) {
    if (arg0 == 0) {
      return poster;
    }
    return posts.get(arg0);
  }

  @Override
  public long getItemId(int arg0) {
    return arg0;
  }

  public View getView(final int position, View convertView, ViewGroup parent) {
    if (position == 0) {
      UserListViewHolder holder;
      convertView = mInflater.inflate(R.layout.poster_header, parent, false);
      holder = new UserListViewHolder();
      holder.description = (TextView) convertView.findViewById(R.id.description);
      holder.profileImage = (ImageView) convertView.findViewById(R.id.profileImage);
      holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
      holder.viewFollowers = (Button) convertView.findViewById(R.id.button_followers);
      holder.viewFollowing = (Button) convertView.findViewById(R.id.button_following);
      convertView.setTag(holder);
      holder.setUserData(activity, poster);
    } else {
      final int pos = position - 1;
      final Post post = posts.get(pos);
      if (post.getType() == PostType.TEXT_POST) {
        final PostTextViewHolder holder;
        convertView = mInflater.inflate(R.layout.profile_text_item, parent, false);
        holder = new PostTextViewHolder();
        holder.timeSince = (TextView) convertView.findViewById(R.id.timeSince);
        holder.postInfo = (TextView) convertView.findViewById(R.id.postInfo);
        holder.tags =  (TextView) convertView.findViewById(R.id.tags);
        holder.postText = (TextView) convertView.findViewById(R.id.postText);
        holder.layout = (LinearLayout) convertView.findViewById(R.id.layout);
        holder.likeImage = (ImageView) convertView.findViewById(R.id.likeImage);
        holder.favoriteImage = (ImageView) convertView.findViewById(R.id.favoriteImage);
        holder.shareImage = (ImageView) convertView.findViewById(R.id.shareImage);
        holder.menuImage = (ImageView) convertView.findViewById(R.id.menuImage);
        convertView.setTag(holder);
        holder.setTextPostData(activity, convertView, post, fromScreen);
      } else if (post.getType() == PostType.IMAGE_POST) {
        final PostImageViewHolder holder;
        convertView = mInflater.inflate(R.layout.profile_image_item, parent, false);
        holder = new PostImageViewHolder();
        holder.title = (TextView) convertView.findViewById(R.id.title);
        holder.timeSince = (TextView) convertView.findViewById(R.id.timeSince);
        holder.postInfo = (TextView) convertView.findViewById(R.id.postInfo);
        holder.postImage = (ImageView) convertView.findViewById(R.id.postImage);
        holder.tags =  (TextView) convertView.findViewById(R.id.tags);
        holder.likeImage = (ImageView) convertView.findViewById(R.id.likeImage);
        holder.favoriteImage = (ImageView) convertView.findViewById(R.id.favoriteImage);
        holder.layout = (LinearLayout) convertView.findViewById(R.id.layout);
        holder.imageProgressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
        holder.shareImage = (ImageView) convertView.findViewById(R.id.shareImage);
        convertView.setTag(holder);
        holder.setImagePostData(activity, this, convertView, post, fromScreen);
      } else if (post.getType() == PostType.INFO_POST) {
        final PostInfoViewHolder holder;
        convertView = mInflater.inflate(R.layout.post_info_item, parent, false);
        holder = new PostInfoViewHolder();

        holder.title = (TextView) convertView.findViewById(R.id.title);
        holder.postImage = (ImageView) convertView.findViewById(R.id.postImage);
        convertView.setTag(holder);
        holder.setPostData(activity, post, fromScreen);
      }
    }
    return convertView;
  }

}