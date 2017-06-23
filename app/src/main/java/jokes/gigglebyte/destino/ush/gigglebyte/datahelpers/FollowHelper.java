package jokes.gigglebyte.destino.ush.gigglebyte.datahelpers;

import android.app.Activity;
import android.content.SharedPreferences;

import java.util.Iterator;
import java.util.List;

import jokes.gigglebyte.destino.ush.gigglebyte.fragments.Fragment_Feed;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Post;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;
import jokes.gigglebyte.destino.ush.gigglebyte.server.ConnectToServer;

public class FollowHelper {

  private static List<User> following;
  private static List<User> followers;

  public static void initialiseUserFollowing(String users) {
    users = "{\"users\":" + users + "}";
    following = JsonParser.GetUsers(users);
  }

  public static void initialiseUserFollowers(String users) {
    users = "{\"users\":" + users + "}";
    followers = JsonParser.GetUsers(users);
  }

  public static boolean isFollowingUser(int userId) {
    for (User user : following) {
      if (userId == user.getId()) return true;
    }
    return false;
  }

  public static void followUser(final Activity activity, final User followUser) {
    following.add(followUser);
    SharedPreferences prefs = activity.getSharedPreferences("USER_DETAILS", Activity.MODE_PRIVATE);
    final int id = prefs.getInt("user_id", -1);

    Thread thread = new Thread() {
      @Override
      public void run() {
        ConnectToServer.followUser(id, followUser.getId());
        PostHelper.initialiseFeedPosts(activity, ConnectToServer.getFeed(id));
      }
    };
    thread.start();
    Fragment_Feed.refreshList();
  }

  public static void unfollowUser(Activity activity, final User unfollowUser) {
    following.remove(unfollowUser);
    SharedPreferences prefs = activity.getSharedPreferences("USER_DETAILS", Activity.MODE_PRIVATE);
    final int id = prefs.getInt("user_id", -1);

    List<Post> posts = PostHelper.getFeedPosts();
    for (Iterator<Post> iterator = posts.listIterator(); iterator.hasNext(); ) {
      Post post = iterator.next();
      if (unfollowUser.getId() == post.getUser().getId()) {
        iterator.remove();
      }
    }
    PostHelper.setFeedPosts(activity, posts);
    Fragment_Feed.refreshList();

    Thread thread = new Thread() {
      @Override
      public void run() {
        ConnectToServer.unfollowUser(id, unfollowUser.getId());
      }
    };
    thread.start();

  }

  public static List<User> getFollowing() {
    return following;
  }

  public static List<User> getFollowers() {
    return followers;
  }

}
