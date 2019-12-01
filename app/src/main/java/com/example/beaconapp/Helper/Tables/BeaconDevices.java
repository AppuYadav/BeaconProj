package com.example.beaconapp.Helper.Tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.example.beaconapp.Helper.DatabaseHelper;
import com.example.beaconapp.Model.Database.DeviceLocationInfoDB;
import com.example.beaconapp.Model.Database.UfoDeviceDB;
import com.example.beaconapp.Utils.AppUtils;
import com.example.beaconapp.Utils.Utils;
import java.util.ArrayList;
import java.util.List;

public class BeaconDevices {

  // LOG TAG
  private static final String LOG = "BeaconDevices";

  // Tables Name
  private static final String TABLE_BEACON_DEVICES = "beacon_devices";

  private DatabaseHelper dbHelper;

  private Context context;

  private SQLiteDatabase database;

  private DeviceLocationINFO deviceLocationINFO;

  public BeaconDevices(Context context) {
    this.context = context;
  }

  public BeaconDevices open() throws SQLException {
    dbHelper = new DatabaseHelper(context);
    database = dbHelper.getWritableDatabase();
    deviceLocationINFO = new DeviceLocationINFO(context);
    return this;
  }

  // BEACON_DEVICE table - columns name
  private static final String KEY_DEVICE_ID = "device_id";
  private static final String MAC_ID = "mac_id";
  private static final String DEVICE_TYPE = "device_type";
  private static final String MAJOR = "major";
  private static final String MINOR = "minor";
  private static final String UUID = "uuid";
  private static final String TX_POWER = "tx_power";
  private static final String RSSI = "rssi";
  private static final String DISTANCE = "distance";
  private static final String TEMP = "temp";
  private static final String LAST_UPDATED = "last_updated";
  private static final String SCAN_RECORD = "scan_record";
  private static final String DEVICE_STATUS = "device_status";
  private static final String DEVICE_CREATED_DATE = "device_created_date";

  // Table Create Statements
  // Beacon Device table create statement
  private static final String CREATE_TABLE_BEACON_DEVICE = "CREATE TABLE " + TABLE_BEACON_DEVICES + "(" + KEY_DEVICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + MAC_ID + " TEXT UNIQUE, " + DEVICE_TYPE + " TEXT, " + MAJOR + " TEXT, " + MINOR + " TEXT, " + UUID + " TEXT, " + TX_POWER + " TEXT, " + RSSI + " TEXT, " + DISTANCE + " TEXT, " + TEMP + " TEXT, " + LAST_UPDATED + " TEXT, " + SCAN_RECORD + " TEXT, " + DEVICE_STATUS + " BOOLEAN NOT NULL CHECK (" + DEVICE_STATUS +" IN (0,1)), " + DEVICE_CREATED_DATE + " TEXT)";

  /**
   * Creating beacon device
   */

  public long insert(UfoDeviceDB ufoDeviceDB){

    ContentValues values = new ContentValues();
    values.put(MAC_ID, ufoDeviceDB.getMacID());
    values.put(DEVICE_TYPE, ufoDeviceDB.getDeviceType());
    values.put(MAJOR, ufoDeviceDB.getMajor());
    values.put(MINOR, ufoDeviceDB.getMinor());
    values.put(UUID, ufoDeviceDB.getUUID());
    values.put(TX_POWER, ufoDeviceDB.getTxPower());
    values.put(RSSI, ufoDeviceDB.getRSSI());
    values.put(DISTANCE, ufoDeviceDB.getDistance());
    values.put(TEMP, ufoDeviceDB.getTemp());
    values.put(LAST_UPDATED, ufoDeviceDB.getLastUpdate());
    values.put(SCAN_RECORD, ufoDeviceDB.getScanRecord());
    values.put(DEVICE_STATUS, ufoDeviceDB.getRegisterDevice());
    values.put(DEVICE_CREATED_DATE, AppUtils.getDateTime());

    try {

      // insert row
      long device_id = database.insertOrThrow(TABLE_BEACON_DEVICES, null, values);
      Log.d(LOG, "insert: >>>>>>>>" + device_id);

      // insert device_location_info
      DeviceLocationInfoDB deviceLocationInfoDB = ufoDeviceDB.getDeviceLocationInfoDB();
      deviceLocationInfoDB.setDeviceID(device_id);
      deviceLocationINFO.open();
      long location_id = deviceLocationINFO.insert(deviceLocationInfoDB);

      return device_id;

    }catch (SQLiteConstraintException e){
      Log.d(LOG, "insert: " + e.getMessage());
      return -1;
    }

  }

  /**
   * Delete beacon device
   */

  public void delete(String mac_id){

    Cursor cursor = null;
    cursor = database.rawQuery("SELECT device_id from " + TABLE_BEACON_DEVICES + " where mac_id = ?", new String[] {mac_id});

    if(cursor.getCount() > 0) {
      cursor.moveToFirst();
      database.delete(DeviceLocationINFO.getTableDevicesLocationName(), "device_id="+cursor.getInt(cursor.getColumnIndex("device_id")),null);
      database.delete(TABLE_BEACON_DEVICES, MAC_ID + "='" + mac_id + "'",null);
    }
  }

