package jokes.gigglebyte.destino.ush.gigglebyte.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.activities.CommentActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.activities.MainActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.activities.PosterProfileActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.PostHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.UserHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.enums.FromScreen;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Post;

public class OptionsPostDialog extends DialogFragment {

  private Activity activity;
  private Post post;

  private FromScreen fromScreen;

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    activity = getActivity();
    final Dialog dialog = new Dialog(activity);
    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    dialog.setContentView(R.layout.actiondialog_option_posts);
    dialog.show();

    Button buttonViewProfile = (Button) dialog.findViewById(R.id.buttonProfileView);
    Button buttonViewComments = (Button) dialog.findViewById(R.id.buttonCommentsView);
    Button buttonFlag = (Button) dialog.findViewById(R.id.buttonFlag);
    Button buttonEdit = (Button) dialog.findViewById(R.id.buttonEdit);
    Button buttonDelete = (Button) dialog.findViewById(R.id.buttonDelete);

    if (getPost().getUser().getId() == UserHelper.getUsersId(activity)) {
      buttonDelete.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          OptionsConfirmDelete optionsConfirmDelete = new OptionsConfirmDelete();
          optionsConfirmDelete.setDeletePost(true);
          optionsConfirmDelete.setPost(getPost());
          optionsConfirmDelete.show(activity.getFragmentManager(), "");
          dismiss();
        }
      });

      buttonEdit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          OptionsEdit optionsEdit = new OptionsEdit();
          optionsEdit.setEditPost(true);
          optionsEdit.setPost(getPost());
          optionsEdit.show(activity.getFragmentManager(), "");
          dismiss();
        }
      });
      buttonViewProfile.setVisibility(View.GONE);
      buttonFlag.setVisibility(View.GONE);
    }
    else {
      buttonViewProfile.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (fromScreen != FromScreen.POSTER) {
            Intent myIntent = new Intent(activity, PosterProfileActivity.class);
            myIntent.putExtra("userId", getPost().getUser().getId());
            activity.startActivity(myIntent);
          }

          dismiss();
        }
      });

      buttonFlag.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          PostHelper.flagPost(activity, getPost());
          dismiss();
        }
      });

      buttonEdit.setVisibility(View.GONE);
      buttonDelete.setVisibility(View.GONE);
    }
    buttonViewComments.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (fromScreen != FromScreen.COMMENTS) {
          MainActivity.selectedPost = post;
          Intent myIntent = new Intent(activity, CommentActivity.class);
          myIntent.putExtra("postId", getPost().getPostId());
          myIntent.putExtra("posterId", getPost().getUser().getId());
          activity.startActivity(myIntent);
        }
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

  public void setFromAdapter(FromScreen fromScreen) {
    this.fromScreen = fromScreen;
  }

}
