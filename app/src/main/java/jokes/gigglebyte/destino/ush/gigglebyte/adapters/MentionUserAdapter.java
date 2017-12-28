package jokes.gigglebyte.destino.ush.gigglebyte.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.PopulateViewHolderHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.MentionViewHolder;

public class MentionUserAdapter extends ArrayAdapter<User> {

  private static List<User> users;
  private LayoutInflater inflater;
  private Activity activity;

  public MentionUserAdapter(Activity activity, int viewId, List<User> results) {
    super(activity, viewId);
    users = results;
    inflater = LayoutInflater.from(activity);
    this.activity = activity;
  }

  @Override
  public long getItemId(int arg0) {
    return arg0;
  }

  public View getView(final int position, View convertView, ViewGroup parent) {
    View row = convertView;
    if (position < users.size()) {
      final MentionViewHolder holder = new MentionViewHolder();
      row = inflater.inflate(R.layout.mention_user_item, parent, false);
      PopulateViewHolderHelper.populateMentionHolder(row, holder);
      row.setTag(holder);
      holder.setUserData(activity, users.get(position), null);
    }
    return row;
  }

  @Override
  public Filter getFilter() {
    return nameFilter;
  }

  private Filter nameFilter = new Filter() {
    @Override
    public String convertResultToString(Object resultValue) {
      return "@" + ((User)resultValue).getName();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
      FilterResults results = new FilterResults();

      if (constraint != null && constraint.length() > 1) {
        ArrayList<User> suggestions = new ArrayList<>();

        for (User user : users) {
          if (user.getName().toLowerCase().startsWith(constraint.toString().toLowerCase().substring(1))) {
            suggestions.add(user);
          }
        }

        results.values = suggestions;
        results.count = suggestions.size();
      }

      return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
      clear();
      if (results != null && results.count > 0) {
        addAll((ArrayList<User>) results.values);
      }
      notifyDataSetChanged();
    }
  };

}