package jokes.gigglebyte.destino.ush.gigglebyte.dialogs;

import java.util.Set;

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
import jokes.gigglebyte.destino.ush.gigglebyte.activities.PosterProfileActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.CommentHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.SharedPrefHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.UserHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Comment;

public class OptionsCommentDialog extends DialogFragment {

  private Activity activity;
  private Comment comment;

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    activity = getActivity();
    final Dialog dialog = new Dialog(activity);
    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    dialog.setContentView(R.layout.actiondialog_option_comments);
    dialog.show();

    Button buttonViewProfile = (Button) dialog.findViewById(R.id.buttonProfileView);
    Button buttonFlag = (Button) dialog.findViewById(R.id.buttonFlag);
    Button buttonEdit = (Button) dialog.findViewById(R.id.buttonEdit);
    Button buttonDelete = (Button) dialog.findViewById(R.id.buttonDelete);

    if (getComment().getUser().getId() == UserHelper.getUsersId(activity)) {
      buttonEdit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          OptionsEdit optionsEdit = new OptionsEdit();
          optionsEdit.setEditPost(false);
          optionsEdit.setComment(getComment());
          optionsEdit.show(activity.getFragmentManager(), "");
          dismiss();
        }
      });

      buttonDelete.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          OptionsConfirmDelete optionsConfirmDelete = new OptionsConfirmDelete();
          optionsConfirmDelete.setDeletePost(false);
          optionsConfirmDelete.setComment(getComment());
          optionsConfirmDelete.show(activity.getFragmentManager(), "");
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
          Intent myIntent = new Intent(activity, PosterProfileActivity.class);
          myIntent.putExtra("userId", getComment().getUser().getId());
          activity.startActivity(myIntent);
          dismiss();
        }
      });

      if (!SharedPrefHelper.getCommentFlags(activity).contains(String.valueOf(comment.getCommentId()))) {
        buttonFlag.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            OptionsConfirmFlag optionsConfirmFlag = new OptionsConfirmFlag();
            optionsConfirmFlag.setFlagPost(false);
            optionsConfirmFlag.setComment(getComment());
            optionsConfirmFlag.show(activity.getFragmentManager(), "");
            dismiss();
          }
        });
      }
      else {
        buttonFlag.setVisibility(View.GONE);
      }

      buttonEdit.setVisibility(View.GONE);
      buttonDelete.setVisibility(View.GONE);
    }

    return dialog;
  }

  public Comment getComment() {
    return comment;
  }

  public void setComment(Comment comment) {
    this.comment = comment;
  }
}
