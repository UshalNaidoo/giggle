package jokes.gigglebyte.destino.ush.gigglebyte.objects;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.ImageHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.UserHelper;

public class User {

  private int id;
  private String name;
  private String description;
  private Bitmap profile_pic;

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

  private class ImageLoadTask extends AsyncTask<String, String, Bitmap> {

    private Activity activity;
    private ImageView imageView;

    public ImageLoadTask(Activity activity, ImageView view) {
      this.activity = activity;
      this.imageView = view;
    }

    protected Bitmap doInBackground(String... param) {
      if (getId() == UserHelper.getUserDetails(activity).getId()) {
        return ImageHelper.getProfilePicture(getId());
      } else {
        try {
          URL url = new URL(
              "http://creatureislandgame.com/Gigglebyte/Images/" + getId() + "/Profile_Pictures/profile.jpg");
          InputStream inputStream = url.openConnection().getInputStream();
          return BitmapFactory.decodeStream(inputStream);
        } catch (MalformedURLException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
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
