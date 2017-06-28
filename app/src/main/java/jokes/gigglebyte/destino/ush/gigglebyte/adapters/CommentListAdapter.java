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
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Comment;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Post;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.PostType;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.CommentViewHolder;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.PostImageViewHolder;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.PostTextViewHolder;

public class CommentListAdapter extends BaseAdapter {

  private static List<Comment> comments;
  private LayoutInflater mInflater;
  private Activity activity;
  private Post post;

  public CommentListAdapter(Activity activity, List<Comment> results, Post post) {
    comments = results;
    this.post = post;
    mInflater = LayoutInflater.from(activity);
    this.activity = activity;
  }

  public void updateAdapter(List<Comment> arrayList) {
    comments = arrayList;
    notifyDataSetChanged();
  }

  @Override
  public int getCount() {
    return comments.size()+1;
  }

  @Override
  public Object getItem(int arg0) {
    if (arg0 == 0) {
      return post;
    }
    return comments.get(arg0);
  }

  @Override
  public long getItemId(int arg0) {
    return arg0;
  }

  public View getView(final int position, View convertView, ViewGroup parent) {
    View row = convertView;
    if (position == 0) {
      if (post.getType() == PostType.TEXT_POST) {
        final PostTextViewHolder holder;
        row = mInflater.inflate(R.layout.post_text_item, parent, false);
        holder = new PostTextViewHolder();
        holder.userName = (TextView) row.findViewById(R.id.userName);
        holder.profileImage = (ImageView) row.findViewById(R.id.pic);
        holder.progressBar = (ProgressBar) row.findViewById(R.id.progressBar);
        holder.followButton = (ImageView) row.findViewById(R.id.followButton);

        holder.tags =  (TextView) row.findViewById(R.id.tags);
        holder.postText = (TextView) row.findViewById(R.id.postText);
        holder.postInfo = (TextView) row.findViewById(R.id.postInfo);
        holder.layout = (LinearLayout) row.findViewById(R.id.layout);
        holder.likeImage = (ImageView) row.findViewById(R.id.likeImage);
        holder.favoriteImage = (ImageView) row.findViewById(R.id.favoriteImage);
        holder.shareImage = (ImageView) row.findViewById(R.id.shareImage);
        holder.menuImage = (ImageView) row.findViewById(R.id.menuImage);
        row.setTag(holder);

        holder.setUserData(activity, post.getUser(), OpenScreen.PROFILE);
        holder.setTextPostData(activity, row, post, FromScreen.COMMENTS);

      } else if (post.getType() == PostType.IMAGE_POST) {
        final PostImageViewHolder holder;
        row = mInflater.inflate(R.layout.post_image_item, parent, false);
        holder = new PostImageViewHolder();
        holder.userName = (TextView) row.findViewById(R.id.userName);
        holder.profileImage = (ImageView) row.findViewById(R.id.pic);
        holder.followButton = (ImageView) row.findViewById(R.id.followButton);
        holder.progressBar = (ProgressBar) row.findViewById(R.id.progressBar);

        holder.title = (TextView) row.findViewById(R.id.title);
        holder.postImage = (ImageView) row.findViewById(R.id.postImage);
        holder.postInfo = (TextView) row.findViewById(R.id.postInfo);
        holder.tags =  (TextView) row.findViewById(R.id.tags);
        holder.likeImage = (ImageView) row.findViewById(R.id.likeImage);
        holder.favoriteImage = (ImageView) row.findViewById(R.id.favoriteImage);
        holder.layout = (LinearLayout) row.findViewById(R.id.layout);
        holder.imageProgressBar = (ProgressBar) row.findViewById(R.id.imageProgressBar);
        holder.shareImage = (ImageView) row.findViewById(R.id.shareImage);
        holder.menuImage = (ImageView) row.findViewById(R.id.menuImage);
        row.setTag(holder);

        holder.setUserData(activity, post.getUser(), OpenScreen.PROFILE);
        holder.setImagePostData(activity, this, row, post, FromScreen.COMMENTS);
      }
    }
    else {
      final CommentViewHolder holder;
      row = mInflater.inflate(R.layout.comment_item, parent, false);
      holder = new CommentViewHolder();
      holder.layout = (LinearLayout) row.findViewById(R.id.layout);
      holder.commentText = (TextView) row.findViewById(R.id.commentText);
      holder.userName = (TextView) row.findViewById(R.id.userName);
      holder.likes = (TextView) row.findViewById(R.id.likes);
      holder.likeImage = (ImageView) row.findViewById(R.id.likeImage);
      holder.profileImage = (ImageView) row.findViewById(R.id.profileImage);
      holder.progressBar = (ProgressBar) row.findViewById(R.id.progressBar);
      row.setTag(holder);

      Comment comment = comments.get(position-1);
      holder.setUserData(activity, comment.getUser(), OpenScreen.PROFILE);
      holder.setData(activity, comment);
    }
    return row;
  }

}