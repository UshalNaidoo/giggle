package jokes.gigglebyte.destino.ush.gigglebyte.adapters;

import android.app.Activity;
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
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.UserGridViewHolder;

public class UserGridAdapter extends BaseAdapter {

  private Activity activity;
  private List<User> users = new ArrayList<>();

  public UserGridAdapter(Activity activity, List<User> results) {
    this.activity = activity;
    this.users = results;
  }

  @Override
  public int getCount() {
    return users.size();
  }

  @Override
  public Object getItem(int arg0) {
    return users.get(arg0);
  }

  @Override
  public long getItemId(int arg0) {
    return arg0;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    UserGridViewHolder holder;

    LayoutInflater inflater = activity.getLayoutInflater();
    convertView = inflater.inflate(R.layout.grid_user_item, parent, false);
    holder = new UserGridViewHolder();
    holder.userName = (TextView) convertView.findViewById(R.id.userName);
    holder.profileImage = (ImageView) convertView.findViewById(R.id.profileImage);
    holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
    holder.followButton = (ImageView) convertView.findViewById(R.id.followButton);
    TextView numberOfFollowing = (TextView) convertView.findViewById(R.id.numberOfFollowing);
    TextView numberOfPosts = (TextView) convertView.findViewById(R.id.numberOfPosts);
    convertView.setTag(holder);

    User user = users.get(position);
    holder.setUserData(activity, user, null);

    String numberOfFollowingSuffix = user.getNumberOfFollowers() == 1 ? activity.getResources().getString(R.string.follower) : activity.getResources().getString(R.string.followers);
    String numberOfPostsSuffix = user.getNumberOfPosts() == 1 ? activity.getResources().getString(R.string.post) : activity.getResources().getString(R.string.posts);
    numberOfFollowing.setText(user.getNumberOfFollowers() + " " + numberOfFollowingSuffix);
    numberOfPosts.setText(user.getNumberOfPosts() + " " + numberOfPostsSuffix);

    return convertView;
  }

}