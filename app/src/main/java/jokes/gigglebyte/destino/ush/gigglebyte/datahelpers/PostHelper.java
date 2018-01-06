package jokes.gigglebyte.destino.ush.gigglebyte.datahelpers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.fragments.Fragment_New;
import jokes.gigglebyte.destino.ush.gigglebyte.interfaces.onSubmitListener;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Post;
import jokes.gigglebyte.destino.ush.gigglebyte.server.ConnectToServer;

import static jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.PostHelper.PostAction.DELETE_POST;
import static jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.PostHelper.PostAction.EDIT_POST;

public class PostHelper implements onSubmitListener {

  private static List<Post> hotPosts = new ArrayList<>();
  private static List<Post> newPosts = new ArrayList<>();
  private static List<Post> feedPosts = new ArrayList<>();
  private static List<Post> favoritePosts = new ArrayList<>();
  private static List<Post> notifications = new ArrayList<>();

  public enum PostAction {
    LIKE_POST,
    UNLIKE_POST,
    FAVORITE_POST,
    UNFAVORITE_POST,
    DELETE_POST,
    EDIT_POST
  }

  public static List<Post> getHotPosts() {
    return hotPosts;
  }

  public static void initialiseHotPosts(Activity activity, String posts) {
    posts = "{\"posts\":" + posts + "}";
    setHotPosts(activity, JsonParser.GetPosts(posts));
  }

  public static void initialiseNewPosts(Activity activity, String posts) {
    posts = "{\"posts\":" + posts + "}";
    setNewPosts(activity, JsonParser.GetPosts(posts));
  }

  public static void initialiseFavoritePosts(Activity activity, String posts) {
    posts = "{\"posts\":" + posts + "}";
    setFavoritePosts(activity, JsonParser.GetPosts(posts));
  }

  public static void setHotPosts(Activity activity, List<Post> posts) {
    posts = getPostStatus(activity, posts);
    hotPosts = posts;
  }

  public static void initialiseNotifications(Activity activity, String posts) {
    posts = "{\"posts\":" + posts + "}";
    setNotifications(activity, JsonParser.GetPosts(posts));
  }

  private static void setNotifications(Activity activity, List<Post> posts) {
    posts = getPostStatus(activity, posts);
    notifications = posts;
  }

  public static void initialiseFeedPosts(Activity activity, String posts) {
    posts = "{\"posts\":" + posts + "}";
    setFeedPosts(activity, JsonParser.GetPosts(posts));
  }

  public static void setFeedPosts(Activity activity, List<Post> posts) {
    posts = getPostStatus(activity, posts);
    feedPosts = posts;
  }

  public static List<Post> getNotifications() {
    return notifications;
  }

  public static List<Post> getFeedPosts() {
    return feedPosts;
  }

  public static List<Post> getNewPosts() {
    return newPosts;
  }

  public static List<Post> getFavoritePosts() {
    return favoritePosts;
  }

  public static void setNewPosts(Activity activity, List<Post> posts) {
    posts = getPostStatus(activity, posts);
    newPosts = posts;
  }


  public static void setFavoritePosts(Activity activity, List<Post> posts) {
    posts = getPostStatus(activity, posts);
    Collections.sort(posts, PostComparator);
    favoritePosts = posts;
  }

  public static void addFavoritePost(Post post) {
    favoritePosts.add(post);
    Collections.sort(favoritePosts, PostComparator);
  }

  public static void removeFavoritePost(Post post) {
    favoritePosts.remove(post);
    Collections.sort(favoritePosts, PostComparator);
  }

  private static Comparator<Post> PostComparator = new Comparator<Post>() {
    public int compare(Post post1, Post post2) {
      //ascending order
      return (post2.getPostId()) - (post1.getPostId());
    }
  };