  public int getDeviceID(String location){
    String query = "select bd.device_id as device_id from beacon_devices bd \n"
        + "left join device_location_info dli on \n"
        + "bd.device_id = dli.device_id \n"
        + "where dli.location_name ='" +location+"'";

    Log.e(LOG, query);

    Cursor c = database.rawQuery(query, null);
    if (c != null)
    {
      c.moveToFirst();
      int name = c.getInt(0);
      // since name is in position 1 ie first coloumn
      return name;
    }
    return 1;
  }

  public String getIDDeviceName(String id){
    String query = "select dli.location_name as location_name from beacon_devices bd \n"
        + "left join device_location_info dli on \n"
        + "bd.device_id = dli.device_id \n"
        + "where bd.device_id ='" +id+"'";

    Log.e(LOG, query);

    Cursor c = database.rawQuery(query, null);
    if (c != null)
    {
      c.moveToFirst();
      String name = c.getString(0);
      // since name is in position 1 ie first coloumn
      return name;
    }
    return "None";
  }

  public long getNameTOID(String name){
    String query = "select bd.device_id as device_id from beacon_devices bd left join device_location_info dli on bd.device_id = dli.device_id where UPPER(dli.location_name) = UPPER('"+ name +"')";

    Log.e(LOG, query);

    Cursor c = database.rawQuery(query, null);
    if (c.getCount() == 1)
    {
      c.moveToFirst();
      long id = c.getLong(0);
      // since name is in position 1 ie first coloumn
      return id;
    }
    return -1;
  }

  public List<UfoDeviceDB> getBeaconDeviceAll(){
    List<UfoDeviceDB> ufoDeviceDBList = new ArrayList<UfoDeviceDB>();

    String selectQuery = "SELECT bd."+ KEY_DEVICE_ID+ " as device_id, bd."+ RSSI +" as rssi, bd." + DEVICE_TYPE + " as device_type, bd." + MAC_ID + " as mac_id, bd." + MAJOR + " as major, bd." + MINOR + " as minor, bd." + DEVICE_CREATED_DATE + " as device_created_date, bd."+DEVICE_STATUS+" as device_status, dli." + DeviceLocationINFO.getLoactionName() + " as location_name, dli." + DeviceLocationINFO.getMessageBroadcast() + " as message_broadcast from "+TABLE_BEACON_DEVICES+ " as bd inner join " + DeviceLocationINFO.getTableDevicesLocationName() + " as dli on bd." + KEY_DEVICE_ID + " = dli." + DeviceLocationINFO.getDeviceId();

    Log.e(LOG, selectQuery);

    Cursor c = database.rawQuery(selectQuery, null);

    // looping through all rows and adding to list
    if(c.moveToFirst()){
      do{

        DeviceLocationInfoDB deviceLocationInfoDB = new DeviceLocationInfoDB();
        deviceLocationInfoDB.setLocationName(c.getString((c.getColumnIndex(DeviceLocationINFO.getLoactionName()))));
        deviceLocationInfoDB.setBroadCastMessage(c.getString((c.getColumnIndex(DeviceLocationINFO.getMessageBroadcast()))));

        UfoDeviceDB ufoDeviceDB = new UfoDeviceDB(c.getString((c.getColumnIndex(DEVICE_TYPE))), c.getString((c.getColumnIndex(MAC_ID))), c.getString((c.getColumnIndex(MAJOR))), c.getString((c.getColumnIndex(MINOR))), c.getInt((c.getColumnIndex(DEVICE_STATUS))) != 0, deviceLocationInfoDB, c.getString((c.getColumnIndex(DEVICE_CREATED_DATE))), c.getString((c.getColumnIndex(RSSI))));
        ufoDeviceDB.setRSSI(c.getString((c.getColumnIndex(RSSI))));

        ufoDeviceDBList.add(ufoDeviceDB);

      }while (c.moveToNext());
    }

    c.close();

    return ufoDeviceDBList;
  };


  public List<String> getSelectedBeacon(String location){
    List<String> ufoDeviceDBList = new ArrayList<String>();

    String selectQuery = "SELECT bd."+ KEY_DEVICE_ID+ " as device_id, bd." + DEVICE_TYPE + " as device_type, bd." + MAC_ID + " as mac_id, bd." + MAJOR + " as major, bd." + MINOR + " as minor, bd." + DEVICE_CREATED_DATE + " as device_created_date, bd."+DEVICE_STATUS+" as device_status, dli." + DeviceLocationINFO.getLoactionName() + " as location_name, dli." + DeviceLocationINFO.getMessageBroadcast() + " as message_broadcast from "+TABLE_BEACON_DEVICES+ " as bd inner join " + DeviceLocationINFO.getTableDevicesLocationName() + " as dli on bd." + KEY_DEVICE_ID + " = dli." + DeviceLocationINFO.getDeviceId() + " where dli." + DeviceLocationINFO.getLoactionName() + " != '" + location + "'";

    Log.e(LOG, selectQuery);

    Cursor c = database.rawQuery(selectQuery, null);

    // looping through all rows and adding to list
    if(c.moveToFirst()){
      do{

        String countQuery = "select * from beacon_mapping where b1="+getDeviceID(location)+" and b2="+getDeviceID(c.getString((c.getColumnIndex(DeviceLocationINFO.getLoactionName()))));
        Cursor cursor = database.rawQuery(countQuery, null);
        if(cursor.getCount() == 0){
          ufoDeviceDBList.add(c.getString((c.getColumnIndex(DeviceLocationINFO.getLoactionName()))));
        }

      }while (c.moveToNext());
    }

    c.close();

    return ufoDeviceDBList;
  };

