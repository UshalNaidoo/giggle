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

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.activities.PosterProfileActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.CommentHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.FollowHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.UserHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.dialogs.OptionsCommentDialog;
import jokes.gigglebyte.destino.ush.gigglebyte.enums.OpenScreen;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Comment;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;
import jokes.gigglebyte.destino.ush.gigglebyte.widgets.ToastWithImage;

public class CommentViewHolder extends UserProfilePictureHolder {

  public LinearLayout layout;
  public TextView userName;
  public TextView commentText;
  public TextView likes;
  public ImageView likeImage;
  public ImageView menuImage;

  private Comment comment;
  private Activity activity;
  private int i = 0;

  public void setUserData(final Activity activity, final User user, OpenScreen screenToOpen) {
    setUserProfile(activity, user, screenToOpen);
    userName.setText(user.getName() == null || user.getName().isEmpty() ? activity.getResources()
        .getString(R.string.unknown) : user.getName());
  }

  public void setData(final Activity activity, Comment comment) {
    this.comment = comment;
    this.activity = activity;
    setUserProfile(activity, comment.getUser(), OpenScreen.PROFILE);

    commentText.setMovementMethod(LinkMovementMethod.getInstance());
    commentText.setText(comment.getCommentText(), TextView.BufferType.SPANNABLE);

    String[] mentions = comment.getCommentText().trim().split(" ");
    int start = 0;
    for (String mention : mentions) {
      if (mention.startsWith("@") && FollowHelper.getFollowerByName(mention.substring(1)) != null) {
        Spannable spans = (Spannable) commentText.getText();
        ClickableSpan clickSpan = getClickableSpan(FollowHelper.getFollowerByName(mention.substring(1)));
        spans.setSpan(clickSpan, start,
                      comment.getCommentText().trim().length() > start + mention.length() + 1
                      ? start + mention.length() + 1
                      : comment.getCommentText()
                          .trim()
                          .length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      }
      start += mention.length() + 1;
    }

    String likeAmount = String.valueOf(comment.getLikes()) + " " + (comment.getLikes() == 1
                                                                    ? activity.getResources()
                                                                        .getString(R.string.like)
                                                                    : activity.getResources()
                                                                        .getString(R.string.likes));
    likes.setText(likeAmount);
    final int userId = comment.getUser().getId();

    commentText.setOnClickListener(doubleClickChecker());
    layout.setOnClickListener(doubleClickChecker());

    commentText.setOnLongClickListener(longClickListener());
    layout.setOnLongClickListener(longClickListener());

    if (menuImage != null) {
      menuImage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          commentOptions();
        }
      });
    }

    if (comment.isUserLike()) {
      likeImage.setImageResource(R.drawable.up_arrow);
    } else {
      likeImage.setImageResource(R.drawable.up_arrow_grey);
    }

    likeImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        likeMethod();
      }
    });

    likes.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        likeMethod();
      }
    });

    profileImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (userId != UserHelper.getUsersId(activity)) {
          Intent myIntent = new Intent(activity, PosterProfileActivity.class);
          myIntent.putExtra("userId", userId);
          activity.startActivity(myIntent);
        }
      }
    });
  }

  private View.OnClickListener doubleClickChecker() {
    return new View.OnClickListener() {
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
          likeMethod();
        }
      }
    };
  }

  private View.OnLongClickListener longClickListener() {
    return new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        commentOptions();
        return true;
      }
    };
  }

  private void commentOptions() {
    OptionsCommentDialog optionsCommentDialog = new OptionsCommentDialog();
    optionsCommentDialog.setComment(comment);
    optionsCommentDialog.show(activity.getFragmentManager(), "");
  }

  private void likeMethod() {
    if (comment.isUserLike()) {
      CommentHelper.unlikeComment(activity, likeImage, likes, comment);
    } else {
      new ToastWithImage(activity).show(activity.getResources()
                                            .getString(R.string.upvoted), R.drawable.up_arrow);
      CommentHelper.likeComment(activity, likeImage, likes, comment);
    }
  }

  private ClickableSpan getClickableSpan(final User user) {
    return new ClickableSpan() {
      @Override
      public void onClick(View widget) {
        Intent myIntent = new Intent(activity, PosterProfileActivity.class);
        myIntent.putExtra("userId", user.getId());
        activity.startActivity(myIntent);
      }

      public void updateDrawState(TextPaint ds) {
        ds.setColor(ds.linkColor);
        ds.setUnderlineText(false);
      }
    };
  }

}
