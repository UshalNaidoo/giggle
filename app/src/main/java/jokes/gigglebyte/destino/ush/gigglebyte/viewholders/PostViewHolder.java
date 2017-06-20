package jokes.gigglebyte.destino.ush.gigglebyte.viewholders;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.activities.CommentActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.activities.MainActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.activities.TagActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.PostHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.UserHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.dialogs.OptionsPostDialog;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Post;
import jokes.gigglebyte.destino.ush.gigglebyte.widgets.ToastWithImage;

public class PostViewHolder extends UserGridViewHolder {

  public LinearLayout layout;
  public TextView timeSince;
  public TextView likes;
  public TextView comments;
  public TextView tags;
  public ImageView likeImage;
  public ImageView favoriteImage;
  public ImageView shareImage;
  public ImageView menuImage;
  private Post post;
  private Activity activity;
  private boolean isDoubleClick = false;
  private OptionsPostDialog.FromScreen fromScreen;

  void setPostData(final Activity activity, View convertView, final Post post, OptionsPostDialog.FromScreen from) {
    this.activity = activity;
    this.post = post;
    this.fromScreen = from;
    timeSince.setText(post.getTimeSincePost());
    comments.setText(String.valueOf(post.getCommentCount()));
    likes.setText(String.valueOf(post.getLikes()));
    final View finalConvertView = convertView;
    shareImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        PostHelper.shareBitmap(activity, finalConvertView.findViewById(R.id.layoutView));
      }
    });

    if (post.getTags() != null) {
      String definition = "";
      for (String tag : post.getTags()) {
        definition += " #" + tag;
      }

      tags.setMovementMethod(LinkMovementMethod.getInstance());
      tags.setText(definition, TextView.BufferType.SPANNABLE);
      tags.setTextColor(activity.getResources().getColor(R.color.app_primary_dark));

      Spannable spans = (Spannable) tags.getText();
      Integer[] indices = getIndices(tags.getText().toString(), ' ');
      int start = 0;
      int end;

      for (int i = 0; i <= indices.length; i++) {
        ClickableSpan clickSpan = getClickableSpan();
        end = (i < indices.length ? indices[i] : spans.length());
        spans.setSpan(clickSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        start = end + 1;
      }
    } else {
      tags.setVisibility(View.GONE);
    }

    if (post.isUserLike()) {
      likeImage.setImageResource(R.drawable.star_like);
    } else {
      likeImage.setImageResource(R.drawable.star_unlike);
    }

    if (post.isUserFavorite()) {
      favoriteImage.setImageResource(R.drawable.heart_like);
    } else {
      favoriteImage.setImageResource(R.drawable.heart_unlike);
    }

    layout.setOnClickListener(doubleClickListener());

    layout.setOnLongClickListener(longClickListener(post.getUser().getId()));


    likeImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        likePost();
      }
    });

    likes.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        likePost();
      }
    });

    favoriteImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        favoritePost();
      }
    });

    if (menuImage != null ) {
      menuImage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          postOptions();
        }
      });
    }

  }

  View.OnLongClickListener longClickListener(final int userId) {
    return new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        if (userId != UserHelper.getUserDetails(activity).getId()) {
          postOptions();
        }
        return true;
      }
    };
  }

  private void postOptions() {
    OptionsPostDialog optionsPostDialog = new OptionsPostDialog();
    optionsPostDialog.setPost(post);
    optionsPostDialog.setFromAdapter(this.fromScreen);
    optionsPostDialog.setLikeImage(likeImage);
    optionsPostDialog.setLikes(likes);
    optionsPostDialog.show(activity.getFragmentManager(), "");
  }

  View.OnClickListener doubleClickListener() {
    return new View.OnClickListener() {
      int i = 0;

      @Override
      public void onClick(View v) {
        i++;
        Handler handler = new Handler();
        Runnable singleClick = new Runnable() {
          @Override
          public void run() {
            if (!isDoubleClick) {
              Intent myIntent = new Intent(activity, CommentActivity.class);
              MainActivity.selectedPost = post;

              myIntent.putExtra("postId", post.getPostId());
              myIntent.putExtra("posterId", post.getUser().getId());
              activity.startActivity(myIntent);
            }
            isDoubleClick = false;
            i = 0;
          }
        };

        if (i == 1) {
          handler.postDelayed(singleClick, 200);
        } else if (i == 2) {
          i = 0;
          likePost();
          isDoubleClick = true;
        }
      }
    };
  }

  private void likePost() {
    if (post.isUserLike()) {
      likes.setText(String.valueOf(post.getLikes() - 1));
      PostHelper.adjustPost(activity, likeImage, PostHelper.PostAction.UNLIKE_POST, post.getLikes() - 1, post);
    } else {
      new ToastWithImage(activity).show(activity.getResources().getString(R.string.upvoted), R.drawable.star_like);
      likes.setText(String.valueOf(post.getLikes() + 1));
      PostHelper.adjustPost(activity, likeImage, PostHelper.PostAction.LIKE_POST, post.getLikes() + 1, post);
    }
  }

  private void favoritePost() {
    if (post.isUserFavorite()) {
      PostHelper.adjustPost(activity, favoriteImage, PostHelper.PostAction.UNFAVORITE_POST, 0, post);
    } else {
      new ToastWithImage(activity).show(activity.getResources().getString(R.string.favourites), R.drawable.heart_like);
      PostHelper.adjustPost(activity, favoriteImage, PostHelper.PostAction.FAVORITE_POST, 0, post);
    }
  }

  private ClickableSpan getClickableSpan() {
    return new ClickableSpan() {
      @Override
      public void onClick(View widget) {
        TextView textView = (TextView) widget;
        if (!textView.getText().toString().isEmpty()) {
          String s = textView.getText().subSequence(textView.getSelectionStart(),
                                                    textView.getSelectionEnd()).toString();
          Intent intent = new Intent(activity, TagActivity.class);
          intent.putExtra("tag", s);
          activity.startActivity(intent);
        }
      }

      public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
      }
    };
  }

  private static Integer[] getIndices(String s, char c) {
    int pos = s.indexOf(c, 0);
    List<Integer> indices = new ArrayList<>();
    while (pos != -1) {
      indices.add(pos);
      pos = s.indexOf(c, pos + 1);
    }
    return indices.toArray(new Integer[indices.size()]);
  }

}
