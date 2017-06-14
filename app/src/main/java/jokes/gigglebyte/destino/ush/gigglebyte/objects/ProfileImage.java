package jokes.gigglebyte.destino.ush.gigglebyte.objects;

import android.graphics.Bitmap;

public class ProfileImage {

  private User user;
  private Bitmap image;

  public Bitmap getImage() {
    return image;
  }

  public void setImage(Bitmap image) {
    this.image = image;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
