package jokes.gigglebyte.destino.ush.gigglebyte.datahelpers;

import android.app.Activity;
import android.content.SharedPreferences;

import jokes.gigglebyte.destino.ush.gigglebyte.activities.UserProfileActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.interfaces.onSubmitListener;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;

public class UserHelper implements onSubmitListener {
  static User selectedUser;

  @Override
  public void setOnSubmitListener(Object arg) {
  }

  @Override
  public void setOnSubmitListener(Activity activity, Object arg) {
    User user = (User) arg;
    saveUserDetails(activity, user);
    PostHelper.updatePosts(user.getId(), user.getName(), ImageHelper.getProfilePicture(user.getId()));
    UserProfileActivity.refreshUser(user);
    UIHelper.updateScreen();
  }

  public static User getUserDetails(Activity activity) {
    SharedPreferences prefs = activity.getSharedPreferences("USER_DETAILS", Activity.MODE_PRIVATE);
    int id = prefs.getInt("user_id", -1);
    User myProfile = new User();
    myProfile.setName(prefs.getString("user_name", "Unknown"));
    myProfile.setId(id);
    myProfile.setDescription(prefs.getString("user_description", "I'm new to Gigglebyte"));
    myProfile.setProfile_pic(ImageHelper.getProfilePicture(id));
    return myProfile;
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
