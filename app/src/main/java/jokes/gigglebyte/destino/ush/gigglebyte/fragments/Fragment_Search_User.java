package jokes.gigglebyte.destino.ush.gigglebyte.fragments;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.activities.MainActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.activities.PosterProfileActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.adapters.UserGridAdapter;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.JsonParser;
import jokes.gigglebyte.destino.ush.gigglebyte.interfaces.FragmentLifecycle;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;
import jokes.gigglebyte.destino.ush.gigglebyte.server.ConnectToServer;

public class Fragment_Search_User extends Fragment implements FragmentLifecycle {

  private Activity activity;
  private GridView gridView;
  private EditText searchField;

  @Override
  public View onCreateView(LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    activity = this.getActivity();
    View rootView = inflater.inflate(R.layout.fragment_search_user, container, false);
    Button buttonSearch = (Button) rootView.findViewById(R.id.buttonSearch);
    searchField = (EditText) rootView.findViewById(R.id.search);

    gridView = (GridView) rootView.findViewById(R.id.gridView);
    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View v,
          int position, long id) {
        User selectedUser = (User) parent.getItemAtPosition(position);
        Intent myIntent = new Intent(activity, PosterProfileActivity.class);
        myIntent.putExtra("userId", selectedUser.getId());
        activity.startActivity(myIntent);
      }
    });

    new SearchForUsers("", false).execute();

    buttonSearch.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        new SearchForUsers(searchField.getText().toString(), true).execute();
      }
    });

    return rootView;
  }

  @Override
  public void onPauseFragment() {
  }

  @Override
  public void onResumeFragment() {
  }

  private class SearchForUsers extends AsyncTask<Integer, Integer, List<User>> {

    String searchFor;
    boolean buttonClicked;

    SearchForUsers(String searchFor, boolean buttonClicked) {
      this.searchFor = searchFor;
      this.buttonClicked = buttonClicked;
    }

    @Override
    protected List<User> doInBackground(Integer... params) {
      if (!this.buttonClicked && !MainActivity.loadedUsers.isEmpty()) {
        return MainActivity.loadedUsers;
      }
      else {
        String usersString = ConnectToServer.searchUser(searchFor);
        return JsonParser.GetUsers("{\"users\":" + usersString + "}");
      }
    }

    @Override
    protected void onPostExecute(List<User> users) {
      UserGridAdapter gridAdapter = new UserGridAdapter(activity, users);
      MainActivity.loadedUsers = users;
      gridView.setAdapter(gridAdapter);
    }

    @Override
    protected void onPreExecute() {
      searchField.setText("");
    }
  }

}
