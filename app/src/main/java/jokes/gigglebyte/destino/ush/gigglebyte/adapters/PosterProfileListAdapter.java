package jokes.gigglebyte.destino.ush.gigglebyte.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.PopulateViewHolderHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.enums.FromScreen;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Post;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.PostType;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.PostImageViewHolder;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.PostInfoViewHolder;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.PostTextViewHolder;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.UserListViewHolder;

public class PosterProfileListAdapter extends BaseAdapter {

  private static List<Post> posts;
  private static User poster;
  private LayoutInflater mInflater;
  private Activity activity;
  private FromScreen fromScreen;

  public PosterProfileListAdapter(Activity activity, List<Post> results, User user, FromScreen fromScreen) {
    posts = results;
    poster = user;
    this.fromScreen = fromScreen;
    mInflater = LayoutInflater.from(activity);
    this.activity = activity;
  }

  @Override
  public int getCount() {
    return posts.size() + 1;
  }

  @Override
  public Object getItem(int arg0) {
    if (arg0 == 0) {
      return poster;
    }
    return posts.get(arg0);
  }

  @Override
  public long getItemId(int arg0) {
    return arg0;
  }

  public View getView(final int position, View convertView, ViewGroup parent) {
    if (position == 0) {
      UserListViewHolder holder = new UserListViewHolder();
      convertView = mInflater.inflate(R.layout.poster_header, parent, false);
      PopulateViewHolderHelper.populateUserListViewHolder(convertView, holder);
      convertView.setTag(holder);
      holder.setUserData(activity, poster);
    }
    else {
      final int pos = position - 1;
      final Post post = posts.get(pos);
      if (post.getType() == PostType.TEXT_POST) {
        final PostTextViewHolder holder = new PostTextViewHolder();
        convertView = mInflater.inflate(R.layout.post_text_item, parent, false);
        PopulateViewHolderHelper.populatePostTextViewHolder(convertView, holder);
        convertView.setTag(holder);
        holder.setTextPostData(activity, convertView, post, fromScreen);
      }
      else if (post.getType() == PostType.IMAGE_POST) {
        final PostImageViewHolder holder = new PostImageViewHolder();
        convertView = mInflater.inflate(R.layout.post_image_item, parent, false);
        PopulateViewHolderHelper.populatePostImageViewHolder(convertView, holder);
        convertView.setTag(holder);
        holder.setImagePostData(activity, this, convertView, post, fromScreen);
      }
      else if (post.getType() == PostType.INFO_POST) {
        final PostInfoViewHolder holder = new PostInfoViewHolder();
        convertView = mInflater.inflate(R.layout.post_info_item, parent, false);
        PopulateViewHolderHelper.populatePostInfoViewHolder(convertView, holder);
        convertView.setTag(holder);
        holder.setPostData(activity, post, fromScreen);
      }
    }
    return convertView;
  }

}