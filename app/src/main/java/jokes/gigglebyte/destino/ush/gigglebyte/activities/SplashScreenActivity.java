package jokes.gigglebyte.destino.ush.gigglebyte.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.FollowHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.JsonParser;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.PostHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.SharedPrefHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.UserHelper;
import jokes.gigglebyte.destino.ush.gigglebyte.notifications.GCMClientManager;
import jokes.gigglebyte.destino.ush.gigglebyte.objects.User;
import jokes.gigglebyte.destino.ush.gigglebyte.server.ConnectToServer;

import static jokes.gigglebyte.destino.ush.gigglebyte.datahelpers.SplashHelper.getLoadingMessages;

public class SplashScreenActivity extends Activity {
  private Activity activity;
  private ProgressBar progressBar;
  private TextView loadingText;
  public static RegisterWithServer serverCall;
  private String PROJECT_NUMBER="974166531337";
  private volatile boolean running = true;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = this;
    setContentView(R.layout.activity_splash);
    progressBar = (ProgressBar) findViewById(R.id.progressBar);
    loadingText = (TextView) findViewById(R.id.loadingText);
    serverCall = new RegisterWithServer();
    serverCall.execute();
  }

  @Override
  public void onBackPressed() {
    running = false;
  }

  class RegisterWithServer extends AsyncTask<Integer, Integer, String> {


    @Override
    protected String doInBackground(Integer... params) {
      int i = 0;
      while (running && i < 10) {
        i++;
        final User user = UserHelper.getUserDetails(activity);
        switch (i) {
          case 1:
            if (user.getId() == -1) {
              user.setId(ConnectToServer.registerNewUser());
              UserHelper.saveUserDetails(activity, user);
            }
            publishProgress(10);
            break;
          case 2:
            GCMClientManager pushClientManager = new GCMClientManager(activity, PROJECT_NUMBER);
            pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {
              @Override
              public void onSuccess(String registrationId, boolean isNewRegistration) {
                ConnectToServer.addDeviceId(user.getId(), registrationId);
              }
              @Override
              public void onFailure(String ex) {
                super.onFailure(ex);
              }
            });
            publishProgress(20);
            break;
          case 3:
            PostHelper.initialiseHotPosts(activity, ConnectToServer.getHotPosts());
            publishProgress(30);
            break;
          case 4:
            PostHelper.initialiseNewPosts(activity, ConnectToServer.getNewPosts());
            publishProgress(40);
            break;
          case 5:
            PostHelper.initialiseFavoritePosts(activity, ConnectToServer.getFavoritePosts(SharedPrefHelper.getUserFavorites(activity)));
            publishProgress(50);
            break;
          case 6:
            PostHelper.initialiseFeedPosts(activity, ConnectToServer.getFeed(user.getId()));
            publishProgress(60);
            break;
          case 7:
            FollowHelper.initialiseUserFollowing(ConnectToServer.getUserFollowing(user.getId()));
            publishProgress(70);
            break;
          case 8:
            FollowHelper.initialiseUserFollowers(ConnectToServer.getUserFollowers(user.getId()));
            publishProgress(80);
            break;
          case 9:
            FollowHelper.initialiseUserFollowers(ConnectToServer.getUserFollowers(user.getId()));
            publishProgress(90);
            break;
          case 10:
            MainActivity.allTags = JsonParser.GetAllTagsList(ConnectToServer.getAllTags());
            publishProgress(100);
            break;
        }
      }
      return null;
    }

    @Override
    protected void onPostExecute(String result) {
      progressBar.setVisibility(View.GONE);
      if (running) {
        startActivity(new Intent(activity, MainActivity.class));
      }
      finish();
    }

    @Override
    protected void onCancelled() {
      running = false;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
      String[] messages = getLoadingMessages(activity);
      switch (values[0]) {
        case 10:
          loadingText.setText(messages[0]);
          break;
        case 30:
          loadingText.setText(messages[1]);
          break;
        case 50:
          loadingText.setText(messages[2]);
          break;
        case 70:
          loadingText.setText(messages[3]);
          break;
        case 60:
          loadingText.setText(messages[4]);
          break;
        case 80:
          loadingText.setText(messages[5]);
          break;
      }
      progressBar.setProgress(values[0]);
    }
  }

}