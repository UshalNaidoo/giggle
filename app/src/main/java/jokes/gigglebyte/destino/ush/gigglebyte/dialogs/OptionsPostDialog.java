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
import jokes.gigglebyte.destino.ush.gigglebyte.activities.CommentActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.activities.PosterProfileActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.PostHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Post;

public class OptionsPostDialog extends DialogFragment {

  private Activity activity;
  private Post post;

  private enum FromScreen {
    POSTER,
    NEW,
    HOT,
    BYTE
  }

  private TextView likes;
  private ImageView likeImage;

  private FromScreen fromScreen;

  private int position;

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    activity = getActivity();
    final Dialog dialog = new Dialog(activity);
    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    dialog.setContentView(R.layout.actiondialog_option_posts);
    dialog.show();

    TextView postText = (TextView) dialog.findViewById(R.id.postText);
    postText.setText(getPost().getPostText().isEmpty() ? getPost().getUser().getName() : getPost().getPostText());
    ImageView profileImage = (ImageView) dialog.findViewById(R.id.profileImage);
    profileImage.setImageBitmap(getPost().getUser().getProfile_pic());

    Button buttonViewProfile = (Button) dialog.findViewById(R.id.buttonProfileView);
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

    Button buttonCommentsView = (Button) dialog.findViewById(R.id.buttonCommentsView);
    buttonCommentsView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (fromScreen != FromScreen.BYTE) {
          Intent myIntent = new Intent(activity, CommentActivity.class);
          myIntent.putExtra("postId", getPost().getPostId());
          myIntent.putExtra("posterId", getPost().getUser().getId());
          myIntent.putExtra("position", getPosition());
          activity.startActivity(myIntent);
        }
        dismiss();
      }
    });

    Button buttonFlag = (Button) dialog.findViewById(R.id.buttonFlag);
    buttonFlag.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        PostHelper.flagPost(activity, getPost());
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

  public Post getPost() {
    return post;
  }

  public void setPost(Post post) {
    this.post = post;
  }

  public void setFromAdapter(String fromAdapter) {
    switch (fromAdapter) {
      case "poster":
        fromScreen = FromScreen.POSTER;
        break;
      case "new":
        fromScreen = FromScreen.NEW;
        break;
      case "hot":
        fromScreen = FromScreen.HOT;
        break;
      case "byte":
        fromScreen = FromScreen.BYTE;
        break;
    }
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  public TextView getLikes() {
    return likes;
  }

  public void setLikes(TextView likes) {
    this.likes = likes;
  }

  public ImageView getLikeImage() {
    return likeImage;
  }

  public void setLikeImage(ImageView likeImage) {
    this.likeImage = likeImage;
  }

}
