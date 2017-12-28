package jokes.gigglebyte.destino.ush.gigglebyte.viewholders;

import android.app.Activity;
import android.widget.LinearLayout;
import android.widget.TextView;
import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.enums.OpenScreen;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;

public class MentionViewHolder extends UserProfilePictureHolder{

  public LinearLayout layout;
  public TextView userName;

  public void setUserData(final Activity activity, final User user, OpenScreen screenToOpen) {
    setUserProfile(activity, user, screenToOpen);
    userName.setText(user.getName() == null || user.getName().isEmpty() ? activity.getResources().getString(R.string.unknown) : user.getName() );
  }
}
