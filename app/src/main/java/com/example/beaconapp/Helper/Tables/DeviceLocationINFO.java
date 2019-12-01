package com.example.beaconapp.Helper.Tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.example.beaconapp.Helper.DatabaseHelper;
import com.example.beaconapp.Model.Database.DeviceLocationInfoDB;
import com.example.beaconapp.Model.DestInfo;
import com.example.beaconapp.Utils.AppUtils;

public class DeviceLocationINFO {

  public static String getTableDevicesLocationName() {
    return TABLE_DEVICES_LOCATION_NAME;
  }

  // Tables Name
  private static final String TABLE_DEVICES_LOCATION_NAME = "device_location_info";

  private DatabaseHelper dbHelper;

  private Context context;

  private SQLiteDatabase database;

  public DeviceLocationINFO(Context c){
    context = c;
  }

  public DeviceLocationINFO open() throws SQLException {
    dbHelper = new DatabaseHelper(context);
    database = dbHelper.getWritableDatabase();
    return this;
  }

  // DEVICE_LOCATION_INFO - column name
  private static final String LOCATION_ID = "location_id";
  private static final String DEVICE_ID = "device_id";
  private static final String LOACTION_NAME = "location_name";
  private static final String LOCATION_STATUS = "location_status";
  private static final String MESSAGE_BROADCAST = "message_broadcast";
  private static final String DEVICE_LOCATION_CREATED_DATE = "device_location_created_date";
  private static final String DEVICE_LOCATION_UPDATED_DATE = "device_location_updated_date";

  // Device Location Info create Statement
  private static final String CREATE_TABLE_DEVICE_LOCATION_INFO = "CREATE TABLE " + TABLE_DEVICES_LOCATION_NAME + "( " + LOCATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DEVICE_ID + " INTEGER NOT NULL, " +  LOACTION_NAME + " TEXT NOT NULL,"+ MESSAGE_BROADCAST + " TEXT NOT NULL, " + LOCATION_STATUS + " BOOLEAN NOT NULL CHECK (" + LOCATION_STATUS + " IN (0,1)), "+DEVICE_LOCATION_CREATED_DATE+" TEXT, " +DEVICE_LOCATION_UPDATED_DATE+" TEXT, FOREIGN KEY ("+DEVICE_ID+") REFERENCES " + BeaconDevices.getTableBeaconDevices() + "("+ BeaconDevices.getKeyDeviceId() +") ON UPDATE SET NULL)";

  public static String getCreateTableDeviceLocationInfo() {
    return CREATE_TABLE_DEVICE_LOCATION_INFO;
  }

  public static String getMessageBroadcast() {
    return MESSAGE_BROADCAST;
  }

  public static String getDeviceId() {
    return DEVICE_ID;
  }

  public static String getLoactionName() {
    return LOACTION_NAME;
  }

  /**
   * Creating device location info
   */

  public long insert(DeviceLocationInfoDB deviceLocationINFO){

    ContentValues values = new ContentValues();
    values.put(DEVICE_ID, deviceLocationINFO.getDeviceID());
    values.put(LOACTION_NAME, deviceLocationINFO.getLocationName());
    values.put(LOCATION_STATUS, deviceLocationINFO.getLocationStatus());
    values.put(DEVICE_LOCATION_CREATED_DATE, AppUtils.getDateTime());
    values.put(DEVICE_LOCATION_UPDATED_DATE, AppUtils.getDateTime());
    values.put(MESSAGE_BROADCAST, deviceLocationINFO.getBroadCastMessage());

    // insert row
    long location_id =database.insert(TABLE_DEVICES_LOCATION_NAME, null, values);

    return location_id;
  }

  public DestInfo getDeviceInfo(int s, int d){

    DestInfo destInfo = new DestInfo();

    String destQuery = "select dli.location_name as location_name, rssi as rssi, message_broadcast as message_broadcast\n"
        + " from beacon_devices bd left join device_location_info dli on bd.device_id = dli.device_id where bd.device_id = '"+d+"'";

    String sourceQuery = "select mac_id from beacon_devices where device_id = '"+s+"'";

    String directQuery = "select distance, direction, hops, mapping_message from beacon_mapping where b1 = '"+ s +"' and b2 = '" + d + "'";

    Cursor c = database.rawQuery(destQuery, null);

    // looping through all rows and adding to list
    if(c.moveToFirst()){
      do{
        destInfo.setLocation_name(c.getString((c.getColumnIndex("location_name"))));
        destInfo.setRssi(c.getString((c.getColumnIndex("rssi"))));
        destInfo.setMessage_broadcast(c.getString((c.getColumnIndex("message_broadcast"))));
      }while (c.moveToNext());
    }


    c = database.rawQuery(sourceQuery, null);

    // looping through all rows and adding to list
    if(c.moveToFirst()){
      do{
        destInfo.setMac_id(c.getString((c.getColumnIndex("mac_id"))));
      }while (c.moveToNext());
    }

    c = database.rawQuery(directQuery, null);

    // looping through all rows and adding to list
    if(c.moveToFirst()){
      do{
        destInfo.setHops(c.getString((c.getColumnIndex("hops"))));
        destInfo.setDirection(c.getString((c.getColumnIndex("direction"))));
        destInfo.setStep(c.getString((c.getColumnIndex("distance"))));
        destInfo.setMessage_mapping(c.getString((c.getColumnIndex("mapping_message"))));
      }while (c.moveToNext());
    }
    return destInfo;
  }

  public Boolean checkLocationName(String locationName){
    String countQuery = "SELECT * FROM " + TABLE_DEVICES_LOCATION_NAME + " where " + LOACTION_NAME + " =?";
    Cursor cursor = database.rawQuery(countQuery, new String[] { locationName });

    int count = cursor.getCount();
    cursor.close();

    // return count
    return count != 0;
  }

  public void close() {
    dbHelper.close();
  }
}