  public static void adjustPost(final Activity activity, ImageView image, PostAction action, int likes, final Post post) {
    switch (action) {
      case LIKE_POST:
        /* Liking a Post */
        image.setImageResource(R.drawable.up_arrow);
        for (Post thisPost : getFavoritePosts()) {
          if (thisPost.getPostId() == post.getPostId()) {
            thisPost.setUserLike(true);
            thisPost.setLikes(likes);
            break;
          }
        }
        for (Post thisPost : getNewPosts()) {
          if (thisPost.getPostId() == post.getPostId()) {
            thisPost.setUserLike(true);
            thisPost.setLikes(likes);
            break;
          }
        }
        for (Post thisPost : getFeedPosts()) {
          if (thisPost.getPostId() == post.getPostId()) {
            thisPost.setUserLike(true);
            thisPost.setLikes(likes);
            break;
          }
          if (thisPost.getInnerPost() != null && thisPost.getInnerPost().getPostId() == post.getPostId()) {
            thisPost.getInnerPost().setUserLike(true);
            thisPost.getInnerPost().setLikes(likes);
            break;
          }
        }
        for (Post thisPost : getHotPosts()) {
          if (thisPost.getPostId() == post.getPostId()) {
            thisPost.setUserLike(true);
            thisPost.setLikes(likes);
            break;
          }
        }
        Set<String> user_likes = SharedPrefHelper.getUserLikes(activity);
        user_likes.add(String.valueOf(post.getPostId()));
        SharedPrefHelper.saveUserLikes(activity, user_likes);

        Thread likeThread = new Thread() {
          @Override
          public void run() {
            ConnectToServer.postLike(post.getPostId(), post.getUser().getId(), UserHelper.getUsersId(activity));
          }
        };
        likeThread.start();

        break;

      case UNLIKE_POST:
        /* Un liking a Post */
        image.setImageResource(R.drawable.up_arrow_grey);
        for (Post thisPost : getFavoritePosts()) {
          if (thisPost.getPostId() == post.getPostId()) {
            thisPost.setUserLike(false);
            thisPost.setLikes(likes);
            break;
          }
        }
        for (Post thisPost : getNewPosts()) {
          if (thisPost.getPostId() == post.getPostId()) {
            thisPost.setUserLike(false);
            thisPost.setLikes(likes);
            break;
          }
        }
        for (Post thisPost : getFeedPosts()) {
          if (thisPost.getPostId() == post.getPostId()) {
            thisPost.setUserLike(false);
            thisPost.setLikes(likes);
            break;
          }
          if (thisPost.getInnerPost() != null && thisPost.getInnerPost().getPostId() == post.getPostId()) {
            thisPost.getInnerPost().setUserLike(false);
            thisPost.getInnerPost().setLikes(likes);
            break;
          }
        }
        for (Post thisPost : getHotPosts()) {
          if (thisPost.getPostId() == post.getPostId()) {
            thisPost.setUserLike(false);
            thisPost.setLikes(likes);
            break;
          }
        }
        Set<String> user_remaining_likes = SharedPrefHelper.getUserLikes(activity);
        user_remaining_likes.remove(String.valueOf(post.getPostId()));
        SharedPrefHelper.saveUserLikes(activity, user_remaining_likes);

        Thread unlikeThread = new Thread() {
          @Override
          public void run() {
            ConnectToServer.postUnlike(post.getPostId(), UserHelper.getUsersId(activity));
          }
        };
        unlikeThread.start();

        break;

      case FAVORITE_POST:
        /* Favorite a Post */
        image.setImageResource(R.drawable.heart_like);
        for (Post thisPost : getNewPosts()) {
          if (thisPost.getPostId() == post.getPostId()) {
            thisPost.setUserFavorite(true);
            break;
          }
        }
        for (Post thisPost : getHotPosts()) {
          if (thisPost.getPostId() == post.getPostId()) {
            thisPost.setUserFavorite(true);
            break;
          }
        }
        for (Post thisPost : getFavoritePosts()) {
          if (thisPost.getPostId() == post.getPostId()) {
            thisPost.setUserFavorite(true);
            break;
          }
        }
        for (Post thisPost : getHotPosts()) {
          if (thisPost.getPostId() == post.getPostId()) {
            thisPost.setUserFavorite(true);
            break;
          }
        }
        for (Post thisPost : getFeedPosts()) {
          if (thisPost.getPostId() == post.getPostId()) {
            thisPost.setUserFavorite(true);
            break;
          }
          if (thisPost.getInnerPost() != null && thisPost.getInnerPost().getPostId() == post.getPostId()) {
            thisPost.getInnerPost().setUserFavorite(true);
            break;
          }
        }
        Set<String> user_favorites = SharedPrefHelper.getUserFavorites(activity);
        user_favorites.add(String.valueOf(post.getPostId()));
        addFavoritePost(post);
        SharedPrefHelper.saveUserFavorites(activity, user_favorites);

        Thread favouriteThread = new Thread() {
          @Override
          public void run() {
            ConnectToServer.postFavourite(post.getPostId(), post.getUser().getId(), UserHelper.getUsersId(activity));
          }
        };
        favouriteThread.start();
        break;

      case UNFAVORITE_POST:
        /* Un favorite a Post */
        image.setImageResource(R.drawable.heart_unlike);
        for (Post thisPost : getNewPosts()) {
          if (thisPost.getPostId() == post.getPostId()) {
            thisPost.setUserFavorite(false);
            break;
          }
        }
        for (Post thisPost : getHotPosts()) {
          if (thisPost.getPostId() == post.getPostId()) {
            thisPost.setUserFavorite(false);
            break;
          }
        }
        for (Post thisPost : getFavoritePosts()) {
          if (thisPost.getPostId() == post.getPostId()) {
            thisPost.setUserFavorite(false);
            break;
          }
        }
        for (Post thisPost : getFeedPosts()) {
          if (thisPost.getPostId() == post.getPostId()) {
            thisPost.setUserFavorite(false);
            break;
          }
          if (thisPost.getInnerPost() != null && thisPost.getInnerPost().getPostId() == post.getPostId()) {
            thisPost.getInnerPost().setUserFavorite(false);
            break;
          }
        }
        user_favorites = SharedPrefHelper.getUserFavorites(activity);
        user_favorites.remove(String.valueOf(post.getPostId()));
        removeFavoritePost(post);
        SharedPrefHelper.saveUserFavorites(activity, user_favorites);
        Thread unfavouriteThread = new Thread() {
          @Override
          public void run() {
            ConnectToServer.postUnfavourite(post.getPostId(), UserHelper.getUsersId(activity));
          }
        };
        unfavouriteThread.start();
        break;

      case DELETE_POST:
        for (int i = 0 ; i < getNewPosts().size(); i++) {
          if (getNewPosts().get(i).getPostId() == post.getPostId()) {
            getNewPosts().remove(i);
            break;
          }
        }
        for (int i = 0 ; i < getHotPosts().size(); i++) {
          if (getHotPosts().get(i).getPostId() == post.getPostId()) {
            getHotPosts().remove(i);
            break;
          }
        }
        for (int i = 0 ; i < getFavoritePosts().size(); i++) {
          if (getFavoritePosts().get(i).getPostId() == post.getPostId()) {
            getFavoritePosts().remove(i);
            break;
          }
        }
        for (int i = 0 ; i < getFeedPosts().size(); i++) {
          if (getFeedPosts().get(i).getPostId() == post.getPostId()) {
            getFeedPosts().remove(i);
            break;
          }
          if (getFeedPosts().get(i).getInnerPost() != null && getFeedPosts().get(i).getInnerPost().getPostId() == post.getPostId()) {
            getFeedPosts().remove(i);
            break;
          }
        }
        user_favorites = SharedPrefHelper.getUserFavorites(activity);
        user_favorites.remove(String.valueOf(post.getPostId()));
        removeFavoritePost(post);
        SharedPrefHelper.saveUserFavorites(activity, user_favorites);
        break;

      case EDIT_POST:
        for (int i = 0 ; i < getNewPosts().size(); i++) {
          if (getNewPosts().get(i).getPostId() == post.getPostId()) {
            getNewPosts().get(i).setPostTitle(post.getPostTitle());
            getNewPosts().get(i).setPostText(post.getPostText());
            break;
          }
        }
        for (int i = 0 ; i < getHotPosts().size(); i++) {
          if (getHotPosts().get(i).getPostId() == post.getPostId()) {
            getHotPosts().get(i).setPostTitle(post.getPostTitle());
            getHotPosts().get(i).setPostText(post.getPostText());
            break;
          }
        }
        for (int i = 0 ; i < getFavoritePosts().size(); i++) {
          if (getFavoritePosts().get(i).getPostId() == post.getPostId()) {
            getFavoritePosts().get(i).setPostTitle(post.getPostTitle());
            getFavoritePosts().get(i).setPostText(post.getPostText());
            break;
          }
        }
        for (int i = 0 ; i < getFeedPosts().size(); i++) {
          if (getFeedPosts().get(i).getPostId() == post.getPostId()) {
            getFeedPosts().get(i).setPostTitle(post.getPostTitle());
            getFeedPosts().get(i).setPostText(post.getPostText());
            break;
          }
          if (getFeedPosts().get(i).getInnerPost() != null && getFeedPosts().get(i).getInnerPost().getPostId() == post.getPostId()) {
            getFeedPosts().get(i).getInnerPost().setPostTitle(post.getPostTitle());
            getFeedPosts().get(i).getInnerPost().setPostText(post.getPostText());
            break;
          }
        }
        break;
    }

    if(action != DELETE_POST && action != EDIT_POST) {
      UIHelper.imageViewClickAnimation(image);
    }
    UIHelper.updateScreen();
  }

