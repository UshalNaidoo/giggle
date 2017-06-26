package jokes.gigglebyte.destino.ush.gigglebyte.datahelpers;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.SharedPreferences;

import android.graphics.Bitmap;
import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.fragments.Fragment_Profile;
import jokes.gigglebyte.destino.ush.gigglebyte.interfaces.onSubmitListener;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;

public class UserHelper implements onSubmitListener {
  public static User selectedUser;
  public static Map<Integer, Bitmap> userImages = new HashMap<>();

  @Override
  public void setOnSubmitListener(Object arg) {
  }

  @Override
  public void setOnSubmitListener(Activity activity, Object arg) {
    User user = (User) arg;
    saveUserDetails(activity, user);
    PostHelper.updatePosts(user.getId(), user.getName(), ImageHelper.getProfilePicture(user.getId()));
    Fragment_Profile.refreshUser(user);
    UIHelper.updateScreen();
  }

  public static User getUserDetails(Activity activity) {
    SharedPreferences prefs = activity.getSharedPreferences("USER_DETAILS", Activity.MODE_PRIVATE);
    int id = prefs.getInt("user_id", -1);
    User myProfile = new User();
    myProfile.setName(prefs.getString("user_name", activity.getResources().getString(R.string.unknown)));
    myProfile.setId(id);
    myProfile.setDescription(prefs.getString("user_description",activity.getResources().getString(R.string.newUserDescription)));
    myProfile.setProfile_pic(ImageHelper.getProfilePicture(id));
    return myProfile;
  }

  public static int getUsersId(Activity activity) {
    SharedPreferences prefs = activity.getSharedPreferences("USER_DETAILS", Activity.MODE_PRIVATE);
    return prefs.getInt("user_id", -1);
  }

  public static void saveUserDetails(Activity activity, final User user) {
    SharedPreferences userPreferences = activity.getSharedPreferences("USER_DETAILS", Activity.MODE_PRIVATE);
    SharedPreferences.Editor userDetailsEditor = userPreferences.edit();
    userDetailsEditor.clear();
    userDetailsEditor.putInt("user_id", user.getId());
    userDetailsEditor.putString("user_name", user.getName());
    userDetailsEditor.putString("user_description", user.getDescription());
    userDetailsEditor.apply();
  }
}
