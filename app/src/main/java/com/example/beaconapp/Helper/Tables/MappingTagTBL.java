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
import com.example.beaconapp.Model.Database.Tag;
import com.example.beaconapp.Model.Database.UfoDeviceDB;
import com.example.beaconapp.Utils.AppUtils;
import com.example.beaconapp.Utils.Utils;
import java.util.ArrayList;
import java.util.List;

public class MappingTagTBL {

  // LOG TAG
  private static final String LOG = "MappingTagTBL";

  // Tables Name
  private static final String TABLE_TAG_MAPPING = "tag_mapping";

  private DatabaseHelper dbHelper;

  private Context context;

  private SQLiteDatabase database;

  public MappingTagTBL(Context context) {
    this.context = context;
  }

  public MappingTagTBL open() throws SQLException {
    dbHelper = new DatabaseHelper(context);
    database = dbHelper.getWritableDatabase();
    return this;
  }

  private static final String RFID_MAPPING_TBL = "create table "+TABLE_TAG_MAPPING+" (id integer primary key autoincrement, tag_id text unique not null, location_name text not null, message text not null, created_date text, updated_date text)";

  public static String getTableTagMapping() {
    return RFID_MAPPING_TBL;
  }

  public long update(Tag tag){
      ContentValues values = new ContentValues();
      values.put("location_name", tag.getVerboseTitle());
      values.put("message", tag.getMessage());
      values.put("updated_date", AppUtils.getDateTime());

      database.update(TABLE_TAG_MAPPING, values,"tag_id='" + tag.getTagID() + "'", null);
      return 1;
  }

  public void delete(String tag_id){
    database.delete(TABLE_TAG_MAPPING, "tag_id='" + tag_id + "'",null);
  }

  public List<Tag> getTagDeviceAll(){
    List<Tag> tagList = new ArrayList<Tag>();

    String selectQuery = "select * from tag_mapping";

    Log.e(LOG, selectQuery);

    Cursor c = database.rawQuery(selectQuery, null);

    // looping through all rows and adding to list
    if(c.moveToFirst()){
      do{
        Tag tag = new Tag(c.getString((c.getColumnIndex("tag_id"))), c.getString((c.getColumnIndex("location_name"))), c.getString((c.getColumnIndex("message"))), c.getString((c.getColumnIndex("created_date"))), c.getString((c.getColumnIndex("updated_date"))));
        tagList.add(tag);
      }while (c.moveToNext());
    }

    c.close();

    return tagList;
  };

  public long insert(Tag tag) {
    ContentValues values = new ContentValues();
    values.put("tag_id", tag.getTagID());
    values.put("location_name", tag.getVerboseTitle());
    values.put("message", tag.getMessage());
    values.put("created_date", AppUtils.getDateTime());
    values.put("updated_date", AppUtils.getDateTime());


    try {

      // insert row
      long device_id = database.insertOrThrow(TABLE_TAG_MAPPING, null, values);
      Log.d(LOG, "insert: >>>>>>>>" + device_id);
      return device_id;

    }catch (SQLiteConstraintException e){
      Log.d(LOG, "insert: " + e.getMessage());
      return -1;
    }
  }

  public String getIDToNameTag(String id){
    String query = "select message from tag_mapping where tag_id = '"+id+"'";

    Log.e(LOG, query);

    Cursor c = database.rawQuery(query, null);
    if (c.getCount() == 1)
    {
      c.moveToFirst();
      String message = c.getString(0);
      // since name is in position 1 ie first coloumn
      return message;
    }
    return null;
  }

}
