package jokes.gigglebyte.destino.ush.gigglebyte.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.interfaces.onSubmitListener;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;
import jokes.gigglebyte.destino.ush.gigglebyte.server.ConnectToServer;

public class EditUserProfilePictureDialog extends DialogFragment {

  private Activity activity;
  private User user;
  private onSubmitListener listener;
  private Uri uri;
  private boolean isCamera;
  private Bitmap bitmap;

  private static Bitmap rotateImage(Bitmap source, int angle) {
    Matrix matrix = new Matrix();
    matrix.postRotate(angle);
    return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
  }

  private static Bitmap cropToSquare(Bitmap bitmap) {
    int width = bitmap.getWidth();
    int height = bitmap.getHeight();
    int newWidth = (height > width) ? width : height;
    int newHeight = (height > width) ? height - (height - width) : height;
    int cropW = (width - height) / 2;
    cropW = (cropW < 0) ? 0 : cropW;
    int cropH = (height - width) / 2;
    cropH = (cropH < 0) ? 0 : cropH;
    return Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    activity = getActivity();
    final Dialog dialog = new Dialog(activity);
    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    dialog.setContentView(R.layout.actiondialog_edit_profile_picture);
    dialog.show();

    final ImageView image = (ImageView) dialog.findViewById(R.id.image);

    try {
      bitmap = cropToSquare(MediaStore.Images.Media.getBitmap(activity.getContentResolver(), getUri()));
      if (isCamera()) {
        bitmap = rotateImage(bitmap, 90);
      }
      image.setImageBitmap(bitmap);
    } catch (IOException e) {
      image.setImageURI(getUri());
      e.printStackTrace();
    }

    Button sendButton = (Button) dialog.findViewById(R.id.button1);
    sendButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        final User user = getUser();
        user.setProfile_pic(bitmap);
        getListener().setOnSubmitListener(activity, user);

        Thread t = new Thread(new Runnable() {

          @Override
          public void run() {
            try {
              ByteArrayOutputStream stream = new ByteArrayOutputStream();
              bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to which format you want.
              byte[] byte_arr = stream.toByteArray();
              String image_str = Base64.encodeToString(byte_arr, Base64.DEFAULT);
              ConnectToServer.changeUserProfilePicture(user.getId(), image_str);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        });
        t.start();
        dismiss();
      }
    });
    return dialog;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public onSubmitListener getListener() {
    return listener;
  }

  public void setListener(onSubmitListener listener) {
    this.listener = listener;
  }

  public Uri getUri() {
    return uri;
  }

  public void setUri(Uri uri) {
    this.uri = uri;
  }

  public boolean isCamera() {
    return isCamera;
  }

  public void setCamera(boolean isCamera) {
    this.isCamera = isCamera;
  }
}
