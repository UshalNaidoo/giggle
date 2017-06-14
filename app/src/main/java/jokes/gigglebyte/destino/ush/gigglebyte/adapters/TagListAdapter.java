package jokes.gigglebyte.destino.ush.gigglebyte.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Tag;
import jokes.gigglebyte.destino.ush.gigglebyte.viewholders.TagViewHolder;

public class TagListAdapter extends BaseAdapter {

  private List<Tag> tags;
  private LayoutInflater mInflater;
  private Activity activity;

  public TagListAdapter(Activity activity, List<Tag> results) {
    this.activity = activity;
    tags = results;
    mInflater = LayoutInflater.from(activity);
  }

  @Override
  public int getCount() {
    return tags.size();
  }

  @Override
  public Object getItem(int arg0) {
    return tags.get(arg0);
  }

  @Override
  public long getItemId(int arg0) {
    return arg0;
  }

  public View getView(final int position, View convertView, ViewGroup parent) {
    final TagViewHolder holder;
    convertView = mInflater.inflate(R.layout.tag_item, parent, false);
    holder = new TagViewHolder();
    holder.tagName = (TextView) convertView.findViewById(R.id.tag);
    holder.numberOfPosts = (TextView) convertView.findViewById(R.id.numberOfPosts);
    convertView.setTag(holder);

    Tag tag = tags.get(position);

    holder.tagName.setText(tag.getTagText());
    holder.numberOfPosts.setText(tag.getNumberOfPosts() + " Posts");

    return convertView;
  }
}