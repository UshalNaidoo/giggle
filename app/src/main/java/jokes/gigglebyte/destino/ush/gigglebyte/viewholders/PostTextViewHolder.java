package jokes.gigglebyte.destino.ush.gigglebyte.viewholders;


import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import jokes.gigglebyte.destino.ush.gigglebyte.activities.CommentActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.activities.MainActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.enums.FromScreen;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Post;

public class PostTextViewHolder extends PostViewHolder {

  public TextView postText;

  public void setTextPostData(final Activity activity, View convertView, final Post post, final FromScreen from) {
    setPostData(activity, convertView, post, from);

    postText.setText(post.getPostText());

    postText.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (from != FromScreen.COMMENTS) {
          Intent myIntent = new Intent(activity, CommentActivity.class);
          MainActivity.selectedPost = post;
          myIntent.putExtra("postId", post.getPostId());
          myIntent.putExtra("posterId", post.getUser().getId());
          activity.startActivity(myIntent);
        }
      }
    });

    postText.setOnClickListener(doubleClickListener());

    postText.setOnLongClickListener(longClickListener(post.getUser().getId()));
  }

}