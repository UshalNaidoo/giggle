package jokes.gigglebyte.destino.ush.gigglebyte.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.FollowHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.PostHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.SharedPrefHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.UserHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.notifications.GCMClientManager;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;
import jokes.gigglebyte.destino.ush.gigglebyte.server.ConnectToServer;

public class SplashScreenActivity extends Activity {
  private Activity activity;
  private ProgressBar progressBar;
  public static RegisterWithServer serverCall;
  private String PROJECT_NUMBER="974166531337";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = this;
    setContentView(R.layout.activity_splash);
    progressBar = (ProgressBar) findViewById(R.id.progressBar);
    serverCall = new RegisterWithServer();
    serverCall.execute();
  }

  class RegisterWithServer extends AsyncTask<Integer, Integer, String> {

    private volatile boolean running = true;

    @Override
    protected String doInBackground(Integer... params) {

      int i = 0;
      while (running && i < 7) {
        i++;
        final User user = UserHelper.getUserDetails(activity);
        switch (i) {
          case 1:
            if (user.getId() == -1) {
              user.setId(ConnectToServer.registerNewUser());
              UserHelper.saveUserDetails(activity, user);
            }
            publishProgress(20);
            break;
          case 2:
            GCMClientManager pushClientManager = new GCMClientManager(activity, PROJECT_NUMBER);
            pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {
              @Override
              public void onSuccess(String registrationId, boolean isNewRegistration) {

                Log.d("Registration id", registrationId);
                ConnectToServer.addDeviceId(user.getId(), registrationId);
              }
              @Override
              public void onFailure(String ex) {
                super.onFailure(ex);
              }
            });
            publishProgress(30);
            break;
          case 3:
            PostHelper.initialiseHotPosts(activity, ConnectToServer.getHotPosts());
            publishProgress(40);
            break;
          case 4:
            PostHelper.initialiseNewPosts(activity, ConnectToServer.getNewPosts());
            publishProgress(50);
            break;
          case 5:
            PostHelper.initialiseFavoritePosts(activity, ConnectToServer.getFavoritePosts(SharedPrefHelper
                                                                                              .getUserFavorites(activity)));
            publishProgress(80);
            break;
          case 6:
            PostHelper.initialiseFeedPosts(activity, ConnectToServer.getFeed(user.getId()));
            FollowHelper.initialiseUserFollowing(ConnectToServer.getUserFollowing(user.getId()));
            FollowHelper.initialiseUserFollowers(ConnectToServer.getUserFollowers(user.getId()));
            publishProgress(100);
            break;
        }
      }
      return null;
    }

    @Override
    protected void onPostExecute(String result) {
      progressBar.setVisibility(View.GONE);
      startActivity(new Intent(activity, MainActivity.class));
      finish();
    }

    @Override
    protected void onCancelled() {
      running = false;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
      progressBar.setProgress(values[0]);
    }
  }

}