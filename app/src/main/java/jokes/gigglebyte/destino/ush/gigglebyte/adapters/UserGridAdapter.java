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
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.UserViewHolder;

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
    View row;
    UserViewHolder holder;

    LayoutInflater inflater = activity.getLayoutInflater();
    row = inflater.inflate(R.layout.grid_user_item, null);
    holder = new UserViewHolder();
    holder.userName = (TextView) row.findViewById(R.id.userName);
    holder.profileImage = (ImageView) row.findViewById(R.id.profileImage);
    holder.progressBar = (ProgressBar) row.findViewById(R.id.progressBar);
    row.setTag(holder);

    User user = users.get(position);
    holder.userName.setText(user.getName());

    if (user.getProfile_pic() != null) {
      holder.progressBar.setVisibility(View.INVISIBLE);
      holder.profileImage.setImageBitmap(user.getProfile_pic());
    } else {
      holder.progressBar.setVisibility(View.VISIBLE);
      holder.profileImage.setImageBitmap(null);
    }
    return row;
  }

}