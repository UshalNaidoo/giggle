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
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Post;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.PostType;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.PostImageViewHolder;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.PostTextViewHolder;

public class PostListAdapter extends BaseAdapter {

  private List<Post> posts;
  private LayoutInflater mInflater;
  private Activity activity;

  public PostListAdapter(Activity activity, List<Post> results) {
    this.activity = activity;
    posts = results;
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

      Post post = posts.get(position);
      holder.setUserData(activity, post.getUser());
      holder.setTextPostData(activity,convertView,post);

    } else if (posts.get(position).getType() == PostType.IMAGE_POST) {
      final PostImageViewHolder holder;
      convertView = mInflater.inflate(R.layout.post_image_item, parent, false);
      holder = new PostImageViewHolder();
      holder.title = (TextView) convertView.findViewById(R.id.title);
      holder.userName = (TextView) convertView.findViewById(R.id.userName);
      holder.timeSince = (TextView) convertView.findViewById(R.id.timeSince);
      holder.tags =  (TextView) convertView.findViewById(R.id.tags);
      holder.postImage = (ImageView) convertView.findViewById(R.id.postImage);
      holder.likes = (TextView) convertView.findViewById(R.id.likes);
      holder.comments = (TextView) convertView.findViewById(R.id.comments);
      holder.likeImage = (ImageView) convertView.findViewById(R.id.likeImage);
      holder.favoriteImage = (ImageView) convertView.findViewById(R.id.favoriteImage);
      holder.layout = (LinearLayout) convertView.findViewById(R.id.layout);
      holder.profileImage = (ImageView) convertView.findViewById(R.id.pic);
      holder.imageProgressBar = (ProgressBar) convertView.findViewById(R.id.imageProgressBar);
      holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
      holder.shareImage = (ImageView) convertView.findViewById(R.id.shareImage);
      holder.menuImage = (ImageView) convertView.findViewById(R.id.menuImage);
      convertView.setTag(holder);

      Post post = posts.get(position);

      holder.setImagePostData(activity, this, convertView, post);
    }
    return convertView;
  }

}