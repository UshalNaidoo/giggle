package jokes.gigglebyte.destino.ush.gigglebyte.objects;

import static jokes.gigglebyte.destino.ush.gigglebyte.server.ServerSettings._Server;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.activities.MainActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.ImageHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.UserHelper;

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

  public void loadImage(BaseAdapter adapter, Activity activity, ProgressBar progressBar,
                        ImageView imageView) {
    if (user.getProfile_pic() == null) {
      if (MainActivity.cachedProfilePictures.containsKey(user.getId())) {
        user.setProfile_pic(MainActivity.cachedProfilePictures.get(user.getId()));
        adapter.notifyDataSetChanged();
      } else {
        new ImageLoadTask(adapter, activity, progressBar, imageView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
      }
    }else {
      imageView.setImageBitmap(user.getProfile_pic());
    }
  }

  private class ImageLoadTask extends AsyncTask<String, String, Bitmap> {

    private Activity activity;
    private ProgressBar progressBar;
    private ImageView imageView;
    private BaseAdapter adapter;

    ImageLoadTask(BaseAdapter adapter, Activity activity, ProgressBar progressBar,
        ImageView imageView) {
      this.progressBar = progressBar;
      this.activity = activity;
      this.imageView = imageView;
      this.adapter = adapter;
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      progressBar.setVisibility(View.VISIBLE);
      imageView.setVisibility(View.INVISIBLE);
    }

    protected Bitmap doInBackground(String... param) {
      Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
      if (user.getId() == UserHelper.getUserDetails(activity).getId()) {
        return ImageHelper.getProfilePicture(user.getId());
      } else {
        try {
          URL url = new URL(_Server + "/Images/" + user.getId() + "/Profile_Pictures/profile.jpg");
          InputStream inputStream = url.openConnection().getInputStream();
          return BitmapFactory.decodeStream(inputStream);
        }
        catch (IOException e) {
          e.printStackTrace();
        }
      }
      return null;
    }

    protected void onPostExecute(Bitmap ret) {
      if (ret != null) {
        user.setProfile_pic(ret);
        imageView.setImageBitmap(ret);
        MainActivity.cachedProfilePictures.put(user.getId(), ret);
      } else {
        imageView.setImageResource(R.drawable.nobody_m);
      }
      imageView.setVisibility(View.VISIBLE);
      progressBar.setVisibility(View.INVISIBLE);
      if (adapter != null) {
        adapter.notifyDataSetChanged();
      }
    }
  }
}
