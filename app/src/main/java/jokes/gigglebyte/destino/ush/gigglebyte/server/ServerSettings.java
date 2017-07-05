package jokes.gigglebyte.destino.ush.gigglebyte.server;

public class ServerSettings {

  public static String _Server = "http://creatureislandgame.com/Gigglebyte";

  static String _newUser = "/user_new.php";

  /* Change User Details */
  static String _changeUserName = "/user_change_name.php";
  static String _changeUserDescription = "/user_change_description.php";
  static String _changeUserProfilePicture = "/user_change_profile_picture.php";

  /* Search */
  static String _searchUser = "/search_user.php";
  static String _searchTag = "/search_tag.php";

  /* Read Data */
  static String _getPostForId = "/get_post.php";
  static String _getNewPosts = "/get_new_posts.php";
  static String _getHotPosts = "/get_hot_posts.php";
  static String _getFavoritePosts = "/get_favorite_posts.php";
  static String _getComments = "/get_comments_for_post.php";
  static String _getUserDetails = "/user_view_profile.php";
  static String _getUsersPosts = "/get_users_posts.php";
  static String _getFeed = "/get_feed_v2.php";
  static String _getNotifications = "/get_notifications.php";

  static String _getAllTags = "/get_all_tags.php";
  static String _getTagPosts = "/get_tag_posts.php";

  static String _getFollowing = "/get_following.php";
  static String _getFollowers = "/get_followers.php";
  static String _follow = "/user_follow.php";
  static String _unfollow = "/user_unfollow.php";

  /* Post Data */
  static String _postImagePost = "/post_image_post.php";
  static String _postTextPost = "/post_text_post.php";
  static String _postComment = "/comment.php";
  static String _postLike = "/post_like.php";
  static String _postUnlike = "/post_unlike.php";
  static String _commentLike = "/comment_like.php";
  static String _commentUnlike = "/comment_unlike.php";
  static String _commentMention = "/comment_mention.php";

  /* Flagging and deleting */
  static String _flagComment = "/comment_flag.php";
  static String _flagPost = "/post_flag.php";
  static String _deleteComment = "/comment_delete.php";
  static String _deletePost = "/post_delete.php";

  /* Edit */
  static String _editComment = "/comment_edit.php";
  static String _editTextPost = "/post_text_edit.php";
  static String _editTitlePost = "/post_title_edit.php";

  /* Push Notifications */
  static String _addDeviceId ="/user_add_device_id.php";

}
