package com.example.beaconapp.Activity;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.beaconapp.Adapter.DeviceListAdapter;
import com.example.beaconapp.Adapter.DeviceListAdapter.OnItemClickListener;
import com.example.beaconapp.Helper.Tables.BeaconDevices;
import com.example.beaconapp.Model.UfoDevice;
import com.example.beaconapp.Model.UfoDeviceDetails;
import com.example.beaconapp.R;
import com.example.beaconapp.Service.TrackerService;
import com.example.beaconapp.Utils.SharedPrefManager;
import com.github.ybq.android.spinkit.style.Pulse;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.ufobeaconsdk.callback.OnFailureListener;
import com.ufobeaconsdk.callback.OnScanSuccessListener;
import com.ufobeaconsdk.callback.OnSuccessListener;
import com.ufobeaconsdk.main.UFOBeaconManager;
import com.ufobeaconsdk.main.UFODevice;
import es.dmoral.toasty.Toasty;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class ScanBeaconActivity extends AppCompatActivity implements OnItemClickListener {
  private Drawable newdrawable;
  private Toolbar toolbar;
  boolean doubleBackToExitPressedOnce = false;
  UFOBeaconManager ufoBeaconManager;
  BluetoothAdapter mBluetoothAdapter;
  private static final int PERMISSION_REQUEST_CODE = 200;
  androidx.appcompat.app.AlertDialog.Builder dialogBuilder;
  androidx.appcompat.app.AlertDialog alertDialog;
  LinkedHashMap<String, UfoDevice> ufoDeviceLinkedHashMap = new LinkedHashMap<String, UfoDevice>();
  RecyclerView recyclerView;
  DeviceListAdapter deviceListAdapter;
  Boolean scanDevice = false;
  ProgressBar loader;
  ImageView imageView;
  FloatingTextButton fab;
  private static final String TAG = MainActivity.class.getSimpleName();
  private BeaconDevices beaconDevices;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_scan_beacon);

    loader = findViewById(R.id.loader);
    imageView = findViewById(R.id.imageView);
    fab = findViewById(R.id.fab);
    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    ufoBeaconManager = new UFOBeaconManager(ScanBeaconActivity.this);
    toolbar = (Toolbar) findViewById(R.id.my_toolbar);

    Pulse pulse = new Pulse();
    loader.setIndeterminateDrawable(pulse);

    beaconDevices = new BeaconDevices(this);
    beaconDevices.open();

    toolbar = (Toolbar) findViewById(R.id.my_toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
    getSupportActionBar().setTitle(R.string.title_scan);

    recyclerView = findViewById(R.id.scanDevice);
    deviceListAdapter = new DeviceListAdapter(ufoDeviceLinkedHashMap, this);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(deviceListAdapter);
    deviceListAdapter.setOnItemClickListener(this);

    if(SharedPrefManager.getInstance(ScanBeaconActivity.this).getAutoON()){
      scanDevice = true;
      imageView.setVisibility(View.GONE);
      loader.setVisibility(View.VISIBLE);
      invalidateOptionsMenu();
      if(!checkPermission()) {
        requestPermission();
      }else if (!mBluetoothAdapter.isEnabled()) {
        showBottomSheetDialog();
      }else{
        startScan();
      }
    }
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
    MenuItem scan = menu.findItem(R.id.action_search);
    scan.setVisible(false);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_scan) {
      scanDevice = true;
      loader.setVisibility(View.VISIBLE);
      imageView.setVisibility(View.GONE);
      invalidateOptionsMenu();
      if(!checkPermission()) {
        requestPermission();
      }else if (!mBluetoothAdapter.isEnabled()) {
        showBottomSheetDialog();
      }else{
        startScan();
      }
    }else if(id == R.id.action_stop){
      scanDevice = false;
      ufoDeviceLinkedHashMap.clear();
      deviceListAdapter.notifyDataSetChanged();
      loader.setVisibility(View.GONE);
      imageView.setVisibility(View.VISIBLE);
      invalidateOptionsMenu();
      stopScan();
    }else if(id == R.id.action_add){
      Intent intent = new Intent(this, ExportActivity.class);
      startActivity(intent);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    MenuItem stop = menu.findItem(R.id.action_stop);
    MenuItem scan = menu.findItem(R.id.action_scan);
    if(scanDevice){
      stop.setVisible(true);
      scan.setVisible(false);
    }else{
      scan.setVisible(true);
      stop.setVisible(false);
    }
    return super.onPrepareOptionsMenu(menu);
  }


  public void stopScan(){
    ufoBeaconManager.stopScan(new OnSuccessListener() {
      @Override
      public void onSuccess(boolean isStop) {
        if (isStop) {
          loader.setVisibility(View.GONE);
          ufoDeviceLinkedHashMap.clear();
          deviceListAdapter.notifyDataSetChanged();
          imageView.setVisibility(View.VISIBLE);
          Toasty.success(getApplicationContext(), "Scan stop successfully", Toast.LENGTH_SHORT, true)
              .show();
        }
      }
    }, new OnFailureListener() {
      @Override
      public void onFailure(final int code, final String message) {
        Toasty.warning(getApplicationContext(), message, Toast.LENGTH_SHORT, true).show();
      }
    });
  }

  private boolean checkPermission() {
    int result = ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION);
    return result == PackageManager.PERMISSION_GRANTED;
  }

  private void requestPermission() {
    ActivityCompat.requestPermissions(this,
        new String[]{ACCESS_FINE_LOCATION},
        PERMISSION_REQUEST_CODE);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
    switch (requestCode) {
      case PERMISSION_REQUEST_CODE:
        if (grantResults.length > 0) {

          boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

          if (locationAccepted) {
            Toasty.success(this, "Permission Granted, Now you can access location data.",
                Toast.LENGTH_SHORT, true).show();
            if (!mBluetoothAdapter.isEnabled()) {
              showBottomSheetDialog();
            } else {
              startScan();
            }
          } else {
            ufoDeviceLinkedHashMap.clear();
            scanDevice = false;
            imageView.setVisibility(View.VISIBLE);
            loader.setVisibility(View.GONE);
            invalidateOptionsMenu();
            Toasty.info(getApplicationContext(), "Permission Denied, You cannot access location data.", Toast.LENGTH_SHORT, true).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
              if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                showMessageOKCancel("You need to allow access the permissions",
                    new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                          requestPermissions(new String[]{ACCESS_FINE_LOCATION},
                              PERMISSION_REQUEST_CODE);
                        }
                      }
                    });
                return;
              }
            }

          }
        }
        break;
    }
  }

  private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
    new AlertDialog.Builder(this)
        .setMessage(message)
        .setPositiveButton("OK", okListener)
        .setNegativeButton("Cancel", null)
        .create()
        .show();
  }

  private void showAlertDialog(int layout){
    dialogBuilder = new Builder(this);
    View layoutView = getLayoutInflater().inflate(layout, null);
    dialogBuilder.setView(layoutView);
    alertDialog = dialogBuilder.create();
    alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    alertDialog.show();
  }

  @Override
  public void onItemClick(int position) {
    UfoDevice data = (new ArrayList<UfoDevice>(ufoDeviceLinkedHashMap.values())).get(position);
    Intent intent = new Intent(this, RegisterDevice.class);
    UfoDeviceDetails ufoDeviceDetails = new UfoDeviceDetails(data.getDeviceType(), data.getMacID(), data.getMajor(), data.getMinor(), data.getUUID(), data.getTxPower(), data.getRSSI(), data.getDistance(), data.getTemp(), data.getLastUpdate(), data.getScanRecord(), data.getRegisterDevice());
    intent.putExtra("deviceInfo",ufoDeviceDetails);
    startActivity(intent);
  }

  public void startScan(){
    ufoBeaconManager.startScan(new OnScanSuccessListener() {
      @Override
      public void onSuccess(final UFODevice ufodevice) {
//        startService(new Intent(getApplicationContext(), TrackerService.class));
        UfoDevice ufoDevice = new UfoDevice(ufodevice.getDeviceType().name(), ufodevice.getBtdevice().getAddress(), String.valueOf(ufodevice.getMajor()), String.valueOf(ufodevice.getMinor()),
            ufodevice.getProximityUUID(), String.valueOf(ufodevice.getTxPower()), String.valueOf(ufodevice.getRssi()), String.valueOf(ufodevice.getDistance()), ufodevice.getTemperature(), String.valueOf(ufodevice.getDate()), ufodevice.getScanRecord(), beaconDevices.getDeviceStatus(ufodevice.getBtdevice().getAddress()));
        ufoDeviceLinkedHashMap.put(ufodevice.getBtdevice().getAddress(), ufoDevice);
        Log.i(TAG, ">>>>>>>>>>>>>>>>");
        if(ufoDeviceLinkedHashMap.size() != 0){
          loader.setVisibility(View.GONE);
        }else{
          loader.setVisibility(View.VISIBLE);
        }
        deviceListAdapter.notifyDataSetChanged();
      }
    }, new OnFailureListener() {
      @Override
      public void onFailure(final int code, final String message) {
        loader.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);
        Toasty.warning(ScanBeaconActivity.this, message, Toast.LENGTH_SHORT, true).show();
        Log.i("UFO Error>>>>>", String.valueOf(message));
      }
    });

  }

  public void showBottomSheetDialog() {
    View view = getLayoutInflater().inflate(R.layout.bluetooth_enable_bottom_sheet, null);
    BottomSheetDialog dialog = new BottomSheetDialog(this);
    dialog.setCanceledOnTouchOutside(false);
    dialog.setContentView(view);
    dialog.show();
    Button btnDeny = view.findViewById(R.id.btnDeny);
    Button btnAllow = view.findViewById(R.id.btnAllow);

    btnDeny.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Toasty.info(getApplicationContext(), "Please enable Blutooth from settings.", Toast.LENGTH_SHORT, true).show();
        scanDevice = false;
        imageView.setVisibility(View.VISIBLE);
        loader.setVisibility(View.GONE);
        invalidateOptionsMenu();
        dialog.dismiss();
      }
    });

    btnAllow.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        ufoBeaconManager.enable(new OnSuccessListener() {
          @Override
          public void onSuccess(boolean isSuccess) {
            dialog.dismiss();
            imageView.setVisibility(View.GONE);
            loader.setVisibility(View.VISIBLE);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
              @Override
              public void run() {
                Toasty.success(ScanBeaconActivity.this, "Yout device bluetooth is on.", Toast.LENGTH_SHORT, true).show();
                startScan();
              }
            }, 1000);
          }
        }, new OnFailureListener() {
          @Override
          public void onFailure(int code, String message) {
            dialog.dismiss();
            Toasty.warning(ScanBeaconActivity.this, "Your device bluetooth problem.", Toast.LENGTH_SHORT, true).show();
          }
        });
      }
    });
  }

  @Override
  public void onBackPressed() {
    stopScan();
    super.onBackPressed();
  }
}
