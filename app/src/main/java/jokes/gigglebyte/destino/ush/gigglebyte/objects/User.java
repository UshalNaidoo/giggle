package jokes.gigglebyte.destino.ush.gigglebyte.objects;

import static jokes.gigglebyte.destino.ush.gigglebyte.server.ServerSettings._Server;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.ImageHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.UserHelper;

public class User {

  private int id;
  private String name;
  private String description;
  private List<User> following;
  private List<User> followers;
  private Bitmap profile_pic;

  public User() {

  }

  public User(int id, String name, String description, Bitmap bitmap) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.profile_pic = bitmap;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Bitmap getProfile_pic() {
    return profile_pic;
  }

  public void setProfile_pic(Bitmap profile_pic) {
    this.profile_pic = profile_pic;
  }

  public void loadImage(Activity activity, ImageView view) {
    // HOLD A REFERENCE TO THE ADAPTER
    if (getProfile_pic() == null) {
      new ImageLoadTask(activity, view).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
  }

  public void setFollowing(List<User> following) {
    this.following = following;
  }

  public List<User> getFollowing() {
    return following;
  }

  public void setFollowers(List<User> followers) {
    this.followers = followers;
  }

  public List<User> getFollowers() {
    return followers;
  }

  private class ImageLoadTask extends AsyncTask<String, String, Bitmap> {

    private Activity activity;
    private ImageView imageView;

    ImageLoadTask(Activity activity, ImageView view) {
      this.activity = activity;
      this.imageView = view;
    }

    protected Bitmap doInBackground(String... param) {
      if (getId() == UserHelper.getUsersId(activity)) {
        return ImageHelper.getProfilePicture(getId());
      } else {
        try {
          if(UserHelper.userImages.containsKey(getId())) {
            return UserHelper.userImages.get(getId());
          }else {
            URL url = new URL(_Server + "/Images/" + getId() + "/Profile_Pictures/profile.jpg");
            InputStream inputStream = url.openConnection().getInputStream();
            Bitmap image = BitmapFactory.decodeStream(inputStream);
            UserHelper.userImages.put(getId(), image);
            return image;
          }
        }
        catch (IOException e) {
          UserHelper.userImages.put(getId(), null);
        }
      }
      return null;
    }

    protected void onPostExecute(Bitmap ret) {
      if (ret != null) {
        setProfile_pic(ret);
        imageView.setImageBitmap(ret);
      }
      else {
        imageView.setImageResource(R.drawable.nobody_m);
      }
    }
  }

}
