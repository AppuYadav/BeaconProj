package com.example.beaconapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.google.android.gms.nearby.messages.Message;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Utils {
  public static final String KEY_CACHED_MESSAGES = "beaconmessages";
  public static List<String> getCachedMessages(Context context) {
    SharedPreferences sharedPrefs = getSharedPreferences(context);
    String cachedMessagesJson = sharedPrefs.getString(KEY_CACHED_MESSAGES, "");
    if (TextUtils.isEmpty(cachedMessagesJson)) {
      return Collections.emptyList();
    } else {
      Type type = new TypeToken<List<String>>() {}.getType();
      return new Gson().fromJson(cachedMessagesJson, type);
    }
  }

  public static void saveFoundMessage(Context context, Message message) {
    ArrayList<String> cachedMessages = new ArrayList<>(getCachedMessages(context));
    Set<String> cachedMessagesSet = new HashSet<>(cachedMessages);
    String messageString = new String(message.getContent());
    if (!cachedMessagesSet.contains(messageString)) {
      cachedMessages.add(0, new String(message.getContent()));
      getSharedPreferences(context)
          .edit()
          .putString(KEY_CACHED_MESSAGES, new Gson().toJson(cachedMessages))
          .apply();
    }
  }

  public static void removeLostMessage(Context context, Message message) {
    ArrayList<String> cachedMessages = new ArrayList<>(getCachedMessages(context));
    cachedMessages.remove(new String(message.getContent()));
    getSharedPreferences(context)
        .edit()
        .putString(KEY_CACHED_MESSAGES, new Gson().toJson(cachedMessages))
        .apply();
  }

  public static SharedPreferences getSharedPreferences(Context context) {
    return context.getSharedPreferences(
        context.getApplicationContext().getPackageName(),
        Context.MODE_PRIVATE);
  }
}
