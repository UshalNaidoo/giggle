package jokes.gigglebyte.destino.ush.gigglebyte.objects;

public enum PostType {
  TEXT_POST("TextPost"),
  IMAGE_POST("ImagePost"),
  FOLLOWING_NOTIFICATION("FollowingNotification"),
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