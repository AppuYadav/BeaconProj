package com.example.beaconapp.Service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import com.example.beaconapp.Activity.MainActivity;
import com.example.beaconapp.R;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

public class NearbyBackgroundService extends IntentService {

  private static final int MESSAGES_NOTIFICATION_ID = 1;

  public NearbyBackgroundService() {
    super(NearbyBackgroundService.class.getName());
  }

  public NearbyBackgroundService(String name) {
    super(name);
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    if (intent != null) {
      Nearby.Messages.handleIntent(intent, new MessageListener() {
        @Override
        public void onFound(Message message) {
          Log.i("Test>>>>>>>>>>>>>>>>", "found message = " + message);
          updateNotification(message);
        }

        @Override
        public void onLost(Message message) {
          Log.i("Test>>>>>>>>>>>>>>>>>", "lost message = " + message);
          updateNotification(message);
        }
      });
    }
  }

  private void updateNotification(Message messages) {
    NotificationManager notificationManager =
        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    Intent launchIntent = new Intent(getApplicationContext(), MainActivity.class);
    launchIntent.setAction(Intent.ACTION_MAIN);
    launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
    PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0,
        launchIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    String contentTitle = messages.getType();
    String contentText = new String(messages.getContent());

    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.logo)
        .setContentTitle(contentTitle)
        .setContentText(contentText)
        .setStyle(new NotificationCompat.BigTextStyle().bigText(contentText))
        .setContentIntent(pi);
    notificationManager.notify(MESSAGES_NOTIFICATION_ID, notificationBuilder.build());
  }
}
