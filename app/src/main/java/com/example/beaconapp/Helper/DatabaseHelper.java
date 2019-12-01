package com.example.beaconapp.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;
import com.example.beaconapp.Helper.Tables.BeaconDevices;
import com.example.beaconapp.Helper.Tables.DeviceLocationINFO;
import com.example.beaconapp.Helper.Tables.MappingBeaconTBL;
import com.example.beaconapp.Helper.Tables.MappingTagTBL;

public class DatabaseHelper extends SQLiteOpenHelper {

  // Logcat tag
  private static final String LOG = "DatabaseHelper";

  // Database Version
  private static  final int DATABASE_VERSION = 1;

  // Database Name
  private static final String DATABASE_NAME = "Beacon.db";

  public DatabaseHelper(@Nullable Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    Log.i(LOG, "DatabaseHelper: created");
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    // createing required tables
    Log.i(LOG,  BeaconDevices.getCreateTableBeaconDevice());
    db.execSQL(BeaconDevices.getCreateTableBeaconDevice());
    db.execSQL(DeviceLocationINFO.getCreateTableDeviceLocationInfo());
    db.execSQL(MappingBeaconTBL.getTableBeaconMapping());
    db.execSQL(MappingTagTBL.getTableTagMapping());
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // on upgrade drop older tables
    db.execSQL("DROP TABLE IF EXISTS " + BeaconDevices.getCreateTableBeaconDevice());
    db.execSQL("DROP TABLE IF EXISTS " + DeviceLocationINFO.getCreateTableDeviceLocationInfo());
    db.execSQL("DROP TABLE IF EXISTS " + MappingBeaconTBL.getTableBeaconMapping());
    db.execSQL("DROP TABLE IF EXISTS " + MappingTagTBL.getTableTagMapping());

    Log.i(">>>>>>>>>>>>>>>>>", "onUpgrade: " + DATABASE_VERSION);
    // create new tables
    onCreate(db);
  }

  @Override
  public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    super.onDowngrade(db, oldVersion, newVersion);
  }

  // closing database
  public void closeDB(){
    SQLiteDatabase db = this.getReadableDatabase();
    if(db != null && db.isOpen())
      db.close();
  }
}
