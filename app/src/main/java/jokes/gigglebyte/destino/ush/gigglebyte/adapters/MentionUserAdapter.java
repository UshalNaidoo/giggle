package jokes.gigglebyte.destino.ush.gigglebyte.adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.PopulateViewHolderHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.enums.OpenScreen;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.MentionViewHolder;

public class MentionUserAdapter extends ArrayAdapter<User> {

  private static List<User> users;
  private LayoutInflater mInflater;
  private Activity activity;

  public MentionUserAdapter(Activity activity, int viewId, List<User> results) {
    super(activity, viewId);
    users = results;
    mInflater = LayoutInflater.from(activity);
    this.activity = activity;
  }

  @Override
  public int getCount() {
    return users.size();
  }

  @Override
  public long getItemId(int arg0) {
    return arg0;
  }

  public View getView(final int position, View convertView, ViewGroup parent) {
    View row;
    final MentionViewHolder holder = new MentionViewHolder();
    row = mInflater.inflate(R.layout.comment_item, parent, false);
    PopulateViewHolderHelper.populateMentionHolder(row, holder);
    row.setTag(holder);
    holder.setUserData(activity, users.get(position), OpenScreen.PROFILE);
    return row;
  }

  @Override
  public Filter getFilter() {
    return nameFilter;
  }


  private Filter nameFilter = new Filter() {
    @Override
    public String convertResultToString(Object resultValue) {
      return ((User)resultValue).getName();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
      FilterResults results = new FilterResults();

      if (constraint != null) {
        ArrayList<User> suggestions = new ArrayList<>();
        for (User customer : users) {
          // Note: change the "contains" to "startsWith" if you only want starting matches
          if (customer.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
            suggestions.add(customer);
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
        // we have filtered results
        addAll((ArrayList<User>) results.values);
      } else {
        // no filter, add entire original list back in
        addAll(users);
      }
      notifyDataSetChanged();
    }
  };

}