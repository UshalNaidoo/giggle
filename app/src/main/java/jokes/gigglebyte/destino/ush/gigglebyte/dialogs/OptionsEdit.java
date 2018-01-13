package jokes.gigglebyte.destino.ush.gigglebyte.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.activities.CommentActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.PostHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Comment;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Post;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.PostType;
import jokes.gigglebyte.destino.ush.gigglebyte.server.ConnectToServer;
import jokes.gigglebyte.destino.ush.gigglebyte.widgets.ToastWithImage;

public class OptionsEdit extends DialogFragment {

  private boolean isEditPost;
  private Post post;
  private Comment comment;

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    final Activity activity = getActivity();
    final Dialog dialog = new Dialog(activity);
    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    dialog.setContentView(R.layout.actiondialog_edit);
    dialog.show();

    Button editButton = (Button) dialog.findViewById(R.id.edit);
    final EditText editText = (EditText) dialog.findViewById(R.id.editText1);

    String textToChange = isEditPost ? (post.getType() == PostType.IMAGE_POST
                                        ? post.getPostTitle()
                                        : post.getPostText()) : comment.getCommentText();
    editText.setText(textToChange);

    editButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Thread thread = new Thread(new Runnable() {
          @Override
          public void run() {
            try {
              if (isEditPost) {
                if (post.getType() == PostType.TEXT_POST) {
                  ConnectToServer.editTextPost(post.getPostId(), editText.getText().toString());
                } else {
                  ConnectToServer.editTitlePost(post.getPostId(), editText.getText().toString());
                }
              } else {
                ConnectToServer.editComment(comment.getCommentId(), editText.getText().toString());
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
                    if (isEditPost) {
                      post.setPostTitle(editText.getText().toString());
                      post.setPostText(editText.getText().toString());
                      message = activity.getResources().getString(R.string.changed);
                      PostHelper.adjustPost(activity, null, PostHelper.PostAction.EDIT_POST, 0, post);
                    } else {
                      message = activity.getResources().getString(R.string.changed);
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

  public void setEditPost(boolean editPost) {
    isEditPost = editPost;
  }

}
