package jokes.gigglebyte.destino.ush.gigglebyte.viewholders;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import jokes.gigglebyte.destino.ush.gigglebyte.activities.LargeProfileImageActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;

public class UserProfilePictureHolder {

  public ProgressBar progressBar;
  public ImageView profileImage;

  public void setUserProfile(final Activity activity, final User user, boolean isClickable) {
    progressBar.setVisibility(View.VISIBLE);
    profileImage.setVisibility(View.INVISIBLE);
    if (user.getProfile_pic() == null) {
      user.loadImage(activity, profileImage);
    } else {
      profileImage.setImageBitmap(user.getProfile_pic());
    }
    profileImage.setVisibility(View.VISIBLE);
    progressBar.setVisibility(View.INVISIBLE);

    if (isClickable) {
      profileImage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (user.getProfile_pic() != null) {
            Intent intent = new Intent(activity, LargeProfileImageActivity.class);
            intent.putExtra("userId", user.getId());
            activity.startActivity(intent);
          }
        }
      });
    }

  }

}
