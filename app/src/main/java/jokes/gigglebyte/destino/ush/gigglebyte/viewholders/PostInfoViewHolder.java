package jokes.gigglebyte.destino.ush.gigglebyte.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import jokes.gigglebyte.destino.ush.gigglebyte.activities.MainActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.enums.FromScreen;
import jokes.gigglebyte.destino.ush.gigglebyte.fragments.Fragment_Posts;
import jokes.gigglebyte.destino.ush.gigglebyte.fragments.Fragment_Search;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Post;

public class PostInfoViewHolder extends PostViewHolder {

  public ImageView postImage;
  public TextView title;


  public void setPostData(final Post post, final FromScreen from) {
    title.setText(post.getPostTitle());
    postImage.setImageBitmap(post.getPostPicture());
    title.setText(post.getPostTitle());

    postImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (FromScreen.FEED.equals(from)) {
          MainActivity.changeTab(3);
          Fragment_Search.switchTab(0);
        }
        if (FromScreen.FAVOURITE.equals(from)) {
          Fragment_Posts.switchTab(0);
        }
      }
    });

  }

}