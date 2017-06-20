package jokes.gigglebyte.destino.ush.gigglebyte.viewholders;

import android.app.Activity;
import android.widget.TextView;

import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;

public class UserListViewHolder extends UserProfilePictureHolder{

  public TextView description;

  public void setUserData(final Activity activity, final User user) {
    setUserProfile(activity, user, true);
    description.setText((user.getDescription() == null || user.getDescription()
        .isEmpty()) ? "I'm new to Gigglebyte" : user.getDescription());
  }

}
