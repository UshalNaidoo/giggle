package jokes.gigglebyte.destino.ush.gigglebyte.fragments;

import static jokes.gigglebyte.destino.ush.gigglebyte.server.ServerSettings._Server;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.activities.PosterProfileActivity;
import jokes.gigglebyte.destino.ush.gigglebyte.adapters.UserGridAdapter;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.ImageHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.JsonParser;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.UserHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.interfaces.FragmentLifecycle;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;
import jokes.gigglebyte.destino.ush.gigglebyte.server.ConnectToServer;

public class Fragment_Search_User extends Fragment implements FragmentLifecycle {

  private Activity activity;
  private GridView gridView;
  private UserGridAdapter gridAdapter;
  private EditText searchField;

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    activity = this.getActivity();
    View rootView = inflater.inflate(R.layout.fragment_search_user, container, false);

    Button buttonSearch = (Button) rootView.findViewById(R.id.buttonSearch);
    searchField = (EditText) rootView.findViewById(R.id.search);
    gridView = (GridView) rootView.findViewById(R.id.gridView);
    buttonSearch.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!searchField.getText().toString().trim().isEmpty()) {
          new SearchForUsers(searchField.getText().toString()).execute();
        }
      }
    });

    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View v,
                              int position, long id) {
        User selectedUser = (User) parent.getItemAtPosition(position);
        Intent myIntent = new Intent(activity, PosterProfileActivity.class);
        myIntent.putExtra("userId", selectedUser.getId());
        activity.startActivity(myIntent);
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

  class SearchForUsers extends AsyncTask<Integer, Integer, List<User>> {

    String searchFor;

    SearchForUsers(String searchFor) {
      this.searchFor = searchFor;
    }

    @Override
    protected List<User> doInBackground(Integer... params) {
      String usersString = ConnectToServer.searchUser(searchFor);
      return JsonParser.GetUsers("{\"users\":" + usersString + "}");
    }

    @Override
    protected void onPostExecute(List<User> users) {
      gridAdapter = new UserGridAdapter(activity, users);
      gridView.setAdapter(gridAdapter);
      for (User user : users) {
        new ImageLoadTask(user, gridAdapter).execute();
      }
    }

    @Override
    protected void onPreExecute() {
      searchField.setText("");
    }
  }

  private class ImageLoadTask extends AsyncTask<String, String, Bitmap> {
    private User user;
    private BaseAdapter adapter;

    public ImageLoadTask(User user, BaseAdapter adapter) {
      this.user = user;
      this.adapter = adapter;
    }

    protected Bitmap doInBackground(String... param) {
      Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
      if (user.getId() == UserHelper.getUserDetails(activity).getId()) {
        return ImageHelper.getProfilePicture(user.getId());
      } else {
        try {
          URL url = new URL(_Server + "/Images/" + user.getId()
              + "/Profile_Pictures/profile.jpg");
          InputStream inputStream = url.openConnection().getInputStream();
          return BitmapFactory.decodeStream(inputStream);
        } catch (MalformedURLException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      return null;
    }

    protected void onPostExecute(Bitmap ret) {
      if (ret != null) {
        user.setProfile_pic(ret);
      }
      if (adapter != null) {
        adapter.notifyDataSetChanged();
      }
    }
  }
}
