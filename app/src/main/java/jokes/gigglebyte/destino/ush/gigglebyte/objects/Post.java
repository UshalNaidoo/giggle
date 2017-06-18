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
import java.util.List;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.activities.MainActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.ImageHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.UserHelper;

public class Post {

  private int postId;
  private String postText;
  private String timeSincePost;
  private int likes;
  private boolean userLike;
  private boolean userFavorite;
  private PostType type;
  private String postTitle;
  private int userId;
  private String userName;
  private Bitmap userPicture;
  private Bitmap postImage;
  private ImageLoadTask profileTask;
  private ImageLoadTask postTask;
  private boolean loadingProfileImage = false;
  private boolean loadingPostImage = false;
  private int commentCount;
  private List<String> tags;
  private int imageId;
  private boolean finishLoadingProfilePicture;

  public int getImageId() {
    return imageId;
  }

  public void setImageId(int imageId) {
    this.imageId = imageId;
  }

  public int getPostId() {
    return postId;
  }

  public void setPostId(int postId) {
    this.postId = postId;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public String getPostText() {
    return postText;
  }

  public void setPostText(String postText) {
    this.postText = postText;
  }

  public String getTimeSincePost() {
    return timeSincePost;
  }

  public void setTimeSincePost(String timeSincePost) {
    this.timeSincePost = timeSincePost;
  }

  public int getLikes() {
    return likes;
  }

  public void setLikes(int likes) {
    this.likes = likes;
  }

  public PostType getType() {
    return type;
  }

  public void setType(PostType type) {
    this.type = type;
  }

  public String getPostTitle() {
    return postTitle;
  }

  public void setPostTitle(String postTitle) {
    this.postTitle = postTitle;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public Bitmap getPostPicture() {
    return postImage;
  }

  public void setPostPicture(Bitmap postImage) {
    this.postImage = postImage;
  }

  public Bitmap getUserPicture() {
    return userPicture;
  }

  public void setUserPicture(Bitmap userPicture) {
    this.userPicture = userPicture;
  }

  public boolean isUserLike() {
    return userLike;
  }

  public void setUserLike(boolean userLike) {
    this.userLike = userLike;
  }

  public boolean isUserFavorite() {
    return userFavorite;
  }

  public void setUserFavorite(boolean userFavorite) {
    this.userFavorite = userFavorite;
  }

  public void loadProfileImage(Activity activity, int userId, BaseAdapter adapter,
                               ProgressBar progressBar,
                               ImageView imageView) {
    if (!loadingProfileImage) {
      if (MainActivity.cachedProfilePictures.containsKey(userId)) {
        finishLoadingProfilePicture = true;
        setUserPicture(MainActivity.cachedProfilePictures.get(userId));
        if (adapter != null) {
          adapter.notifyDataSetChanged();
        }
      }
      else {
        setProfileLoadTask(new ImageLoadTask(activity, userId, adapter, progressBar, imageView, -1));
        getProfileLoadTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
      }
      loadingProfileImage = true;
    } else {
      progressBar.setVisibility(View.GONE);
      finishLoadingProfilePicture = true;
      if (getUserPicture() == null) {
        imageView.setImageResource(R.drawable.nobody_m);
      } else {
        imageView.setImageBitmap(getUserPicture());
      }
    }
  }

  public void loadImagePost(Activity activity, int userId, int imageId, BaseAdapter adapter, ProgressBar progressBar, ImageView imageView) {
    if (!loadingPostImage) {
      progressBar.setVisibility(View.VISIBLE);
      setPostLoadTask(new ImageLoadTask(activity, userId, adapter, progressBar, imageView, imageId));
      getPostLoadTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
      loadingPostImage = true;
    }
  }

  private void setProfileLoadTask(ImageLoadTask loadTask) {
    this.profileTask = loadTask;
  }

  private ImageLoadTask getProfileLoadTask() {
    return this.profileTask;
  }

  private void setPostLoadTask(ImageLoadTask loadTask) {
    this.postTask = loadTask;
  }

  private ImageLoadTask getPostLoadTask() {
    return this.postTask;
  }

  public void cancelLoadingImages() {
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public int getCommentCount() {
    return commentCount;
  }

  public void setCommentCount(int commentCount) {
    this.commentCount = commentCount;
  }

  public boolean isFinishLoadingProfilePicture() {
    return finishLoadingProfilePicture;
  }

  private class ImageLoadTask extends AsyncTask<String, String, Bitmap> {

    private int userId;
    private Activity activity;
    private BaseAdapter adapter;
    private ProgressBar progressBar;
    private ImageView imageView;
    private int imageId;

    ImageLoadTask(Activity activity, int userId, BaseAdapter adapter,
        ProgressBar progressBar,
        ImageView imageView, int imageId) {
      this.progressBar = progressBar;
      this.imageView = imageView;
      this.userId = userId;
      this.activity = activity;
      this.adapter = adapter;
      this.imageId = imageId;
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      progressBar.setVisibility(View.VISIBLE);
      imageView.setVisibility(View.INVISIBLE);
    }

    protected Bitmap doInBackground(String... param) {
      Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
      if (this.userId == UserHelper.getUserDetails(activity).getId() && this.imageId == -1) {
        return ImageHelper.getProfilePicture(this.userId);
      } else {
        try {
          URL url;
          if (this.imageId == -1) {
            url = new URL(_Server + "/Images/" + this.userId
                + "/Profile_Pictures/profile.jpg");
          }
          else {
            url = new URL(_Server + "/Images/" + this.userId
                + "/Image_Posts/post"+ this.imageId +".jpg");
          }
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
        imageView.setImageBitmap(ret);
        if (this.imageId == -1) {
          setUserPicture(ret);
          MainActivity.cachedProfilePictures.put(userId, ret);
        }
        else {
          setPostPicture(ret);
        }
      } else {
        imageView.setImageResource(R.drawable.nobody_m);
      }
      finishLoadingProfilePicture = true;
      imageView.setVisibility(View.VISIBLE);
      if (adapter != null) {
        adapter.notifyDataSetChanged();
      }
    }
  }
}
