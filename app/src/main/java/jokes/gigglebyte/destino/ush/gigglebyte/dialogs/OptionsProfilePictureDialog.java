package jokes.gigglebyte.destino.ush.gigglebyte.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.ImageHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.UserHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.fragments.Fragment_Profile;

public class OptionsProfilePictureDialog extends DialogFragment {

  protected static final int CAMERA_REQUEST = 0;
  protected static final int GALLERY_PICTURE = 1;
  private Activity activity;

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    activity = getActivity();
    final Dialog dialog = new Dialog(activity);
    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    dialog.setContentView(R.layout.actiondialog_add_image_post);
    dialog.show();

    Button buttonGallery = (Button) dialog.findViewById(R.id.buttonGallery);
    buttonGallery.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent pictureActionIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pictureActionIntent, GALLERY_PICTURE);
      }
    });
    Button buttonPhoto = (Button) dialog.findViewById(R.id.buttonPhoto);
    buttonPhoto.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);
      }
    });

    return dialog;
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK && (requestCode == CAMERA_REQUEST
                                             || requestCode == GALLERY_PICTURE)) {
      Uri contentUri = data.getData();
      EditUserProfilePictureDialog editUserProfilePictureDialog = new EditUserProfilePictureDialog();
      editUserProfilePictureDialog.setUser(UserHelper.getUserDetails(activity));
      editUserProfilePictureDialog.setListener(new ImageHelper());
      editUserProfilePictureDialog.setCamera(requestCode == CAMERA_REQUEST);
      editUserProfilePictureDialog.setUri(contentUri);
      editUserProfilePictureDialog.show(activity.getFragmentManager(), "");
      dismiss();
      Fragment_Profile.refreshFragment();
    }
  }
}
