package com.example.beaconapp.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.example.beaconapp.Activity.MainActivity;
import com.example.beaconapp.R;
import com.google.android.gms.nearby.messages.Message;

public class TrackerService extends Service {

  private static final int MESSAGES_NOTIFICATION_ID = 1;

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    buildNotification();
  }

  private void buildNotification() {
    // Create the persistent notification
    NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
        .setContentTitle(getString(R.string.app_name))
        .setContentText(getString(R.string.notification_text))
        .setOngoing(true)
        .setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.notification_text)))
        .setSmallIcon(R.drawable.rfid);
    startForeground(1, builder.build());
  }

//  private void updateNotification(String messages) {
//    NotificationManager notificationManager =
//        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//    Intent launchIntent = new Intent(getApplicationContext(), MainActivity.class);
//    launchIntent.setAction(Intent.ACTION_MAIN);
//    launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//    PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0,
//        launchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
////    String contentTitle = messages.getType();
////    String contentText = new String(messages.getContent());
//
//    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//        .setSmallIcon(R.drawable.rfid)
//        .setContentTitle(getString(R.string.app_name))
//        .setContentText(messages)
//        .setStyle(new NotificationCompat.BigTextStyle().bigText(messages))
//        .setContentIntent(pi);
//    notificationManager.notify(MESSAGES_NOTIFICATION_ID, notificationBuilder.build());
//  }
}
