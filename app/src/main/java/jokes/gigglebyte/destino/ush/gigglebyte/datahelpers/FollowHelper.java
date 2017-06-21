package jokes.gigglebyte.destino.ush.gigglebyte.datahelpers;

import android.app.Activity;
import android.content.Context;

public class FollowHelper {

  public static void getUsersUserFollows(Activity activity) {
    UserHelper.getUserDetails(activity).getId();
    Thread thread = new Thread() {
      @Override
      public void run() {
//        ConnectToServer.commentLike(comment.getCommentId(), comment.getUser().getId());
      }
    };
    thread.start();
  }
}
