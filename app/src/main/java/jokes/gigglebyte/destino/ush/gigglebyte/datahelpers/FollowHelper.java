package jokes.gigglebyte.destino.ush.gigglebyte.datahelpers;

import android.app.Activity;
import android.content.SharedPreferences;

import java.util.List;

import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;
import jokes.gigglebyte.destino.ush.gigglebyte.server.ConnectToServer;

public class FollowHelper {

  static List<User> following;

  public static void initialiseUserFollows(String users) {
    users = "{\"users\":" + users + "}";
    following = JsonParser.GetUsers(users);
  }

  public static boolean isFollowingUser(int userId) {
    for (User user : following) {
      if (userId == user.getId()) return true;
    }
    return false;
  }

  public static void followUser(Activity activity, final User followUser) {
    following.add(followUser);
    SharedPreferences prefs = activity.getSharedPreferences("USER_DETAILS", Activity.MODE_PRIVATE);
    final int id = prefs.getInt("user_id", -1);

    Thread thread = new Thread() {
      @Override
      public void run() {
        ConnectToServer.followUser(id, followUser.getId());
      }
    };
    thread.start();
  }

  public static void unfollowUser(Activity activity, final User unfollowUser) {
    following.remove(unfollowUser);
    SharedPreferences prefs = activity.getSharedPreferences("USER_DETAILS", Activity.MODE_PRIVATE);
    final int id = prefs.getInt("user_id", -1);

    Thread thread = new Thread() {
      @Override
      public void run() {
        ConnectToServer.unfollowUser(id, unfollowUser.getId());
      }
    };
    thread.start();
  }

}
