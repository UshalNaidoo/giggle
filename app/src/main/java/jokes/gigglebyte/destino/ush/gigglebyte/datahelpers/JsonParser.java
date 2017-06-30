package jokes.gigglebyte.destino.ush.gigglebyte.datahelpers;

import android.support.annotation.NonNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jokes.gigglebyte.destino.ush.gigglebyte.objects.Comment;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Post;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.PostType;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Tag;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;

public class JsonParser {

  public static List<Post> GetPosts(String response) {
    List<Post> posts = new ArrayList<>();
    try {
      JSONObject json = new JSONObject(response);
      JSONArray jsonPosts = json.getJSONArray("posts");
      if (jsonPosts != null) {
        for (int i = 0; i < jsonPosts.length(); i++) {
          JSONObject jsonObject = jsonPosts.getJSONObject(i);
          posts.add((jsonObject.getInt("type") == 0) ? getTextPost(jsonObject) : getImagePost(jsonObject));
        }
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return posts;
  }

  @NonNull
  private static Post getTextPost(JSONObject jsonObject) throws JSONException {
    Post nextPost = new Post();
    nextPost.setLikes(jsonObject.getInt("likes"));
    nextPost.setPostText(jsonObject.getString("text"));
    User user = new User(jsonObject.getInt("user_id"), jsonObject.getString("user_name"), null, null);
    nextPost.setUser(user);
    nextPost.setTimeSincePost(jsonObject.getString("time_since"));
    nextPost.setPostId(jsonObject.getInt("_id"));
    nextPost.setCommentCount(jsonObject.getInt("comments"));
    nextPost.setType(PostType.TEXT_POST);
    getPostTags(nextPost, jsonObject.getJSONArray("tag"));
    return nextPost;
  }

  @NonNull
  private static Post getImagePost(JSONObject jsonObject) throws JSONException {
    Post nextPost = new Post();
    nextPost.setLikes(jsonObject.getInt("likes"));
    User user = new User(jsonObject.getInt("user_id"), jsonObject.getString("user_name"), null, null);
    nextPost.setUser(user);
    nextPost.setTimeSincePost(jsonObject.getString("time_since"));
    nextPost.setPostId(jsonObject.getInt("_id"));
    nextPost.setPostTitle(jsonObject.getString("title"));
    nextPost.setCommentCount(jsonObject.getInt("comments"));
    nextPost.setType(PostType.IMAGE_POST);
    nextPost.setImageId(jsonObject.getInt("image_id"));
    getPostTags(nextPost, jsonObject.getJSONArray("tag"));
    return nextPost;
  }

  @NonNull
  private static Post getFollowingNotification(JSONObject jsonObject) throws JSONException {
    Post Post = new Post();
    Post.setPostId(jsonObject.getInt("_id"));
    User user = new User(jsonObject.getInt("user_id"), jsonObject.getString("user_name"), null, null);
    Post.setUser(user);
    User followingUser = new User(jsonObject.getInt("text"), jsonObject.getString("title"), null, null);
    Post.setFollowingUser(followingUser);
    Post.setTimeSincePost(jsonObject.getString("time_since"));
    Post.setType(PostType.FOLLOWING_NOTIFICATION);
    return Post;
  }

  public static List<Post> GetFeed(String response) {
    List<Post> posts = new ArrayList<>();
    try {
      JSONObject json = new JSONObject(response);
      JSONArray jsonPosts = json.getJSONArray("posts");
      if (jsonPosts != null) {
        for (int i = 0; i < jsonPosts.length(); i++) {
          JSONObject jsonObject = jsonPosts.getJSONObject(i);
          switch (jsonObject.getInt("type")) {
            case 0 :
              posts.add(getTextPost(jsonObject));
              break;
            case 1 :
              posts.add(getImagePost(jsonObject));
              break;
            case 2 :
              posts.add(getFollowingNotification(jsonObject));
              break;
          }
        }
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return posts;
  }

  private static void getPostTags(Post nextPost, JSONArray jsonTags) throws JSONException {
    if (jsonTags != null) {
      List<String> tags = new ArrayList<>();
      for (int j = 0; j < jsonTags.length(); j++) {
        JSONObject tag = jsonTags.getJSONObject(j);
        tags.add(tag.getString("tag"));
      }
      nextPost.setTags(tags);
    }
  }

  public static List<Comment> GetComments(String response) {
    List<Comment> comments = new ArrayList<>();
    try {
      JSONObject json = new JSONObject(response);
      JSONArray jsonPosts = json.getJSONArray("comments");
      for (int i = 0; i < jsonPosts.length(); i++) {
        JSONObject jsonObject = jsonPosts.getJSONObject(i);
        Comment comment = new Comment();
        comment.setLikes(jsonObject.getInt("likes"));
        comment.setCommentId(jsonObject.getInt("_id"));
        comment.setCommentText(jsonObject.getString("text"));
        User user = new User(jsonObject.getInt("user_id"), jsonObject.getString("user_name"), null, null);
        comment.setUser(user);
        comments.add(comment);
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return comments;
  }

  public static User GetUser(String response) {
    User user = new User();
    try {
      JSONObject json = new JSONObject(response);
      user.setName(json.getString("name"));
      user.setDescription(json.getString("description"));
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return user;
  }

  public static List<User> GetUsers(String response) {
    List<User> users = new ArrayList<>();
    try {
      JSONObject json = new JSONObject(response);
      JSONArray jsonPosts = json.getJSONArray("users");
      for (int i = 0; i < jsonPosts.length(); i++) {
        JSONObject jsonObject = jsonPosts.getJSONObject(i);
        User user = new User();
        user.setId(jsonObject.getInt("_id"));
        user.setName(jsonObject.getString("name"));
        user.setDescription(jsonObject.getString("description"));
        user.setNumberOfFollowers(jsonObject.getInt("numberOfFollowers"));
        user.setNumberOfPosts(jsonObject.getInt("numberOfPosts"));
        users.add(user);
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return users;
  }

  public static List<Tag> GetTagsFromSearch(String response) {
    List<Tag> tags = new ArrayList<>();
    try {
      JSONObject json = new JSONObject(response);
      JSONArray jsonPosts = json.getJSONArray("tags");
      for (int i = 0; i < jsonPosts.length(); i++) {
        JSONObject jsonObject = jsonPosts.getJSONObject(i);
        Tag tag = new Tag();
        tag.setTagText("#" + jsonObject.getString("tag"));
        tag.setNumberOfPosts(jsonObject.getInt("number"));
        tags.add(tag);
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return tags;
  }

  public static List<String> GetAllTagsList(String response) {
    List<String> tags = new ArrayList<>();
    try {
      JSONObject json = new JSONObject("{\"tags\":" + response + "}");
      JSONArray jsonPosts = json.getJSONArray("tags");
      for (int i = 0; i < jsonPosts.length(); i++) {
        JSONObject jsonObject = jsonPosts.getJSONObject(i);
        tags.add("#" + jsonObject.getString("tag"));
        tags.add(jsonObject.getString("tag"));
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return tags;
  }
}
