package com.example.beaconapp.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.beaconapp.Adapter.RegisterDeviceListAdapter;
import com.example.beaconapp.Adapter.TagAdapter.OnItemClickListener;
import com.example.beaconapp.Adapter.TagAdapter;
import com.example.beaconapp.Helper.Tables.BeaconDevices;
import com.example.beaconapp.Helper.Tables.MappingTagTBL;
import com.example.beaconapp.Model.Database.Tag;
import com.example.beaconapp.R;
import java.util.ArrayList;
import java.util.List;

public class AllTag extends AppCompatActivity implements OnItemClickListener {

  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.tag_device)
  RecyclerView tagDevice;
  private List<Tag> tagList = new ArrayList<Tag>();
  private MappingTagTBL mappingTagTBL;
  RecyclerView recyclerView;
  TagAdapter tagAdapter;
  private SearchView searchView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_all_tag);

    ButterKnife.bind(this);

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
    getSupportActionBar().setTitle(R.string.title_all_register_tag);
  }

  @Override
  protected void onResume() {
    super.onResume();
    mappingTagTBL = new MappingTagTBL(this);
    mappingTagTBL.open();
    tagList = mappingTagTBL.getTagDeviceAll();
    recyclerView = findViewById(R.id.tag_device);
    tagAdapter = new TagAdapter(tagList, this);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(tagAdapter);
    tagAdapter.setOnItemClickListener(this);
    tagAdapter.notifyDataSetChanged();
    invalidateOptionsMenu();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.beacon_device, menu);
    MenuItem scan = menu.findItem(R.id.action_scan);
    scan.setVisible(false);
    MenuItem stop = menu.findItem(R.id.action_stop);
    stop.setVisible(false);

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
        tagAdapter.getFilter().filter(query);
        return false;
      }

      @Override
      public boolean onQueryTextChange(String query) {
        // filter recycler view when text is changed
        tagAdapter.getFilter().filter(query);
        return false;
      }
    });
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_add) {
      Intent intent = new Intent(this, AddTag.class);
      startActivity(intent);
      return true;
    }else if (id == R.id.action_search) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
  }

  @Override
  public void onItemClick(int position) {
    Tag tag = tagList.get(position);
    Intent intent = new Intent(this, AddTag.class);
    intent.putExtra("tagInfo",tag);
    startActivity(intent);
  }
}
