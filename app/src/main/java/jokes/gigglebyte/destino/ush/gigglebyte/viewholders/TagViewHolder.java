package jokes.gigglebyte.destino.ush.gigglebyte.viewholders;

import android.app.Activity;
import android.widget.TextView;
import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Tag;

public class TagViewHolder {

  public TextView tagName;
  public TextView numberOfPosts;

  public void setTagData(Activity activity, Tag tag) {
    tagName.setText(tag.getTagText());
    String numberOfPostsText = tag.getNumberOfPosts() + " " + (tag.getNumberOfPosts() == 1? activity.getResources().getString(R.string.post) : activity.getResources().getString(R.string.posts));
    numberOfPosts.setText(numberOfPostsText);
  }

}
