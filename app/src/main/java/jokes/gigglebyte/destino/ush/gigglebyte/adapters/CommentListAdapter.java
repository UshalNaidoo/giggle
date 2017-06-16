package jokes.gigglebyte.destino.ush.gigglebyte.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
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
import jokes.gigglebyte.destino.ush.gigglebyte.activities.PosterProfileActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.CommentHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.UserHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.dialogs.OptionsCommentDialog;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Comment;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.CommentViewHolder;
import jokes.gigglebyte.destino.ush.gigglebyte.widgets.ToastWithImage;

import static android.view.View.OnClickListener;

public class CommentListAdapter extends BaseAdapter {

  private static List<Comment> comments;

  private LayoutInflater mInflater;
  private Activity activity;
  private ToastWithImage toastWithImage;

  public CommentListAdapter(Activity activity, List<Comment> results) {
    comments = results;
    mInflater = LayoutInflater.from(activity);
    toastWithImage = new ToastWithImage(activity);
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
    final CommentViewHolder holder;
    convertView = mInflater.inflate(R.layout.comment_item, parent, false);
    holder = new CommentViewHolder();
    holder.layout = (LinearLayout) convertView.findViewById(R.id.layout);
    holder.commentText = (TextView) convertView.findViewById(R.id.commentText);
    holder.likes = (TextView) convertView.findViewById(R.id.likes);
    convertView.setTag(holder);
    Comment comment = comments.get(position);
    holder.commentText.setText(comment.getCommentText());
    holder.likeImage = (ImageView) convertView.findViewById(R.id.likeImage);
    holder.profileImage = (ImageView) convertView.findViewById(R.id.profileImage);
    holder.commentLikes = comment.getLikes();
    holder.likes.setText(String.valueOf(holder.commentLikes));
    holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
    holder.profileImage.setVisibility(View.INVISIBLE);
    if (comment.isUserLike()) {
      holder.likeImage.setImageResource(R.drawable.star_like);
    } else {
      holder.likeImage.setImageResource(R.drawable.star_unlike);
    }
    holder.profileImage.setVisibility(View.INVISIBLE);
    final int userId = comment.getUserId();

    holder.commentText.setOnClickListener(doubleClickChecker(position, holder));
    holder.layout.setOnClickListener(doubleClickChecker(position, holder));

//    if (userId != UserHelper.getUserDetails(activity).getId()) {
      holder.commentText.setOnLongClickListener(longClickListener(comment));
      holder.layout.setOnLongClickListener(longClickListener(comment));
//    }
//    else {
//
//    }

    holder.likeImage.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        likeMethod(position, holder);
      }
    });

    holder.likes.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        likeMethod(position, holder);
      }
    });

    if (comment.getUserPicture() != null) {
      holder.progressBar.setVisibility(View.INVISIBLE);
      holder.profileImage.setImageBitmap(comment.getUserPicture());
    } else {
      // MY DEFAULT IMAGE
      comment.loadImage(this, activity, holder.progressBar, holder.profileImage);
    }

    holder.profileImage.setVisibility(View.VISIBLE);

    holder.profileImage.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (userId != UserHelper.getUserDetails(activity).getId()) {
          Intent myIntent = new Intent(activity, PosterProfileActivity.class);
          myIntent.putExtra("userId", userId);
          activity.startActivity(myIntent);
        }
      }
    });
    return convertView;
  }

  int i = 0;

  private OnClickListener doubleClickChecker(final int position,
                                             final CommentViewHolder holder) {
    return new OnClickListener() {
      @Override
      public void onClick(View v) {
        i++;
        Handler handler = new Handler();
        Runnable r = new Runnable() {
          @Override
          public void run() {
            i = 0;
          }
        };

        if (i == 1) {
          handler.postDelayed(r, 250);
        } else if (i == 2) {
          i = 0;
          likeMethod(position, holder);
        }
      }
    };
  }

  private View.OnLongClickListener longClickListener(final Comment comment) {
    return new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        OptionsCommentDialog optionsCommentDialog = new OptionsCommentDialog();
        optionsCommentDialog.setComment(comment);
        optionsCommentDialog.show(activity.getFragmentManager(), "");
        return true;
      }
    };
  }

  private void likeMethod(int position, CommentViewHolder holder) {
    if (comments.get(position).isUserLike()) {
      CommentHelper.unlikeComment(activity, holder.likeImage, holder.likes, comments.get(position));
    } else {
      toastWithImage.show(activity.getResources().getString(R.string.upvoted), R.drawable.star_like);
      CommentHelper.likeComment(activity, holder.likeImage, holder.likes, comments.get(position));
    }
  }
}