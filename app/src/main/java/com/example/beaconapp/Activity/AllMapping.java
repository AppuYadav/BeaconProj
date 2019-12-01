package com.example.beaconapp.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.beaconapp.Adapter.MappingList;
import com.example.beaconapp.Adapter.MappingList.OnItemClickListener;
import com.example.beaconapp.Helper.Tables.MappingBeaconTBL;
import com.example.beaconapp.Model.Database.MappingBeaconDB;
import com.example.beaconapp.Model.MappingBeacon;
import com.example.beaconapp.R;
import java.util.ArrayList;
import java.util.List;

public class AllMapping extends AppCompatActivity implements OnItemClickListener {

  @BindView(R.id.toolbar)
  Toolbar toolbar;
  public Bundle bundle;
  RecyclerView recyclerView;
  private List<MappingBeacon> mappingBeaconList = new ArrayList<MappingBeacon>();
  MappingBeaconTBL mappingBeaconTBL;
  MappingList mappingList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_all_mapping);
    ButterKnife.bind(this);

    bundle = getIntent().getExtras();

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
    getSupportActionBar().setTitle(bundle.getString("location1"));
  }

  @Override
  protected void onResume() {
    super.onResume();
    mappingBeaconTBL = new MappingBeaconTBL(this);
    mappingBeaconTBL.open();
    mappingBeaconList = mappingBeaconTBL.getMappingDeviceAll(bundle.getString("location1"));
    recyclerView = findViewById(R.id.mappingDevice);
    mappingList = new MappingList(mappingBeaconList, this);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(mappingList);
    mappingList.setOnItemClickListener(this);
    mappingList.notifyDataSetChanged();
    invalidateOptionsMenu();
  }

  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.beacon_device, menu);
    MenuItem scan = menu.findItem(R.id.action_scan);
    scan.setVisible(false);
    MenuItem stop = menu.findItem(R.id.action_stop);
    stop.setVisible(false);
    MenuItem search = menu.findItem(R.id.action_search);
    search.setVisible(false);

    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_add) {
      Intent intent = new Intent(this, DeviceMapping.class);
      intent.putExtras(bundle);
      startActivity(intent);
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onItemClick(int position) {
    MappingBeacon mappingBeacon = mappingBeaconList.get(position);
//    MappingBeaconDB mappingBeaconDB = new MappingBeaconDB(mappingBeacon.getNumberofhops(), mappingBeacon.getDistance(), mappingBeacon.getDirection(), mappingBeacon.getLocation_name());
    Bundle bundle1 = new Bundle();
    bundle1.putString("location1", bundle.getString("location1"));
    bundle1.putString("id", mappingBeacon.getId());
    bundle1.putInt("distance", mappingBeacon.getDistance());
    bundle1.putInt("hops", mappingBeacon.getNumberofhops());
    bundle1.putString("direction", mappingBeacon.getDirection());
    bundle1.putString("location2", mappingBeacon.getLocation_name());
    bundle1.putString("message", mappingBeacon.getMapping_message());


    Intent intent = new Intent(this, DeviceMapping.class);
    intent.putExtras(bundle1);
    startActivity(intent);
  }
}