  public static Post getPostStatus(Activity activity, Post post) {
    List<Post> posts = new ArrayList<>();
    posts.add(post);
    return getPostStatus(activity, posts).get(0);
  }

  public static List<Post> getPostStatus(Activity activity, List<Post> posts) {
    //Store likes in shared Prefs
    Set<String> likes = SharedPrefHelper.getUserLikes(activity);
    Set<String> favorites = SharedPrefHelper.getUserFavorites(activity);

    if (posts != null) {
      for (Post p : posts) {
        for (String s : likes) {
          if (p.getPostId() == Integer.parseInt(s)) {
            p.setUserLike(true);
          }
          if(p.getInnerPost() != null && p.getInnerPost().getPostId() == Integer.parseInt(s)){
            p.getInnerPost().setUserLike(true);
          }
        }
        for (String s : favorites) {
          if (p.getPostId() == Integer.parseInt(s)) {
            p.setUserFavorite(true);
          }
          if(p.getInnerPost() != null && p.getInnerPost().getPostId() == Integer.parseInt(s)){
            p.getInnerPost().setUserFavorite(true);
          }
        }
      }
    }
    return posts;
  }

  @Override
  public void setOnSubmitListener(Object arg) {
    newPosts.add(0, (Post) arg);
    Fragment_New.refreshList();
  }

