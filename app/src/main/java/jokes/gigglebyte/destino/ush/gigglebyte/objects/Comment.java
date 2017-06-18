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
  private int userId;
  private String userName;
  private Bitmap userPicture;

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public int getLikes() {
    return likes;
  }

  public void setLikes(int likes) {
    this.likes = likes;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public Bitmap getUserPicture() {
    return userPicture;
  }

  public void setUserPicture(Bitmap userPicture) {
    this.userPicture = userPicture;
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
    if (getUserPicture() == null) {
      if (MainActivity.cachedProfilePictures.containsKey(userId)) {
        setUserPicture(MainActivity.cachedProfilePictures.get(userId));
        adapter.notifyDataSetChanged();
      } else {
        new ImageLoadTask(adapter, activity, progressBar, imageView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
      }
    }else {
      imageView.setImageBitmap(getUserPicture());
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
      if (userId == UserHelper.getUserDetails(activity).getId()) {
        return ImageHelper.getProfilePicture(userId);
      } else {
        try {
          URL url = new URL(_Server + "/Images/" + userId + "/Profile_Pictures/profile.jpg");
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
        setUserPicture(ret);
        imageView.setImageBitmap(ret);
        MainActivity.cachedProfilePictures.put(userId, ret);
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
