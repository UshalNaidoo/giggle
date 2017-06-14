package jokes.gigglebyte.destino.ush.gigglebyte.notifications;

import com.google.android.gms.gcm.GcmListenerService;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import jokes.gigglebyte.destino.ush.gigglebyte.R;
import jokes.gigglebyte.destino.ush.gigglebyte.activities.MainActivity;


public class PushNotificationService extends GcmListenerService {

  @Override
  public void onMessageReceived(String from, Bundle data) {
    String message = data.getString("message");

    Log.e("Gigglebyte", "Message received : " + message);

    sendNotification(message);
  }


  private void sendNotification(String message) {
    int icon = R.drawable.logo;
    long when = System.currentTimeMillis();
    NotificationManager notificationManager = (NotificationManager)
        this.getSystemService(Context.NOTIFICATION_SERVICE);
    Notification notification = new Notification(icon, message, when);

    String title = this.getString(R.string.app_name);

    Intent notificationIntent = new Intent(this, MainActivity.class);
    notificationIntent.putExtra("message", message);
    // set intent so it does not start a new activity
    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_SINGLE_TOP);
    PendingIntent intent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
    notification.setLatestEventInfo(this, title, message, intent);
    notification.flags |= Notification.FLAG_AUTO_CANCEL;

    // Play default notification sound
    notification.defaults |= Notification.DEFAULT_SOUND;

    // Vibrate if vibrate is enabled
    notification.defaults |= Notification.DEFAULT_VIBRATE;
    notificationManager.notify(0, notification);
  }
}