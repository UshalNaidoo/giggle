package jokes.gigglebyte.destino.ush.gigglebyte.viewholders;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import jokes.gigglebyte.destino.ush.gigglebyte.activities.FollowersActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.activities.MainActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.enums.FromScreen;
import jokes.gigglebyte.destino.ush.gigglebyte.fragments.Tabs_Posts;
import jokes.gigglebyte.destino.ush.gigglebyte.fragments.Tabs_Search;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Post;

public class PostInfoViewHolder extends PostViewHolder {

  public ImageView postImage;
  public TextView title;


  public void setPostData(final Activity activity, final Post post, final FromScreen from) {
    title.setText(post.getPostTitle());
    postImage.setImageBitmap(post.getPostPicture());
    title.setText(post.getPostTitle());

    postImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (FromScreen.FEED.equals(from)) {
          MainActivity.changeTab(3);
          Tabs_Search.switchTab(0);
        }
        if (FromScreen.FAVOURITE.equals(from)) {
          Tabs_Posts.switchTab(0);
        }
        if (FromScreen.USER.equals(from)) {
          MainActivity.popUpAddText(activity);
        }
        if (FromScreen.FOLLOWING.equals(from)) {
          FollowersActivity.closeActivity();
          MainActivity.changeTab(3);
          Tabs_Search.switchTab(0);
        }
        if (FromScreen.FOLLOWERS.equals(from)) {
          MainActivity.popUpAddText(activity);
        }
      }
    });

  }

}