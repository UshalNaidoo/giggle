package jokes.gigglebyte.destino.ush.gigglebyte.dialogs;

import java.util.Set;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.activities.CommentActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.PostHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.SharedPrefHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.UserHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Comment;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Post;
import jokes.gigglebyte.destino.ush.gigglebyte.server.ConnectToServer;
import jokes.gigglebyte.destino.ush.gigglebyte.widgets.ToastWithImage;

public class OptionsConfirmFlag extends DialogFragment {

  private boolean isFlagPost;
  private Post post;
  private Comment comment;

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    final Activity activity = getActivity();
    final Dialog dialog = new Dialog(activity);
    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    dialog.setContentView(R.layout.actiondialog_option_delete);
    dialog.show();

    Button buttonYes = (Button) dialog.findViewById(R.id.buttonYes);
    Button buttonNo = (Button) dialog.findViewById(R.id.buttonNo);
    TextView messageTextView = (TextView) dialog.findViewById(R.id.messageTextView);

    messageTextView.setText(isFlagPost ? activity.getResources().getString(R.string.confirm_post_flag) : activity.getResources().getString(R.string.confirm_comment_flag));

    buttonNo.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        dismiss();
      }
    });

    buttonYes.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Thread thread = new Thread(new Runnable() {
          @Override
          public void run() {
            try  {
              if (isFlagPost) {
                ConnectToServer.flagPost(UserHelper.getUsersId(activity), post.getPostId());
              }
              else {
                ConnectToServer.flagComment(UserHelper.getUsersId(activity), comment.getCommentId());
              }
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        });

        thread.start();

        Thread uiThread = new Thread() {
          @Override
          public void run() {
            new Thread() {
              public void run() {
                activity.runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                    final String message;
                    if (isFlagPost) {
                      message = activity.getResources().getString(R.string.post_flagged);
                      Set<String> flag_posts = SharedPrefHelper.getPostFlags(activity);
                      flag_posts.add(String.valueOf(post.getPostId()));
                      SharedPrefHelper.savePostFlags(activity, flag_posts);
                    } else {
                      message = activity.getResources().getString(R.string.comment_flagged);
                      Set<String> commentFlags = SharedPrefHelper.getCommentFlags(activity);
                      commentFlags.add(String.valueOf(comment.getCommentId()));
                      SharedPrefHelper.saveCommentFlags(activity, commentFlags);
                    }
                    new ToastWithImage(activity).show(message, null);
                  }
                });
              }
            }.start();
          }
        };
        uiThread.start();
        dismiss();
      }
    });

    return dialog;
  }

  public void setFlagPost(boolean deletePost) {
    isFlagPost = deletePost;
  }

  public Post getPost() {
    return post;
  }

  public void setPost(Post post) {
    this.post = post;
  }

  public Comment getComment() {
    return comment;
  }

  public void setComment(Comment comment) {
    this.comment = comment;
  }

}
