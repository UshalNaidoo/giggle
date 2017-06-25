package jokes.gigglebyte.destino.ush.gigglebyte.server;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Set;

public class ConnectToServer {

  private static String response = null;
  private static JSONObject json = null;

  public static int registerNewUser() {
    String parameters = "key=AoD93128Jd73jKH31je3";
    String UrlString = ServerSettings._Server + ServerSettings._newUser;
    try {
      response = Connect.connectToServer(UrlString, parameters);
      try {
        json = new JSONObject(response);
        return json.getInt("_id");

      } catch (JSONException e) {
        return -1;
      }
    } catch (Exception e) {
      return -1;
    }
  }

  public static void changeUserDescription(int id, String description) {
    String parameters = "user_id=" + id + "&description="+description;
    String UrlString = ServerSettings._Server + ServerSettings._changeUserDescription;
    try {
      response = Connect.connectToServer(UrlString, parameters);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void changeUserName(int id, String userName) {
    String parameters = "user_id=" + id + "&user_name="+userName;
    String UrlString = ServerSettings._Server + ServerSettings._changeUserName;
    try {
      response = Connect.connectToServer(UrlString, parameters);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /* Get Data */
  public static String getNewPosts() {
    String parameters = "key=AoD93128Jd73jKH31je3";
    String UrlString = ServerSettings._Server + ServerSettings._getNewPosts;
    try {
      return Connect.connectToServer(UrlString, parameters);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  public static String getFavoritePosts(Set<String> post_ids) {
    String parameters = "key=AoD93128Jd73jKH31je3&post_ids="+post_ids.toString().replace("[", "").replace("]", "");
    String UrlString = ServerSettings._Server + ServerSettings._getFavoritePosts;
    try {
      return Connect.connectToServer(UrlString, parameters);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  public static String getHotPosts() {
    String parameters = "key=AoD93128Jd73jKH31je3";
    String UrlString = ServerSettings._Server + ServerSettings._getHotPosts;
    try {
      return Connect.connectToServer(UrlString, parameters);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  public static String getComments(int postId) {
    String parameters = "post_id=" + postId;
    String UrlString = ServerSettings._Server + ServerSettings._getComments;
    try {
      return Connect.connectToServer(UrlString, parameters);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  public static String getUserDetails(int userId) {
    String parameters = "user_id=" + userId;
    String UrlString = ServerSettings._Server + ServerSettings._getUserDetails;
    try {
      return Connect.connectToServer(UrlString, parameters);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  public static String getUsersPosts(int userId) {
    String parameters = "user_id=" + userId + "&key=AoD93128Jd73jKH31je3";
    String UrlString = ServerSettings._Server + ServerSettings._getUsersPosts;
    try {
      return Connect.connectToServer(UrlString, parameters);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  public static String getFeed(int userId) {
    String parameters = "user_id=" + userId + "&key=AoD93128Jd73jKH31je3";
    String UrlString = ServerSettings._Server + ServerSettings._getFeed;
    try {
      return Connect.connectToServer(UrlString, parameters);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  public static String getTagPosts(String tag) {
    String parameters = "tag=" + tag + "&key=AoD93128Jd73jKH31je3";
    String UrlString = ServerSettings._Server + ServerSettings._getTagPosts;
    try {
      return Connect.connectToServer(UrlString, parameters);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  /* Post Data */
  public static int postTextPost(int id, String postText, Set<String> tags) {
    JSONArray JSONposts = new JSONArray();
    for (String s : tags) {
      try {
        JSONObject JSONpost = new JSONObject();
        JSONpost.put("tags", s);
        JSONposts.put(JSONpost);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }

    String parameters = "user_id=" + id +"&tags="+ JSONposts+"&post_text=" + postText;
    String UrlString = ServerSettings._Server + ServerSettings._postTextPost;
    try {
      response = Connect.connectToServer(UrlString, parameters);
      try {
        json = new JSONObject(response);
        if (json.getString("result").equals("true")) {
          return json.getInt("postId");
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return -1;
  }

  public static int postImagePost(int id, String image, Set<String> tags, String title) {
    JSONArray JSONposts = new JSONArray();
    for (String s : tags) {
      try {
        JSONObject JSONpost = new JSONObject();
        JSONpost.put("tags", s);
        JSONposts.put(JSONpost);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }

    String UrlString = ServerSettings._Server + ServerSettings._postImagePost;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
    nameValuePairs.add(new BasicNameValuePair("user_id", String.valueOf(id)));
    nameValuePairs.add(new BasicNameValuePair("image", image));
    nameValuePairs.add(new BasicNameValuePair("title", title));
    nameValuePairs.add(new BasicNameValuePair("tags", JSONposts.toString()));
    HttpClient httpclient = new DefaultHttpClient();
    HttpPost httppost = new HttpPost(UrlString);
    try {
      httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    try {
      httpclient.execute(httppost);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return -1;
  }

  public static void postComment(int id, int postId, String commentText, int posterId) {
    String parameters = "user_id=" + id + "&post_id=" + postId + "&comment_text=" + commentText + "&notify_id=" + posterId;
    String UrlString = ServerSettings._Server + ServerSettings._postComment;
    try {
      response = Connect.connectToServer(UrlString, parameters);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void postLike(int postId, int posterId) {
    String parameters = "post_id=" + postId + "&notify_id=" + posterId;
    String UrlString = ServerSettings._Server + ServerSettings._postLike;
    try {
      response = Connect.connectToServer(UrlString, parameters);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void postUnlike(int postId) {
    String parameters = "post_id=" + postId;
    String UrlString = ServerSettings._Server + ServerSettings._postUnlike;
    try {
      response = Connect.connectToServer(UrlString, parameters);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void commentLike(int commentId, int commenterId) {
    String parameters = "comment_id=" + commentId + "&notify_id=" + commenterId;
    String UrlString = ServerSettings._Server + ServerSettings._commentLike;
    try {
      response = Connect.connectToServer(UrlString, parameters);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void commentUnlike(int commentId) {
    String parameters = "comment_id=" + commentId;
    String UrlString = ServerSettings._Server + ServerSettings._commentUnlike;
    try {
      response = Connect.connectToServer(UrlString, parameters);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static String searchUser(String text) {
    String parameters = "text=" + text;
    String UrlString = ServerSettings._Server + ServerSettings._searchUser;
    try {
      return Connect.connectToServer(UrlString, parameters);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  public static String getUserFollowing(int userId) {
    String parameters = "user_id=" + userId + "&key=AoD93128Jd73jKH31je3";
    String UrlString = ServerSettings._Server + ServerSettings._getFollowing;
    try {
      return Connect.connectToServer(UrlString, parameters);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  public static String getUserFollowers(int userId) {
    String parameters = "user_id=" + userId + "&key=AoD93128Jd73jKH31je3";
    String UrlString = ServerSettings._Server + ServerSettings._getFollowers;
    try {
      return Connect.connectToServer(UrlString, parameters);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  public static String followUser(int userId, int followingId) {
    String parameters = "user_id=" + userId +"&following_id=" + followingId + "&key=AoD93128Jd73jKH31je3";
    String UrlString = ServerSettings._Server + ServerSettings._follow;
    try {
      return Connect.connectToServer(UrlString, parameters);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  public static String unfollowUser(int userId, int followingId) {
    String parameters = "user_id=" + userId +"&following_id=" + followingId + "&key=AoD93128Jd73jKH31je3";
    String UrlString = ServerSettings._Server + ServerSettings._unfollow;
    try {
      return Connect.connectToServer(UrlString, parameters);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  public static String searchTag(String text) {
    String parameters = "text=" + text;
    String UrlString = ServerSettings._Server + ServerSettings._searchTag;
    try {
      return Connect.connectToServer(UrlString, parameters);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  public static void flagComment(int commentId) {
    String parameters = "comment_id=" + commentId;
    String UrlString = ServerSettings._Server + ServerSettings._flagComment;
    try {
      response = Connect.connectToServer(UrlString, parameters);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void flagPost(int postId) {
    String parameters = "post_id=" + postId;
    String UrlString = ServerSettings._Server + ServerSettings._flagPost;
    try {
      response = Connect.connectToServer(UrlString, parameters);
    } catch (Exception e) {
      e.printStackTrace();
      Log.e("Giggle", e.toString());
    }
  }

  public static void changeUserProfilePicture(int userId,String image) {
    String UrlString = ServerSettings._Server + ServerSettings._changeUserProfilePicture;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
    nameValuePairs.add(new BasicNameValuePair("user_id", String.valueOf(userId)));
    nameValuePairs.add(new BasicNameValuePair("image", image));
    HttpClient httpclient = new DefaultHttpClient();
    HttpPost httppost = new HttpPost(UrlString);
    try {
      httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    try {
      httpclient.execute(httppost);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void addDeviceId(int userId, String deviceId) {
    String parameters = "id=" + userId + "&device_id=" + deviceId;
    String UrlString = ServerSettings._Server + ServerSettings._addDeviceId;
    try {
      response = Connect.connectToServer(UrlString, parameters);
    } catch (Exception e) {
      e.printStackTrace();
      Log.e("Giggle", e.toString());
    }
  }

}
