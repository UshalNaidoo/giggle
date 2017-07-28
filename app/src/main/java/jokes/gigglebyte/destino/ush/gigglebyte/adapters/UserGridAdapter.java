package jokes.gigglebyte.destino.ush.gigglebyte.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.PopulateViewHolderHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.enums.FromScreen;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Post;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.PostInfoViewHolder;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.UserGridViewHolder;

public class UserGridAdapter extends BaseAdapter {

  private Activity activity;
  private List<User> users = new ArrayList<>();
  private boolean userList;
  private Post infoPost;
  private FromScreen fromScreen;

  public UserGridAdapter(Activity activity, List<User> results, boolean userList, Post infoPost, FromScreen fromScreen) {
    this.activity = activity;
    this.users = results;
    this.fromScreen = fromScreen;
    this.userList = userList;
    this.infoPost = infoPost;
  }

  @Override
  public int getCount() {
    if (userList && users.size() == 0) {
      return 1;
    }
    return users.size();
  }

  @Override
  public Object getItem(int arg0) {
    if (userList && users.size() == 0) {
      return null;
    }
    return users.get(arg0);
  }

  @Override
  public long getItemId(int arg0) {
    return arg0;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {

    LayoutInflater inflater = activity.getLayoutInflater();
    if (userList && users.size() == 0) {
      final PostInfoViewHolder holder = new PostInfoViewHolder();
      convertView = inflater.inflate(R.layout.post_info_item, parent, false);
      PopulateViewHolderHelper.populatePostInfoViewHolder(convertView, holder);
      convertView.setTag(holder);
      holder.setPostData(activity, infoPost, fromScreen);
    }
    else {
      UserGridViewHolder holder = new UserGridViewHolder();
      convertView = inflater.inflate(R.layout.grid_user_item, parent, false);
      PopulateViewHolderHelper.populateUserGridViewHolder(convertView, holder);
      TextView numberOfFollowing = (TextView) convertView.findViewById(R.id.numberOfFollowing);
      TextView numberOfPosts = (TextView) convertView.findViewById(R.id.numberOfPosts);
      convertView.setTag(holder);
      User user = users.get(position);
      holder.setUserData(activity, user, null);
      String numberOfFollowingSuffix = user.getNumberOfFollowers() == 1 ? activity.getResources().getString(R.string.follower) : activity.getResources().getString(R.string.followers);
      String numberOfPostsSuffix = user.getNumberOfPosts() == 1 ? activity.getResources().getString(R.string.post) : activity.getResources().getString(R.string.posts);
      numberOfFollowing.setText(user.getNumberOfFollowers() + " " + numberOfFollowingSuffix);
      numberOfPosts.setText(user.getNumberOfPosts() + " " + numberOfPostsSuffix);
    }
    return convertView;
  }

}