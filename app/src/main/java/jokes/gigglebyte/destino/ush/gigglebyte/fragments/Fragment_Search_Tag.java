package jokes.gigglebyte.destino.ush.gigglebyte.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.activities.MainActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.activities.TagActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.adapters.TagListAdapter;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.JsonParser;
import jokes.gigglebyte.destino.ush.gigglebyte.interfaces.FragmentLifecycle;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Tag;
import jokes.gigglebyte.destino.ush.gigglebyte.server.ConnectToServer;

public class Fragment_Search_Tag extends Fragment implements FragmentLifecycle {

  private Activity activity;
  private EditText searchField;
  private TagListAdapter adapter;
  private ListView listView;

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    activity = this.getActivity();
    View rootView = inflater.inflate(R.layout.fragment_search_tag, container, false);

    Button buttonSearch = (Button) rootView.findViewById(R.id.buttonSearch);
    searchField = (EditText) rootView.findViewById(R.id.search);

    new SearchForTags("", false).execute();

    buttonSearch.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String searchFor = searchField.getText().toString().trim();
        if (searchFor.startsWith("#")) {
          searchFor = searchFor.substring(1);
        }
        new SearchForTags(searchFor, true).execute();
      }
    });

    listView = (ListView) rootView.findViewById(R.id.tag_list);
    listView.setOnScrollListener(new AbsListView.OnScrollListener() {
      @Override
      public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == 0) {
          adapter.notifyDataSetChanged();
        }
      }

      @Override
      public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                           int totalItemCount) {
      }
    });

    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View v,
                              int position, long id) {
        Tag tag = (Tag) parent.getItemAtPosition(position);
        Intent intent = new Intent(activity, TagActivity.class);
        intent.putExtra("tag", tag.getTagText());
        startActivity(intent);
      }
    });


    final TextWatcher watcher = new TextWatcher() {
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int aft) {
      }

      @Override
      public void afterTextChanged(Editable s) {
        String searchFor = searchField.getText().toString().trim();
        if (searchFor.startsWith("#")) {
          searchFor = searchFor.substring(1);
        }
        if (s.length() > 0) {
          new SearchForTags(searchFor, true).execute();
          searchField.removeTextChangedListener(this);
          new SearchForTags(searchFor, true).execute();
          searchField.addTextChangedListener(this);
        }
        else if (s.length() == 0) {
          searchField.removeTextChangedListener(this);
          new SearchForTags("", true).execute();
          searchField.addTextChangedListener(this);
        }
      }
    };

    searchField.addTextChangedListener(watcher);

    return rootView;
  }

  @Override
  public void onPauseFragment() {
  }

  @Override
  public void onResumeFragment() {
  }

  private class SearchForTags extends AsyncTask<Integer, Integer, List<Tag>> {

    String searchFor;
    boolean buttonClicked;

    SearchForTags(String searchFor, boolean buttonClicked) {
      this.searchFor = searchFor;
      this.buttonClicked = buttonClicked;
    }

    @Override
    protected List<Tag> doInBackground(Integer... params) {
      if (!this.buttonClicked && !MainActivity.loadedTags.isEmpty()) {
        return MainActivity.loadedTags;
      } else {
        return JsonParser.GetTagsFromSearch(
            "{\"tags\":" + ConnectToServer.searchTag(searchFor) + "}");
      }
    }

    @Override
    protected void onPostExecute(final List<Tag> result) {
      adapter = new TagListAdapter(activity, result);
      listView.setAdapter(adapter);
      MainActivity.loadedTags = result;
    }

    @Override
    protected void onPreExecute() {
    }
  }

}
