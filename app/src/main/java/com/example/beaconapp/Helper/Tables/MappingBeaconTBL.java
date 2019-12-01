package com.example.beaconapp.Helper.Tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.example.beaconapp.Helper.DatabaseHelper;
import com.example.beaconapp.Model.Database.Tag;
import com.example.beaconapp.Model.GraphData;
import com.example.beaconapp.Model.MappingBeacon;
import com.example.beaconapp.Utils.AppUtils;
import java.util.ArrayList;
import java.util.List;

public class MappingBeaconTBL {

  // LOG TAG
  private static final String LOG = "MappingBeaconTBL";

  // Tables Name
  private static final String TABLE_BEACON_MAPPING = "beacon_mapping";

  private DatabaseHelper dbHelper;

  private Context context;

  private SQLiteDatabase database;

  public MappingBeaconTBL(Context context) {
    this.context = context;
  }

  public MappingBeaconTBL open() throws SQLException {
    dbHelper = new DatabaseHelper(context);
    database = dbHelper.getWritableDatabase();
    return this;
  }

  private static final String BEACON_MAPPING_TBL = "create table "+TABLE_BEACON_MAPPING+" (id integer primary key , b1 integer not null, b2 integer not null,"
      + "hops integer not null, distance integer not null, direction text not null, created_date text, updated_date text, mapping_message text DEFAULT \"Hii\")";

  public static String getTableBeaconMapping() {
    Log.i(LOG, "getTableBeaconMapping: " + BEACON_MAPPING_TBL);
    return BEACON_MAPPING_TBL;
  }

  public long insert(MappingBeacon mappingBeacon){
    ContentValues values = new ContentValues();
    values.put("b1", mappingBeacon.getB1());
    values.put("b2", mappingBeacon.getB2());
    values.put("hops", mappingBeacon.getNumberofhops());
    values.put("distance", mappingBeacon.getDistance());
    values.put("direction", mappingBeacon.getDirection());
    values.put("created_date", AppUtils.getDateTime());
    values.put("updated_date", AppUtils.getDateTime());
    values.put("mapping_message", mappingBeacon.getMapping_message());

    try {

      // insert row
      long mapping_id = database.insertOrThrow(TABLE_BEACON_MAPPING, null, values);
      Log.d(LOG, "insert: >>>>>>>>" + mapping_id);

      return mapping_id;

    }catch (SQLiteConstraintException e){
      Log.d(LOG, "insert: " + e.getMessage());
      return -1;
    }
  }



  public List<MappingBeacon> getMappingDeviceAll(String location_name){
    List<MappingBeacon> mappingBeaconList = new ArrayList<MappingBeacon>();

    String selectQuery = "select bm.id as id, bm.b2 as b2, bm.hops as hops,"
        + " bm.distance as distance, bm.direction as direction, bm.mapping_message as mapping_message,"
        + " bm.created_date as created_date, bm.updated_date as updated_date  from beacon_mapping bm \n"
        + "left join beacon_devices bd on \n"
        + "bm.b1 = bd.device_id inner join \n"
        + "device_location_info dli on \n"
        + "bd.device_id = dli.device_id \n"
        + "where dli.location_name = '"+location_name+"'";

    Log.e(LOG, selectQuery);

    Cursor c = database.rawQuery(selectQuery, null);

    BeaconDevices beaconDevices = new BeaconDevices(context);
    beaconDevices.open();

    // looping through all rows and adding to list
    if(c.moveToFirst()){
      do{

        MappingBeacon mappingBeacon = new MappingBeacon(c.getString((c.getColumnIndex("direction"))),beaconDevices.getIDDeviceName(c.getString((c.getColumnIndex("b2")))) , c.getInt((c.getColumnIndex("distance"))), c.getInt((c.getColumnIndex("hops"))), c.getString((c.getColumnIndex("created_date"))),
            c.getString((c.getColumnIndex("updated_date"))), c.getString((c.getColumnIndex("id"))), c.getString(((c.getColumnIndex("mapping_message")))));

        mappingBeaconList.add(mappingBeacon);

      }while (c.moveToNext());
    }

    c.close();

    return mappingBeaconList;
  };

  public void delete(String id){
    database.delete(TABLE_BEACON_MAPPING, "id='" + id + "'",null);
  }

  public long update(MappingBeacon mappingBeacon){
    ContentValues values = new ContentValues();
    values.put("b1", mappingBeacon.getB1());
    values.put("b2", mappingBeacon.getB2());
    values.put("hops", mappingBeacon.getNumberofhops());
    values.put("distance", mappingBeacon.getDistance());
    values.put("direction", mappingBeacon.getDirection());
    values.put("updated_date", AppUtils.getDateTime());
    values.put("mapping_message", mappingBeacon.getMapping_message());

    try {

      // insert row
      long mapping_id = database.update(TABLE_BEACON_MAPPING, values, "id='" + mappingBeacon.getId() + "'", null);
      Log.d(LOG, "update: >>>>>>>>" + mapping_id);

      return mapping_id;

    }catch (SQLiteConstraintException e){
      Log.d(LOG, "update: " + e.getMessage());
      return -1;
    }
  }

  public List<GraphData> getMappingData(){
    List<GraphData> mappingBeaconList = new ArrayList<GraphData>();
    String selectQuery = "select * from beacon_mapping";

    Log.e(LOG, selectQuery);

    Cursor c = database.rawQuery(selectQuery, null);

    if(c.moveToFirst()){
      do{

        GraphData graphData = new GraphData(c.getInt((c.getColumnIndex("b1"))), c.getInt((c.getColumnIndex("b2"))), c.getInt((c.getColumnIndex("distance"))), c.getInt((c.getColumnIndex("hops"))), c.getString((c.getColumnIndex("direction"))));

        mappingBeaconList.add(graphData);

      }while (c.moveToNext());
    }

    c.close();

    return mappingBeaconList;
  }
}
