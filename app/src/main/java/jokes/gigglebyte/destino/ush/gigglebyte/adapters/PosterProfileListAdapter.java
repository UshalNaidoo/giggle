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
import jokes.gigglebyte.destino.ush.gigglebyte.dialogs.OptionsPostDialog;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Post;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.PostType;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.PostImageViewHolder;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.PostTextViewHolder;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.UserListViewHolder;

public class PosterProfileListAdapter extends BaseAdapter {

  private static List<Post> posts;
  private static User poster;
  private LayoutInflater mInflater;
  private Activity activity;
  private OptionsPostDialog.FromScreen fromScreen;

  public PosterProfileListAdapter(Activity activity, List<Post> results, User user, OptionsPostDialog.FromScreen fromScreen) {
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
        holder.tags =  (TextView) convertView.findViewById(R.id.tags);
        holder.postText = (TextView) convertView.findViewById(R.id.postText);
        holder.likes = (TextView) convertView.findViewById(R.id.likes);
        holder.layout = (LinearLayout) convertView.findViewById(R.id.layout);
        holder.comments = (TextView) convertView.findViewById(R.id.comments);
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
        holder.postImage = (ImageView) convertView.findViewById(R.id.postImage);
        holder.tags =  (TextView) convertView.findViewById(R.id.tags);
        holder.likes = (TextView) convertView.findViewById(R.id.likes);
        holder.comments = (TextView) convertView.findViewById(R.id.comments);
        holder.likeImage = (ImageView) convertView.findViewById(R.id.likeImage);
        holder.favoriteImage = (ImageView) convertView.findViewById(R.id.favoriteImage);
        holder.layout = (LinearLayout) convertView.findViewById(R.id.layout);
        holder.imageProgressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
        holder.shareImage = (ImageView) convertView.findViewById(R.id.shareImage);
        convertView.setTag(holder);
        holder.setImagePostData(activity, this, convertView, post, fromScreen);
       }
    }
    return convertView;
  }

}