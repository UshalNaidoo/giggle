package jokes.gigglebyte.destino.ush.gigglebyte.objects;

public class Comment {

  private boolean userLike;
  private int commentId;
  private String commentText;
  private int likes;
  private User user;

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public int getLikes() {
    return likes;
  }

  public void setLikes(int likes) {
    this.likes = likes;
  }

  public int getCommentId() {
    return commentId;
  }

  public void setCommentId(int commentId) {
    this.commentId = commentId;
  }

  public String getCommentText() {
    return commentText;
  }

  public void setCommentText(String commentText) {
    this.commentText = commentText;
  }

  public boolean isUserLike() {
    return userLike;
  }

  public void setUserLike(boolean userLike) {
    this.userLike = userLike;
  }

}
