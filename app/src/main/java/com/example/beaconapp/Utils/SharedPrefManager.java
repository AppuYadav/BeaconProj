package com.example.beaconapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
  private static SharedPrefManager mInstance;
  private static Context mCtx;

  private static final String SHARED_PREF_NAME = "Beacon@2019";
  private static final String KEY_NICK_NAME = "nickname";
  private static final String BEACON_MANAGER = "beaconmanager";
  private static final String AUTO_ON = "autoon";

  private SharedPrefManager(Context context) {
    mCtx = context;

  }

  public static synchronized SharedPrefManager getInstance(Context context) {
    if (mInstance == null) {
      mInstance = new SharedPrefManager(context);
    }
    return mInstance;
  }

  public boolean setNickName(String nickname){
    SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(KEY_NICK_NAME, nickname);
    editor.apply();
    return true;
  }

  public String getNickName(){
    SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    return sharedPreferences.getString(KEY_NICK_NAME, null);
  }


  public boolean setBeaconManagerStart(Boolean value){
    SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putBoolean(BEACON_MANAGER, value);
    editor.apply();
    return true;
  }

  public Boolean getBeaconManagerStart(){
    SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    return sharedPreferences.getBoolean(BEACON_MANAGER, false);
  }

  public boolean setAutoON(Boolean value){
    SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putBoolean(AUTO_ON, value);
    editor.apply();
    return true;
  }

  public Boolean getAutoON(){
    SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    return sharedPreferences.getBoolean(AUTO_ON, false);
  }

}