  @Override
  public void setOnSubmitListener(Activity activity, Object arg) {
    setOnSubmitListener(null, arg);
  }

  public static void updatePosts(int userId, String userName, Bitmap bitmap) {
    ArrayList<Post> posts = new ArrayList<>();
    posts.addAll(hotPosts);
    posts.addAll(newPosts);
    posts.addAll(favoritePosts);
    for (Post p : posts) {
      if (p.getUser().getId() == userId) {
        if (userName != null) {
          p.getUser().setName(userName);
        }
        if (bitmap != null) {
          p.getUser().setProfile_pic(bitmap);
        }
      }
    }
  }

  public static void shareBitmap(Activity activity, View v) {
    try {
      v.setDrawingCacheEnabled(true);
      v.buildDrawingCache();
      Bitmap image = v.getDrawingCache();

      Intent share = new Intent(Intent.ACTION_SEND);
      share.setType("image/jpeg");
      ByteArrayOutputStream bytes = new ByteArrayOutputStream();
      image.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
      File f = new File(
          Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
      try {
        f.createNewFile();
        FileOutputStream fo = new FileOutputStream(f);
        fo.write(bytes.toByteArray());
      } catch (IOException e) {
        e.printStackTrace();
      }
      share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporary_file.jpg"));
      activity.startActivity(Intent.createChooser(share, "Share Image"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
