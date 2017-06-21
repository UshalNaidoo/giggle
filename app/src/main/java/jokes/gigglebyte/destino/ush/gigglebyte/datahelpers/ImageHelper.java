package jokes.gigglebyte.destino.ush.gigglebyte.datahelpers;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import jokes.gigglebyte.destino.ush.gigglebyte.activities.UserProfileActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.interfaces.onSubmitListener;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;

public class ImageHelper implements onSubmitListener {

  private void saveProfilePicture(Bitmap bitmap, int userId) {
    try {
      String file_path =
          Environment.getExternalStorageDirectory().getAbsolutePath() + "/Profile_Pictures";
      File dir = new File(file_path);
      if (!dir.exists()) {
        dir.mkdirs();
      }
      File file = new File(dir, "profile_picture_" + userId + ".png");
      FileOutputStream fOut = new FileOutputStream(file);
      bitmap.compress(Bitmap.CompressFormat.JPEG, 55, fOut);
      fOut.flush();
      fOut.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static Bitmap getProfilePicture(int userId) {
    Bitmap bitmap = null;
    String file_path =
        Environment.getExternalStorageDirectory().getAbsolutePath() + "/Profile_Pictures/"
        + "profile_picture_" + userId + ".png";
    File f = new File(file_path);
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
    try {
      bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
    } catch (FileNotFoundException e) {
    }
    return bitmap;
  }

  @Override
  public void setOnSubmitListener(Activity activity, Object arg) {
    User user = (User) arg;
    saveProfilePicture(user.getProfile_pic(), user.getId());
    PostHelper.updatePosts(user.getId(), user.getName(), ImageHelper.getProfilePicture(user.getId()));
    UIHelper.updateScreen();
    UserProfileActivity.refreshUser(user);
  }

  @Override
  public void setOnSubmitListener(Object arg) {
    setOnSubmitListener(null, arg);
  }
}
