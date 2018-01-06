package jokes.gigglebyte.destino.ush.gigglebyte.datahelpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Comment;
import jokes.gigglebyte.destino.ush.gigglebyte.server.ConnectToServer;

public class CommentHelper {
  private static List<Comment> allComments = new ArrayList<>();

  private static void likeComment(Context context, Comment comment) {
    //Store likes in shared Prefs
    comment.setUserLike(true);
    comment.setLikes(comment.getLikes() + 1);
    SharedPreferences sharedPreferences = context.getSharedPreferences("COMMENT_LIKES", Context.MODE_PRIVATE);
    SharedPreferences.Editor commentLikesEditor = sharedPreferences.edit();
    Set<String> likes = sharedPreferences.getStringSet("COMMENT_LIKES", new HashSet<String>());
    likes.add(String.valueOf(comment.getCommentId()));
    commentLikesEditor.putStringSet("COMMENT_LIKES", likes);
    commentLikesEditor.apply();
  }

  private static void unlikeComment(Context context, Comment comment) {
    //Store likes in shared Prefs
    comment.setUserLike(false);
    comment.setLikes(comment.getLikes() - 1);
    SharedPreferences sharedPreferences = context.getSharedPreferences("COMMENT_LIKES", Context.MODE_PRIVATE);
    SharedPreferences.Editor commentLikesEditor = sharedPreferences.edit();
    Set<String> likes = sharedPreferences.getStringSet("COMMENT_LIKES", new HashSet<String>());
    likes.remove(String.valueOf(comment.getCommentId()));
    commentLikesEditor.putStringSet("COMMENT_LIKES", likes);
    commentLikesEditor.apply();
  }

  public static void likeComment(final Activity activity, ImageView likeImage, TextView likes,
                                 final Comment comment) {
    likeImage.setImageResource(R.drawable.up_arrow);
    int likesInt = comment.getLikes() + 1;
    String likeAmount = String.valueOf(likesInt) + " " + (likesInt == 1 ? activity.getResources().getString(R.string.like) : activity.getResources().getString(R.string.likes));
    likes.setText(likeAmount);
    UIHelper.imageViewClickAnimation(likeImage);
    likeComment(activity, comment);

    Thread thread = new Thread() {
      @Override
      public void run() {
        ConnectToServer.commentLike(comment.getCommentId(), UserHelper.getUsersId(activity), comment.getUser().getId());
      }
    };
    thread.start();
  }

  public static void unlikeComment(final Activity activity, ImageView likeImage, TextView likes,
                                   final Comment comment) {
    likeImage.setImageResource(R.drawable.up_arrow_grey);

    int likesInt = comment.getLikes() - 1;
    String likeAmount = String.valueOf(likesInt) + " " + (likesInt == 1 ? activity.getResources().getString(R.string.like) : activity.getResources().getString(R.string.likes));
    likes.setText(likeAmount);
    UIHelper.imageViewClickAnimation(likeImage);
    unlikeComment(activity, comment);

    Thread thread = new Thread() {
      @Override
      public void run() {
        ConnectToServer.commentUnlike(comment.getCommentId(), UserHelper.getUsersId(activity));
      }
    };
    thread.start();
  }

  public static void addComment(Comment comment) {
    allComments.add(comment);
  }

}
