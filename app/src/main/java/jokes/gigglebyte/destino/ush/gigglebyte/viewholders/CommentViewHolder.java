package jokes.gigglebyte.destino.ush.gigglebyte.viewholders;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.activities.PosterProfileActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.CommentHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.UserHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.dialogs.OptionsCommentDialog;
import jokes.gigglebyte.destino.ush.gigglebyte.enums.OpenScreen;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Comment;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;
import jokes.gigglebyte.destino.ush.gigglebyte.widgets.ToastWithImage;

public class CommentViewHolder extends UserProfilePictureHolder{

  public LinearLayout layout;
  public TextView userName;
  public TextView commentText;
  public TextView likes;
  public ImageView likeImage;

  private Comment comment;
  private Activity activity;

  public void setUserData(final Activity activity, final User user, OpenScreen screenToOpen) {
    setUserProfile(activity, user, screenToOpen);
    userName.setText(user.getName() == null || user.getName().isEmpty() ? activity.getResources().getString(R.string.unknown) : user.getName() );
  }

  public void setData(final Activity activity, Comment comment) {
    this.comment = comment;
    this.activity = activity;
    setUserProfile(activity, comment.getUser(), OpenScreen.PROFILE);
    commentText.setText(comment.getCommentText());
    likes.setText(String.valueOf(comment.getLikes()));
    final int userId = comment.getUser().getId();

    commentText.setOnClickListener(doubleClickChecker());
    layout.setOnClickListener(doubleClickChecker());

    //    if (userId != UserHelper.getUserDetails(activity).getId()) {
    commentText.setOnLongClickListener(longClickListener());
    layout.setOnLongClickListener(longClickListener());
    //    }
    //    else {
    //
    //    }

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

  private int i = 0;

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
        OptionsCommentDialog optionsCommentDialog = new OptionsCommentDialog();
        optionsCommentDialog.setComment(comment);
        optionsCommentDialog.show(activity.getFragmentManager(), "");
        return true;
      }
    };
  }

  private void likeMethod() {
    if (comment.isUserLike()) {
      CommentHelper.unlikeComment(activity, likeImage, likes, comment);
    } else {
      new ToastWithImage(activity).show(activity.getResources().getString(R.string.upvoted), R.drawable.star_like);
      CommentHelper.likeComment(activity, likeImage, likes, comment);
    }
  }

}
