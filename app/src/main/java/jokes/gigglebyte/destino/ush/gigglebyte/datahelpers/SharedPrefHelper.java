package jokes.gigglebyte.destino.ush.gigglebyte.datahelpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class SharedPrefHelper {

  public static Set<String> getUserFavorites(Activity activity) {
    SharedPreferences favoriteSharedPreferences = activity.getSharedPreferences("POST_FAVORITES", Context.MODE_PRIVATE);
    return favoriteSharedPreferences.getStringSet("POST_FAVORITES", new HashSet<String>());
  }

  public static void saveUserFavorites(Activity activity, Set<String> userFavorites) {
    SharedPreferences favoriteSharedPreferences = activity.getSharedPreferences("POST_FAVORITES", Context.MODE_PRIVATE);
    SharedPreferences.Editor postFavoriteEditor = favoriteSharedPreferences.edit();
    postFavoriteEditor.clear();
    postFavoriteEditor.putStringSet("POST_FAVORITES", userFavorites);
    postFavoriteEditor.commit();
  }

  public static Set<String> getUserLikes(Activity activity) {
    SharedPreferences likesSharedPreferences = activity.getSharedPreferences("POST_LIKES", Context.MODE_PRIVATE);
    return likesSharedPreferences.getStringSet("POST_LIKES", new HashSet<String>());
  }

  public static void saveUserLikes(Activity activity, Set<String> userLikes) {
    SharedPreferences likesSharedPreferences = activity.getSharedPreferences("POST_LIKES", Context.MODE_PRIVATE);
    SharedPreferences.Editor postLikesEditor = likesSharedPreferences.edit();
    postLikesEditor.clear();
    postLikesEditor.putStringSet("POST_LIKES", userLikes);
    postLikesEditor.commit();
  }

  public static Set<String> getUserFollows(Activity activity) {
    SharedPreferences likesSharedPreferences = activity.getSharedPreferences("USER_FOLLOWS", Context.MODE_PRIVATE);
    return likesSharedPreferences.getStringSet("USER_FOLLOWS", new HashSet<String>());
  }

  public static void saveUserFollows(Activity activity, Set<String> userFollows) {
    SharedPreferences followsPreferences = activity.getSharedPreferences("USER_FOLLOWS", Context.MODE_PRIVATE);
    SharedPreferences.Editor followsEditor = followsPreferences.edit();
    followsEditor.clear();
    followsEditor.putStringSet("USER_FOLLOWS", userFollows);
    followsEditor.commit();
  }

  public static Set<String> getPostFlags(Activity activity) {
    SharedPreferences flagsSharedPreferences = activity.getSharedPreferences("POST_FLAGS", Context.MODE_PRIVATE);
    return flagsSharedPreferences.getStringSet("POST_FLAGS", new HashSet<String>());
  }

  public static void savePostFlags(Activity activity, Set<String> postIds) {
    SharedPreferences flagsSharedPreferences = activity.getSharedPreferences("POST_FLAGS", Context.MODE_PRIVATE);
    SharedPreferences.Editor postFlagsEditor = flagsSharedPreferences.edit();
    postFlagsEditor.clear();
    postFlagsEditor.putStringSet("POST_FLAGS", postIds);
    postFlagsEditor.commit();
  }

  public static Set<String> getCommentFlags(Activity activity) {
    SharedPreferences commentSharedPreferences = activity.getSharedPreferences("COMMENT_FLAGS", Context.MODE_PRIVATE);
    return commentSharedPreferences.getStringSet("COMMENT_FLAGS", new HashSet<String>());
  }

  public static void saveCommentFlags(Activity activity, Set<String> userLikes) {
    SharedPreferences commentSharedPreferences = activity.getSharedPreferences("COMMENT_FLAGS", Context.MODE_PRIVATE);
    SharedPreferences.Editor commentFlagEditor = commentSharedPreferences.edit();
    commentFlagEditor.clear();
    commentFlagEditor.putStringSet("COMMENT_FLAGS", userLikes);
    commentFlagEditor.commit();
  }
}
