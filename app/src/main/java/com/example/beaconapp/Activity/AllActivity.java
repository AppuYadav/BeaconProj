package com.example.beaconapp.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.beaconapp.Adapter.RegisterDeviceListAdapter.OnItemClickListener;
import com.example.beaconapp.Adapter.RegisterDeviceListAdapter;
import com.example.beaconapp.Helper.Tables.BeaconDevices;
import com.example.beaconapp.Model.Database.DeviceLocationInfoDB;
import com.example.beaconapp.Model.Database.UfoDeviceDB;
import com.example.beaconapp.Model.UfoDeviceDetails;
import com.example.beaconapp.R;
import java.util.ArrayList;
import java.util.List;

public class AllActivity extends AppCompatActivity implements OnItemClickListener {

  private androidx.appcompat.widget.Toolbar toolbar;
  RecyclerView recyclerView;
  RegisterDeviceListAdapter registerDeviceListAdapter;
  private List<UfoDeviceDB> ufoDeviceList = new ArrayList<UfoDeviceDB>();
  private BeaconDevices beaconDevices;
  private SearchView searchView;

  @Override
  protected void onResume() {
    super.onResume();
    beaconDevices = new BeaconDevices(this);
    beaconDevices.open();
    ufoDeviceList = beaconDevices.getBeaconDeviceAll();
    recyclerView = findViewById(R.id.register_device);
    registerDeviceListAdapter = new RegisterDeviceListAdapter(ufoDeviceList, this);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(registerDeviceListAdapter);
    registerDeviceListAdapter.setOnItemClickListener(this);
    registerDeviceListAdapter.notifyDataSetChanged();
    invalidateOptionsMenu();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_all);

    toolbar = findViewById(R.id.my_toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
    getSupportActionBar().setTitle(R.string.title_all_register);

//    beaconDevices = new BeaconDevices(this);
//    beaconDevices.open();
//    ufoDeviceList = beaconDevices.getBeaconDeviceAll();
//    recyclerView = findViewById(R.id.register_device);
//    registerDeviceListAdapter = new RegisterDeviceListAdapter(ufoDeviceList, this);
//    recyclerView.setLayoutManager(new LinearLayoutManager(this));
//    recyclerView.setAdapter(registerDeviceListAdapter);
//    registerDeviceListAdapter.setOnItemClickListener(this);
//    registerDeviceListAdapter.notifyDataSetChanged();
//    invalidateOptionsMenu();
  }

  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
  }

  @Override
  public void onItemClick(int position) {
    UfoDeviceDB data = ufoDeviceList.get(position);
    Intent intent = new Intent(this, RegisterDevice.class);
    UfoDeviceDB ufoDeviceDB = new UfoDeviceDB(data.getDeviceType(), data.getMacID(), data.getMajor(), data.getMinor(), data.getRegisterDevice(), new DeviceLocationInfoDB(data.getDeviceLocationInfoDB().getLocationName(), data.getRegisterDevice(), data.getDeviceLocationInfoDB().getBroadCastMessage()), data.getCreated_date(), data.getRSSI());
    intent.putExtra("ufoDeviceInfo",ufoDeviceDB);
    startActivity(intent);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.search, menu);

    // Associate searchable configuration with the SearchView
    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
    searchView = (SearchView) menu.findItem(R.id.action_search)
        .getActionView();
    searchView.setSearchableInfo(searchManager
        .getSearchableInfo(getComponentName()));
    searchView.setMaxWidth(Integer.MAX_VALUE);

    // listening to search query text change
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        // filter recycler view when query submitted
        registerDeviceListAdapter.getFilter().filter(query);
        return false;
      }

      @Override
      public boolean onQueryTextChange(String query) {
        // filter recycler view when text is changed
        registerDeviceListAdapter.getFilter().filter(query);
        return false;
      }
    });
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_search) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
