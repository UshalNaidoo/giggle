package jokes.gigglebyte.destino.ush.gigglebyte.dialogs;

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
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Comment;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Post;
import jokes.gigglebyte.destino.ush.gigglebyte.server.ConnectToServer;
import jokes.gigglebyte.destino.ush.gigglebyte.widgets.ToastWithImage;

public class OptionsConfirmDelete extends DialogFragment {

  private boolean isDeletePost;
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

    messageTextView.setText(isDeletePost ? activity.getResources().getString(R.string.confirm_post_delete) : activity.getResources().getString(R.string.confirm_comment_delete));

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
              if (isDeletePost) {
                ConnectToServer.deletePost(post.getPostId());
              } else {
                ConnectToServer.deleteComment(comment.getCommentId());
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
                    if (isDeletePost) {
                      message = activity.getResources().getString(R.string.post_deleted);
                      PostHelper.adjustPost(activity, null, PostHelper.PostAction.DELETE_POST , 0, post);
                    } else {
                      message = activity.getResources().getString(R.string.comment_deleted);
                      CommentActivity.reload();
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

  public void setDeletePost(boolean deletePost) {
    isDeletePost = deletePost;
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
