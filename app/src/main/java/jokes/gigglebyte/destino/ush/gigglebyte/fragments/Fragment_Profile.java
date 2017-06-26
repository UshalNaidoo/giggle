package jokes.gigglebyte.destino.ush.gigglebyte.fragments;

import static jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.PostHelper.getPostStatus;

import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.adapters.PosterProfileListAdapter;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.FollowHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.JsonParser;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.PostHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.UIHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.UserHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.dialogs.EditUserDescriptionDialog;
import jokes.gigglebyte.destino.ush.gigglebyte.dialogs.EditUserNameDialog;
import jokes.gigglebyte.destino.ush.gigglebyte.dialogs.OptionsProfilePictureDialog;
import jokes.gigglebyte.destino.ush.gigglebyte.enums.FromScreen;
import jokes.gigglebyte.destino.ush.gigglebyte.interfaces.FragmentLifecycle;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.Post;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;
import jokes.gigglebyte.destino.ush.gigglebyte.server.ConnectToServer;

public class Fragment_Profile extends Fragment implements FragmentLifecycle {

  private static Activity activity;

  private static FloatingActionMenu menuDown;
  private ListView listView;
  private static TextView userName;
  private static User myProfile;
  private static PosterProfileListAdapter adapter;

  @Override
  public View onCreateView(LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    activity = this.getActivity();

    View rootView = inflater.inflate(R.layout.activity_user_profile, container, false);

    menuDown = (FloatingActionMenu) rootView.findViewById(R.id.menu_down);
    userName = (TextView) rootView.findViewById(R.id.userName);
    listView = (ListView) rootView.findViewById(R.id.listView);
    myProfile = UserHelper.getUserDetails(activity);
    myProfile.setFollowers(FollowHelper.getFollowers());
    myProfile.setFollowing(FollowHelper.getFollowing());
    UserHelper.selectedUser = myProfile;
    userName.setText(myProfile.getName().isEmpty() ? getResources().getString(R.string.unknown) : myProfile.getName());

    userName.setOnClickListener(new View.OnClickListener() {
      int i = 0;

      @Override
      public void onClick(View v) {
        i++;
        Handler handler = new Handler();
        Runnable singleClick = new Runnable() {
          @Override
          public void run() {
            i = 0;
          }
        };

        if (i == 1) {
          //Single click
          handler.postDelayed(singleClick, 250);
        } else if (i == 2) {
          //Double click
          i = 0;
          User user = UserHelper.getUserDetails(activity);
          editUsername(user);
        }
      }
    });

    if (activity != null && listView != null && userName != null) {
      userName.setText(myProfile.getName());
      new GetProfile().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    UIHelper.setActionBar(activity, "", true);

    FloatingActionButton changeNameFab = (FloatingActionButton) rootView.findViewById(R.id.changeNameFab);
    FloatingActionButton changeDescriptionFab = (FloatingActionButton) rootView.findViewById(R.id.changeDescriptionFab);
    FloatingActionButton changePictureFab = (FloatingActionButton) rootView.findViewById(R.id.changePictureFab);

    changeNameFab.setOnClickListener(clickListener);
    changeDescriptionFab.setOnClickListener(clickListener);
    changePictureFab.setOnClickListener(clickListener);


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



    return rootView;
  }

  @Override
  public void onPauseFragment() {
  }

  @Override
  public void onResumeFragment() {
  }


  private class GetProfile extends AsyncTask<Integer, Integer, String> {

    @Override
    protected String doInBackground(Integer... params) {
      Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
      return ConnectToServer.getUsersPosts(myProfile.getId());
    }

    @Override
    protected void onPostExecute(String result) {
      result = "{\"posts\":" + result + "}";
      List<Post> posts = JsonParser.GetPosts(result);
      posts = getPostStatus(activity, posts);
      PostHelper.setPostsForUser(activity, posts);
      adapter = new PosterProfileListAdapter(activity, posts, myProfile, FromScreen.USER);
      listView.setAdapter(adapter);
    }
  }

  public static void refreshFragment() {
    if (menuDown != null) {
      menuDown.hideMenuButton(false);
      new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
          menuDown.showMenuButton(true);
        }
      }, 300);
    }
  }

  private View.OnClickListener clickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      User user = UserHelper.getUserDetails(activity);
      switch (v.getId()) {
        case R.id.changeNameFab:
          editUsername(user);
          break;
        case R.id.changeDescriptionFab:
          editDescription(user);
          break;
        case R.id.changePictureFab:
          startPictureDialog();
          break;
      }
    }
  };

  public void startPictureDialog() {
    OptionsProfilePictureDialog pickerProfilePictureDialog = new OptionsProfilePictureDialog();
    pickerProfilePictureDialog.show(activity.getFragmentManager(), "");
  }

  public static void editDescription(User user) {
    EditUserDescriptionDialog editUserDescriptionDialog = new EditUserDescriptionDialog();
    editUserDescriptionDialog.setListener(new UserHelper());
    editUserDescriptionDialog.setUser(user);
    editUserDescriptionDialog.show(activity.getFragmentManager(), "");
  }

  private void editUsername(User user) {
    EditUserNameDialog editUserNameDialog = new EditUserNameDialog();
    editUserNameDialog.setListener(new UserHelper());
    editUserNameDialog.setUser(user);
    editUserNameDialog.show(activity.getFragmentManager(), "");
  }

  public static void refreshUser(User user) {
    refreshFragment();
    myProfile.setDescription(user.getDescription());
    myProfile.setName(user.getName());
    myProfile.setProfile_pic(user.getProfile_pic());
    adapter.notifyDataSetChanged();
    userName.setText(user.getName());
  }


}
