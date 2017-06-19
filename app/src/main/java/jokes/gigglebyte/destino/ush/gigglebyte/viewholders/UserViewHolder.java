package jokes.gigglebyte.destino.ush.gigglebyte.viewholders;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import jokes.gigglebyte.destino.ush.gigglebyte.activities.LargeProfileImageActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;

public class UserViewHolder {

  public ProgressBar progressBar;
  public TextView description;
  public TextView userName;
  public ImageView profileImage;

  public void setUserData(final Activity activity, final User user) {
    progressBar.setVisibility(View.VISIBLE);
    profileImage.setVisibility(View.INVISIBLE);
    if (user.getProfile_pic() == null) {
      user.loadImage(activity, profileImage);
    } else {
      profileImage.setImageBitmap(user.getProfile_pic());
    }
    profileImage.setVisibility(View.VISIBLE);
    progressBar.setVisibility(View.INVISIBLE);

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

    description.setText((user.getDescription() == null || user.getDescription()
        .isEmpty()) ? "I'm new to Gigglebyte" : user.getDescription());
  }

}
