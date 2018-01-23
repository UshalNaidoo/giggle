package jokes.gigglebyte.destino.ush.gigglebyte.viewholders;


import android.app.Activity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import jokes.gigglebyte.destino.ush.gigglebyte.enums.FromScreen;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Post;

public class PostImageViewHolder extends PostViewHolder {

  public ProgressBar imageProgressBar;
  public ImageView postImage;
  public TextView title;


  public void setImagePostData(final Activity activity, BaseAdapter adapter, View convertView,
                               final Post post, FromScreen from) {
    setPostData(activity, convertView, post, from);

    if (post.getPostTitle().equals("")) {
      title.setVisibility(View.GONE);
    } else {
      title.setText(post.getPostTitle());
    }
    //get image from url like profile
    if (post.getPostPicture() != null) {
      imageProgressBar.setVisibility(View.GONE);
      postImage.setImageBitmap(post.getPostPicture());
    } else {
      post.loadImagePost(activity, post.getUser()
          .getId(), post.getImageId(), adapter, imageProgressBar, postImage);
    }
  }

}