  /**
   * getting beacon devices count
   */

  public int getBeaconDeviceCount() {
    String countQuery = "SELECT  * FROM " + TABLE_BEACON_DEVICES;
    Cursor cursor = database.rawQuery(countQuery, null);

    int count = cursor.getCount();
    cursor.close();

    // return count
    return count;
  }

  public Boolean getDeviceStatus(String macid){
    String countQuery = "SELECT * FROM " + TABLE_BEACON_DEVICES + " where " + MAC_ID + " =?";
    Cursor cursor = database.rawQuery(countQuery, new String[] { macid });

    int count = cursor.getCount();
    cursor.close();

    // return count
    return count != 0;
  }

  public String getDeviceName(String macid){
    Cursor cursor = null;
    String location_Name = "";
    try {
      cursor = database.rawQuery("SELECT dli.location_name as location_name FROM beacon_devices as bd inner join device_location_info dli on bd.device_id = dli.device_id where bd.mac_id =?", new String[] {macid + ""});

      if(cursor.getCount() > 0) {
        cursor.moveToFirst();
        location_Name = cursor.getString(cursor.getColumnIndex("location_name"));
      }
      return location_Name;
    }finally {
      cursor.close();
    }
  }

  public String getBroadCastMessage(String macid){
    Cursor cursor = null;
    String location_Name = "";
    try {
      cursor = database.rawQuery("SELECT dli.message_broadcast as location_name FROM beacon_devices as bd inner join device_location_info dli on bd.device_id = dli.device_id where bd.mac_id =?", new String[] {macid + ""});

      if(cursor.getCount() > 0) {
        cursor.moveToFirst();
        location_Name = cursor.getString(cursor.getColumnIndex("location_name"));
      }
      return location_Name;
    }finally {
      cursor.close();
    }
  }

  public Boolean checkExists(String location_name){
    String query = "select * from device_location_info where location_name = UPPER('"+ location_name +"')";

    Log.e(LOG, query);

    Cursor c = database.rawQuery(query, null);
    if (c.getCount() == 1)
    {
      return true;
    }
    return false;
  }

  /**
   * Updating a Beacon Devices
   */
  public int update(UfoDeviceDB ufoDeviceDB) {
//    String selectQuery = "update " +DeviceLocationINFO.getTableDevicesLocationName()+" set " + DeviceLocationINFO.getLoactionName() +" ='" + ufoDeviceDB.getDeviceLocationInfoDB().getLocationName() + "'," + DeviceLocationINFO.getMessageBroadcast() + " = '" + ufoDeviceDB.getDeviceLocationInfoDB().getBroadCastMessage() + "' where device_id = (select device_id from "+ TABLE_BEACON_DEVICES + " where mac_id = '"+ ufoDeviceDB.getMacID()+"')";

//    Cursor c = database.rawQuery(selectQuery, null);

    Cursor cursor = database.rawQuery("SELECT device_id from " + TABLE_BEACON_DEVICES + " where mac_id = ?", new String[] {ufoDeviceDB.getMacID()});

//    if(checkExists(ufoDeviceDB.getDeviceLocationInfoDB().getLocationName())){
      if(cursor.getCount() > 0) {
        cursor.moveToFirst();
        ContentValues cv = new ContentValues();
        cv.put(DeviceLocationINFO.getLoactionName(),ufoDeviceDB.getDeviceLocationInfoDB().getLocationName());
        cv.put(DeviceLocationINFO.getMessageBroadcast(),ufoDeviceDB.getDeviceLocationInfoDB().getBroadCastMessage());
        database.update(DeviceLocationINFO.getTableDevicesLocationName(), cv, "device_id="+cursor.getInt(cursor.getColumnIndex("device_id")), null);

        ContentValues cv2 = new ContentValues();
        cv2.put(RSSI,ufoDeviceDB.getRSSI());
        database.update(TABLE_BEACON_DEVICES, cv2, "device_id="+cursor.getInt(cursor.getColumnIndex("device_id")), null);
      }

      return 1;
//    }else{
//      return -1;
//    }

  }

  public static String getCreateTableBeaconDevice() {
    return CREATE_TABLE_BEACON_DEVICE;
  }

  public static String getTableBeaconDevices() {
    return TABLE_BEACON_DEVICES;
  }

  public static String getKeyDeviceId() {
    return KEY_DEVICE_ID;
  }

}
