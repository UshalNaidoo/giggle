package jokes.gigglebyte.destino.ush.gigglebyte.objects;

public enum PostType {
  TEXT_POST("TextPost", 0),
  IMAGE_POST("ImagePost", 1);

  private String stringValue;
  private int intValue;
  private PostType(String toString, int value) {
    stringValue = toString;
    intValue = value;
  }

  @Override
  public String toString() {
    return stringValue;
  }

  public int getIntValue() {
    return intValue;
  }
}