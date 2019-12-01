package com.example.beaconapp.Activity;

import static com.example.beaconapp.Utils.AppUtils.validateInputText;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.beaconapp.Helper.Tables.BeaconDevices;
import com.example.beaconapp.Helper.Tables.MappingBeaconTBL;
import com.example.beaconapp.Model.Database.MappingBeaconDB;
import com.example.beaconapp.Model.MappingBeacon;
import com.example.beaconapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import es.dmoral.toasty.Toasty;
import java.util.ArrayList;
import java.util.List;

public class DeviceMapping extends AppCompatActivity {

  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.beaconLocation)
  Spinner beaconLocation;
  @BindView(R.id.beacondirection)
  Spinner beacondirection;
  @BindView(R.id.hops)
  TextInputEditText hops;
  @BindView(R.id.distance)
  TextInputEditText distance;
  @BindView(R.id.addmapping)
  Button addmapping;
  @BindView(R.id.hops_input)
  TextInputLayout hopsInput;
  @BindView(R.id.distance_input)
  TextInputLayout distanceInput;
  Boolean mappingDelete = false;
  @BindView(R.id.mapping_message)
  TextInputEditText mappingMessage;
  @BindView(R.id.mapping_message_input)
  TextInputLayout mappingMessageInput;

  private BeaconDevices beaconDevices;
  private MappingBeaconTBL mappingBeaconTBL;
  private MappingBeacon mappingBeacon;
  private MappingBeaconDB mappingBeaconDB;
  Bundle bundle;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_device_mapping);

    beaconDevices = new BeaconDevices(this);
    beaconDevices.open();

    mappingBeaconTBL = new MappingBeaconTBL(this);
    mappingBeaconTBL.open();

    bundle = getIntent().getExtras();

    List<String> ufoDeviceList = new ArrayList<String>();
    ufoDeviceList = beaconDevices.getSelectedBeacon(bundle.getString("location1"));
    ufoDeviceList.add(0, "Select Location");

    ButterKnife.bind(this);

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
    getSupportActionBar().setTitle(bundle.getString("location1"));

    List<String> direction = new ArrayList<String>();
    direction.add("Select Direction");
    direction.add("East");
    direction.add("West");
    direction.add("North");
    direction.add("South");

    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_spinner_item, ufoDeviceList);
    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    beaconLocation.setAdapter(dataAdapter);

    ArrayAdapter<String> dataAdapterDirection = new ArrayAdapter<String>(this,
        android.R.layout.simple_spinner_item, direction);
    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    beacondirection.setAdapter(dataAdapterDirection);

    addmapping.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (addmapping.getText() == "ADD") {
          if (validate()) {
            if (beaconLocation.getSelectedItem().toString().trim()
                .equalsIgnoreCase("Select Location")) {
              Toasty.warning(DeviceMapping.this, "Please select location",
                  Toast.LENGTH_SHORT).show();
            } else if (beacondirection.getSelectedItem().toString().trim()
                .equalsIgnoreCase("Select Direction")) {
              Toasty.warning(DeviceMapping.this, "Please select direction",
                  Toast.LENGTH_SHORT).show();
            } else {
              Integer b1 = beaconDevices.getDeviceID(bundle.getString("location1"));
              Integer b2 = beaconDevices.getDeviceID(beaconLocation.getSelectedItem().toString());
              MappingBeacon mappingBeacon = new MappingBeacon(
                  beacondirection.getSelectedItem().toString(), mappingMessage.getText().toString(), b1, b2,
                  Integer.valueOf(distance.getText().toString()),
                  Integer.valueOf(hops.getText().toString()));

              long insertStatus = mappingBeaconTBL.insert(mappingBeacon);
              if (insertStatus != -1) {
                Toasty
                    .success(getApplicationContext(), "Mapping Added successfully",
                        Toast.LENGTH_SHORT,
                        true)
                    .show();

                mappingDelete = true;
                invalidateOptionsMenu();
                addmapping.setText("UPDATE");

              } else {
                Toasty.error(getApplicationContext(), "Mapping Exist", Toast.LENGTH_SHORT, true)
                    .show();
              }
            }
          }
        } else {
          if (validate()) {
            if (beaconLocation.getSelectedItem().toString().trim()
                .equalsIgnoreCase("Select Location")) {
              Toasty.warning(DeviceMapping.this, "Please select location",
                  Toast.LENGTH_SHORT).show();
            } else if (beacondirection.getSelectedItem().toString().trim()
                .equalsIgnoreCase("Select Direction")) {
              Toasty.warning(DeviceMapping.this, "Please select direction",
                  Toast.LENGTH_SHORT).show();
            } else {

              Integer b1 = beaconDevices.getDeviceID(bundle.getString("location1"));
              Integer b2 = beaconDevices.getDeviceID(beaconLocation.getSelectedItem().toString());
              MappingBeacon mappingBeacon = new MappingBeacon(
                  beacondirection.getSelectedItem().toString(), b1, b2,
                  Integer.valueOf(distance.getText().toString()),
                  Integer.valueOf(hops.getText().toString()), bundle.getString("id"), mappingMessage.getText().toString());

              long insertStatus = mappingBeaconTBL.update(mappingBeacon);
              if (insertStatus != -1) {
                Toasty
                    .success(getApplicationContext(), "Mapping Updated successfully",
                        Toast.LENGTH_SHORT,
                        true)
                    .show();

                mappingDelete = true;
                invalidateOptionsMenu();
                addmapping.setText("UPDATE");

              } else {
                Toasty
                    .error(getApplicationContext(), "Mapping updated problems", Toast.LENGTH_SHORT,
                        true)
                    .show();
              }
            }
          }
        }
      }
    });

    if (bundle.getString("location2") != null) {
      addmapping.setText("UPDATE");
      hops.setText(String.valueOf(bundle.getInt("hops")));
      distance.setText(String.valueOf(bundle.getInt("distance")));
      int pos1 = dataAdapter.getPosition(bundle.getString("location2"));
      beaconLocation.setSelection(pos1);
      int pos2 = dataAdapterDirection.getPosition(bundle.getString("direction"));
      beacondirection.setSelection(pos2);
      mappingMessage.setText(bundle.getString("message"));
      mappingDelete = true;
      ufoDeviceList.add(1, bundle.getString("location2"));
      beaconLocation.setSelection(dataAdapter.getPosition(bundle.getString("location2")));
      invalidateOptionsMenu();
    } else {
      addmapping.setText("ADD");
    }

  }

  private boolean validate() {
    return validateInputText("Hops", hops, hopsInput, DeviceMapping.this) &&
        validateInputText("Distance (In Steps)", distance, distanceInput, DeviceMapping.this) && validateInputText("Message", mappingMessage, mappingMessageInput, DeviceMapping.this);
  }

  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    super.onPrepareOptionsMenu(menu);
    if (mappingDelete) {
      menu.findItem(R.id.action_delete).setVisible(true);
    } else {
      menu.findItem(R.id.action_delete).setVisible(false);
    }
    return true;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
//    getMenuInflater().inflate(R.menu.delete, menu);
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.delete, menu);
    MenuItem deletetem = menu.findItem(R.id.action_delete);
    if (bundle.getString("location2") != null) {
      deletetem.setVisible(true);
    } else {
      deletetem.setVisible(false);
    }
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_delete) {
      mappingBeaconTBL.delete(bundle.getString("id"));
      Toasty
          .success(getApplicationContext(), "Beacon delete successfully", Toast.LENGTH_SHORT, true)
          .show();

      onBackPressed();
    }

    return super.onOptionsItemSelected(item);
  }
}
