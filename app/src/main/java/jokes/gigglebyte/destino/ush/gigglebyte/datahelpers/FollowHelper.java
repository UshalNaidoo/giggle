package jokes.gigglebyte.destino.ush.gigglebyte.datahelpers;

import android.app.Activity;

import java.util.Iterator;
import java.util.List;

import jokes.gigglebyte.destino.ush.gigglebyte.activities.FollowersActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.activities.MainActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.fragments.Fragment_Search_User;
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
      if (userId == user.getId()) {
        return true;
      }
    }
    return false;
  }

  public static void followUser(final Activity activity, final User followUser) {
    following.add(followUser);
    final int id = UserHelper.getUsersId(activity);

    for (User user : MainActivity.loadedUsers) {
      if (user.getId() == followUser.getId()) {
        user.setNumberOfFollowers(user.getNumberOfFollowers() + 1);
        break;
      }
    }

    Thread thread = new Thread() {
      @Override
      public void run() {
        ConnectToServer.followUser(id, followUser.getId());
        PostHelper.initialiseFeedPosts(activity, ConnectToServer.getFeed(id));
      }
    };
    thread.start();

    UIHelper.updateScreen();
    FollowersActivity.refresh();
    Fragment_Search_User.refresh();
  }

  public static void unfollowUser(Activity activity, final User unfollowUser) {
    for (Iterator<User> iterator = following.listIterator(); iterator.hasNext(); ) {
      User user = iterator.next();
      if (unfollowUser.getId() == user.getId()) {
        iterator.remove();
      }
    }

    final int id = UserHelper.getUsersId(activity);

    List<Post> posts = PostHelper.getFeedPosts();
    for (Iterator<Post> iterator = posts.listIterator(); iterator.hasNext(); ) {
      Post post = iterator.next();
      if (unfollowUser.getId() == post.getUser().getId()) {
        iterator.remove();
      }
    }

    for (User user : MainActivity.loadedUsers) {
      if (user.getId() == unfollowUser.getId()) {
        user.setNumberOfFollowers(user.getNumberOfFollowers() - 1);
        break;
      }
    }

    PostHelper.setFeedPosts(activity, posts);

    UIHelper.updateScreen();
    FollowersActivity.refresh();
    Fragment_Search_User.refresh();
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
