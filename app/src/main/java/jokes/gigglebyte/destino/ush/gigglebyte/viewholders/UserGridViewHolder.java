package jokes.gigglebyte.destino.ush.gigglebyte.viewholders;

import android.app.Activity;
import android.widget.TextView;
import jokes.gigglebyte.destino.ush.gigglebyte.enums.OpenScreen;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;

public class UserGridViewHolder extends UserProfilePictureHolder{

  public TextView userName;

  public void setUserData(Activity activity, User user, OpenScreen screenToOpen) {
    setUserProfile(activity, user, screenToOpen);
    userName.setText(user.getName() == null || user.getName().isEmpty() ? "Unknown" : user.getName() );
  }

}
