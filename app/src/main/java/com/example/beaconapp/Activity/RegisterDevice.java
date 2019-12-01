package com.example.beaconapp.Activity;

import static com.example.beaconapp.Utils.AppUtils.validateInputText;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.beaconapp.Helper.Tables.BeaconDevices;
import com.example.beaconapp.Helper.Tables.DeviceLocationINFO;
import com.example.beaconapp.Model.Database.DeviceLocationInfoDB;
import com.example.beaconapp.Model.Database.UfoDeviceDB;
import com.example.beaconapp.Model.UfoDeviceDetails;
import com.example.beaconapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import es.dmoral.toasty.Toasty;

public class RegisterDevice extends AppCompatActivity {

  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.location_name)
  TextInputEditText locationName;
  @BindView(R.id.board_cast_message)
  TextInputEditText boardCastMessage;
  @BindView(R.id.adddevice)
  Button adddevice;
  @BindView(R.id.location_name_input)
  TextInputLayout locationNameInput;
  @BindView(R.id.board_cast_message_input)
  TextInputLayout boardCastMessageInput;
  @BindView(R.id.rssi)
  TextInputEditText rssi;
  @BindView(R.id.board_cast_rssi)
  TextInputLayout boardCastRssi;

  private BeaconDevices beaconDevices;
  private UfoDeviceDB ufoDeviceDB;
  private DeviceLocationInfoDB deviceLocationInfoDB;
  private UfoDeviceDetails ufoDeviceDetails;
  private DeviceLocationINFO deviceLocationINFO;
  Boolean BeaconDelete = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register_device);

    ButterKnife.bind(this);

    beaconDevices = new BeaconDevices(this);
    beaconDevices.open();

    deviceLocationINFO = new DeviceLocationINFO(this);
    deviceLocationINFO.open();

    ufoDeviceDetails = getIntent().getParcelableExtra("deviceInfo");
    ufoDeviceDB = getIntent().getParcelableExtra("ufoDeviceInfo");

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
    getSupportActionBar()
        .setTitle(ufoDeviceDetails != null ? ufoDeviceDetails.getMacID() : ufoDeviceDB.getMacID());

    if (ufoDeviceDetails != null) {
      rssi.setText(ufoDeviceDetails.getRSSI());
      if (ufoDeviceDetails.getRegisterDevice()) {
        BeaconDelete = true;
//        locationName.setEnabled(false);
        adddevice.setText("UPDATE");
        locationName.setText(beaconDevices.getDeviceName(ufoDeviceDetails.getMacID()).trim());
        boardCastMessage.setText(beaconDevices.getBroadCastMessage(ufoDeviceDetails.getMacID()).trim());
        invalidateOptionsMenu();
      }
    } else if (ufoDeviceDB != null) {
      rssi.setText(ufoDeviceDB.getRSSI());
      if (ufoDeviceDB.getRegisterDevice()) {
        BeaconDelete = true;
//        locationName.setEnabled(false);
        adddevice.setText("UPDATE");
        locationName.setText(ufoDeviceDB.getDeviceLocationInfoDB().getLocationName().trim());
        boardCastMessage.setText(ufoDeviceDB.getDeviceLocationInfoDB().getBroadCastMessage().trim());
        invalidateOptionsMenu();
      }
    }

    adddevice.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.i(">>>>>>>>>>>>>>>>>>>>>", "onClick: " + validate());
        if (validate()) {
          deviceLocationInfoDB = new DeviceLocationInfoDB(locationName.getText().toString(), true,
              boardCastMessage.getText().toString());
          if (adddevice.getText() != "UPDATE") {
            if (!deviceLocationINFO.checkLocationName(locationName.getText().toString().trim())) {
              ufoDeviceDB = new UfoDeviceDB(ufoDeviceDetails.getDeviceType(),
                  ufoDeviceDetails.getMacID(), ufoDeviceDetails.getMajor(),
                  ufoDeviceDetails.getMinor(), ufoDeviceDetails.getUUID(),
                  ufoDeviceDetails.getTxPower(), ufoDeviceDetails.getRSSI(),
                  ufoDeviceDetails.getDistance(), ufoDeviceDetails.getTemp(),
                  ufoDeviceDetails.getLastUpdate(), ufoDeviceDetails.getScanRecord(),
                  !ufoDeviceDetails.getRegisterDevice(), deviceLocationInfoDB);

              long insertStatus = beaconDevices.insert(ufoDeviceDB);
              if (insertStatus != -1) {
                Toasty.success(getApplicationContext(), "Beacon Registered successfully",
                    Toast.LENGTH_SHORT, true)
                    .show();

                BeaconDelete = true;
//                locationName.setEnabled(false);
                invalidateOptionsMenu();
                adddevice.setText("UPDATE");

              } else {
                Toasty.error(getApplicationContext(), "Beacon Already Exist", Toast.LENGTH_SHORT,
                    true)
                    .show();
              }
            } else {
              locationNameInput.setError("Location already exists");
            }
          } else {

            UfoDeviceDB ufoDeviceDBupdate = new UfoDeviceDB();
            ufoDeviceDBupdate.setMacID(
                ufoDeviceDetails != null ? ufoDeviceDetails.getMacID() : ufoDeviceDB.getMacID());
            ufoDeviceDBupdate.setDeviceLocationInfoDB(deviceLocationInfoDB);
            ufoDeviceDBupdate.setRSSI(rssi.getText().toString());

            long updateStatus = beaconDevices.update(ufoDeviceDBupdate);
            if (updateStatus != -1) {
              Toasty.success(getApplicationContext(), "Beacon update successfully",
                  Toast.LENGTH_SHORT, true)
                  .show();

              BeaconDelete = true;
              invalidateOptionsMenu();
              adddevice.setText("UPDATE");


            } else {
              Toasty
                  .error(getApplicationContext(), "Beacon name Already Exists", Toast.LENGTH_SHORT,
                      true)
                  .show();
            }

          }
        }
      }
    });
  }

  private boolean validate() {
    return validateInputText("Location Name", locationName, locationNameInput, RegisterDevice.this)
        &&
        validateInputText("Broadcast Message", boardCastMessage, boardCastMessageInput,
            RegisterDevice.this);
  }

  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    super.onPrepareOptionsMenu(menu);
    if (BeaconDelete) {
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

    if (ufoDeviceDetails != null) {
      if (ufoDeviceDetails.getRegisterDevice()) {
        deletetem.setVisible(ufoDeviceDetails.getRegisterDevice());
      }
    } else if (ufoDeviceDB != null) {
      if (ufoDeviceDB.getRegisterDevice()) {
        deletetem.setVisible(ufoDeviceDB.getRegisterDevice());
      }
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
      beaconDevices
          .delete(ufoDeviceDetails != null ? ufoDeviceDetails.getMacID() : ufoDeviceDB.getMacID());
      BeaconDelete = true;

      Toasty
          .success(getApplicationContext(), "Beacon delete successfully", Toast.LENGTH_SHORT, true)
          .show();

      onBackPressed();
    }

    return super.onOptionsItemSelected(item);
  }

}
