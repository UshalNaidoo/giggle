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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.activities.PosterProfileActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.CommentHelper;
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

    TextView commentText = (TextView) dialog.findViewById(R.id.commentText);
    commentText.setText(getComment().getCommentText());
    ImageView profileImage = (ImageView) dialog.findViewById(R.id.profileImage);
    profileImage.setImageBitmap(getComment().getUserPicture());

    Button buttonViewProfile = (Button) dialog.findViewById(R.id.buttonProfileView);
    buttonViewProfile.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent myIntent = new Intent(activity, PosterProfileActivity.class);
        myIntent.putExtra("userId", getComment().getUserId());
        activity.startActivity(myIntent);
        dismiss();
      }
    });

    Button buttonFlag = (Button) dialog.findViewById(R.id.buttonFlag);
    buttonFlag.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        CommentHelper.flagComment(activity, getComment());
        dismiss();
      }
    });

    ImageButton closeButton = (ImageButton) dialog.findViewById(R.id.buttonClose);
    closeButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dismiss();
      }
    });
    return dialog;
  }

  public Comment getComment() {
    return comment;
  }

  public void setComment(Comment comment) {
    this.comment = comment;
  }
}
