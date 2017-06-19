package jokes.gigglebyte.destino.ush.gigglebyte.viewholders;

import android.widget.TextView;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Tag;

public class TagViewHolder {

  public TextView tagName;
  public TextView numberOfPosts;

  public void setData(Tag tag) {
    tagName.setText(tag.getTagText());
    String numberOfPostsText = tag.getNumberOfPosts() + " Posts";
    numberOfPosts.setText(numberOfPostsText);
  }

}
