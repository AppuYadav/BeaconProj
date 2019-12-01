package com.example.beaconapp.Service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.example.beaconapp.Activity.MainActivity;
import com.example.beaconapp.Activity.ScanActivity;
import com.example.beaconapp.R;
import com.example.beaconapp.Utils.Utils;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import java.util.List;
import java.util.Locale;

public class BackgroundSubscribeIntentService extends IntentService {
  private static final String TAG = "BackSubIntentService";

  private static final int MESSAGES_NOTIFICATION_ID = 1;
  private static final int NUM_MESSAGES_IN_NOTIFICATION = 5;
  TextToSpeech t1;

  public BackgroundSubscribeIntentService() {
    super("BackgroundSubscribeIntentService");
  }

  @Override
  public void onCreate() {
    super.onCreate();

    t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
      @Override
      public void onInit(int status) {
        if(status != TextToSpeech.ERROR) {
          t1.setLanguage(Locale.UK);
        }
      }
    });

    updateNotification();
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    if (intent != null) {
      Nearby.Messages.handleIntent(intent, new MessageListener() {
        @Override
        public void onFound(Message message) {
          Utils.saveFoundMessage(getApplicationContext(), message);
          t1.speak(String.valueOf(message.getContent()), TextToSpeech.QUEUE_FLUSH, null);
          updateNotification();
        }

        @Override
        public void onLost(Message message) {
          Utils.removeLostMessage(getApplicationContext(), message);
          updateNotification();
        }
      });
    }
  }

  private void updateNotification() {
    List<String> messages = Utils.getCachedMessages(getApplicationContext());
    NotificationManager notificationManager =
        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    Intent launchIntent = new Intent(getApplicationContext(), ScanActivity.class);
    launchIntent.setAction(Intent.ACTION_MAIN);
    launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
    PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0,
        launchIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    String contentTitle = getContentTitle(messages);
    String contentText = getContentText(messages);

    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.logo)
        .setContentTitle(contentTitle)
        .setContentText(contentText)
        .setStyle(new NotificationCompat.BigTextStyle().bigText(contentText))
        .setOngoing(true)
        .setContentIntent(pi);
    notificationManager.notify(MESSAGES_NOTIFICATION_ID, notificationBuilder.build());
  }

  private String getContentTitle(List<String> messages) {
    switch (messages.size()) {
      case 0:
        return getResources().getString(R.string.scanning);
      case 1:
        return getResources().getString(R.string.one_message);
      default:
        return getResources().getString(R.string.many_messages, messages.size());
    }
  }

  private String getContentText(List<String> messages) {
    String newline = System.getProperty("line.separator");
    if (messages.size() < NUM_MESSAGES_IN_NOTIFICATION) {
      return TextUtils.join(newline, messages);
    }
    return TextUtils.join(newline, messages.subList(0, NUM_MESSAGES_IN_NOTIFICATION)) +
        newline + "&#8230;";
  }
}