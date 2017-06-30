package jokes.gigglebyte.destino.ush.gigglebyte.objects;

public enum PostType {
  TEXT_POST("TextPost"),
  IMAGE_POST("ImagePost"),
  FOLLOWING_NOTIFICATION("FollowingNotification"),
  LIKE_TEXT_POST_NOTIFICATION("LikeTextPostNotification"),
  LIKE_IMAGE_POST_NOTIFICATION("LikeImagePostNotification"),
  COMMENT_TEXT_POST_NOTIFICATION("CommentTextPostNotification"),
  COMMENT_IMAGE_POST_NOTIFICATION("CommentImagePostNotification"),
  INFO_POST("InfoPost");

  private String stringValue;

  PostType(String toString) {
    stringValue = toString;
  }

  @Override
  public String toString() {
    return stringValue;
  }

}