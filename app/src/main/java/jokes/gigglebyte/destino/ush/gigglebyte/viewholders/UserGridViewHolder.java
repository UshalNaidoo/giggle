package jokes.gigglebyte.destino.ush.gigglebyte.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;

public class UserGridViewHolder {

  public ProgressBar progressBar;
  public TextView userName;
  public ImageView profileImage;

  public void setUserData(User user) {
    userName.setText(user.getName());
    if (user.getProfile_pic() != null) {
      progressBar.setVisibility(View.INVISIBLE);
      profileImage.setImageBitmap(user.getProfile_pic());
    } else {
      progressBar.setVisibility(View.VISIBLE);
      profileImage.setImageBitmap(null);
    }
  }

}
