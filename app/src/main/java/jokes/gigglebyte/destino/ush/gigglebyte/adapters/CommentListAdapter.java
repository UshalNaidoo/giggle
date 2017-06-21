package jokes.gigglebyte.destino.ush.gigglebyte.adapters;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Comment;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.CommentViewHolder;

public class CommentListAdapter extends BaseAdapter {

  private static List<Comment> comments;
  private LayoutInflater mInflater;
  private Activity activity;

  public CommentListAdapter(Activity activity, List<Comment> results) {
    comments = results;
    mInflater = LayoutInflater.from(activity);
    this.activity = activity;
  }

  public void updateAdapter(List<Comment> arrayList) {
    comments = arrayList;
    notifyDataSetChanged();
  }

  @Override
  public int getCount() {
    return comments.size();
  }

  @Override
  public Object getItem(int arg0) {
    return comments.get(arg0);
  }

  @Override
  public long getItemId(int arg0) {
    return arg0;
  }

  public View getView(final int position, View convertView, ViewGroup parent) {
    View row = convertView;
    final CommentViewHolder holder;

    if(row == null) {
      row = mInflater.inflate(R.layout.comment_item, parent, false);
      holder = new CommentViewHolder();
      holder.layout = (LinearLayout) row.findViewById(R.id.layout);
      holder.commentText = (TextView) row.findViewById(R.id.commentText);
      holder.likes = (TextView) row.findViewById(R.id.likes);
      holder.likeImage = (ImageView) row.findViewById(R.id.likeImage);
      holder.profileImage = (ImageView) row.findViewById(R.id.profileImage);
      holder.progressBar = (ProgressBar) row.findViewById(R.id.progressBar);
      row.setTag(holder);
    }
    else {
      holder = (CommentViewHolder) row.getTag();
    }
    holder.setData(activity, comments.get(position));
    return row;
  }

}