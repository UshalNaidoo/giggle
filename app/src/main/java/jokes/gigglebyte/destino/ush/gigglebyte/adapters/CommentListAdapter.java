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
        final PostTextViewHolder holder = new PostTextViewHolder();
        row = mInflater.inflate(R.layout.post_text_item, parent, false);
        PopulateViewHolderHelper.populatePostTextViewHolder(row, holder);
        row.setTag(holder);
        holder.setUserData(activity, post.getUser(), OpenScreen.PROFILE);
        holder.setTextPostData(activity, row, post, FromScreen.COMMENTS);
      }
      else if (post.getType() == PostType.IMAGE_POST) {
        final PostImageViewHolder holder = new PostImageViewHolder();
        row = mInflater.inflate(R.layout.post_image_item, parent, false);
        PopulateViewHolderHelper.populatePostImageViewHolder(row, holder);
        row.setTag(holder);
        holder.setUserData(activity, post.getUser(), OpenScreen.PROFILE);
        holder.setImagePostData(activity, this, row, post, FromScreen.COMMENTS);
      }
    }
    else {
      final CommentViewHolder holder = new CommentViewHolder();
      row = mInflater.inflate(R.layout.comment_item, parent, false);
      PopulateViewHolderHelper.populateCommentViewHolder(row, holder);
      row.setTag(holder);
      Comment comment = comments.get(position-1);
      holder.setUserData(activity, comment.getUser(), OpenScreen.PROFILE);
      holder.setData(activity, comment);
    }
    return row;
  }

}