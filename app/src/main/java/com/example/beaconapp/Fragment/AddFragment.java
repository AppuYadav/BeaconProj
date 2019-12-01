package com.example.beaconapp.Fragment;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.beaconapp.Activity.MainActivity;
import com.example.beaconapp.Activity.RegisterDevice;
import com.example.beaconapp.Activity.SettingActivity;
import com.example.beaconapp.Adapter.DeviceListAdapter;
import com.example.beaconapp.Model.UfoDevice;
import com.example.beaconapp.Model.UfoDeviceDetails;
import com.example.beaconapp.R;
import com.example.beaconapp.Utils.SharedPrefManager;
import com.github.ybq.android.spinkit.style.Pulse;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ufobeaconsdk.callback.OnFailureListener;
import com.ufobeaconsdk.callback.OnScanSuccessListener;
import com.ufobeaconsdk.callback.OnSuccessListener;
import com.ufobeaconsdk.main.UFOBeaconManager;
import com.ufobeaconsdk.main.UFODevice;
import es.dmoral.toasty.Toasty;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class AddFragment extends Fragment implements DeviceListAdapter.OnItemClickListener {

  BluetoothAdapter mBluetoothAdapter;
  UFOBeaconManager ufoBeaconManager;
  FloatingTextButton fab;
  ProgressBar loader;
  ImageView imageView;
  private static final int PERMISSION_REQUEST_CODE = 200;
//  public List<UfoDevice> ufoDeviceLists = new ArrayList<>();
  LinkedHashMap<String, UfoDevice> ufoDeviceLinkedHashMap = new LinkedHashMap<String, UfoDevice>();
  RecyclerView recyclerView;
  DeviceListAdapter deviceListAdapter;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    setHasOptionsMenu(true);
    return inflater.inflate(R.layout.fragment_add, container, false);
  }


  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    fab = getView().findViewById(R.id.fab);
    loader = getView().findViewById(R.id.loader);
    imageView = getView().findViewById(R.id.imageView);
    ufoBeaconManager = new UFOBeaconManager(getContext());

    recyclerView = getView().findViewById(R.id.scanDevice);
    deviceListAdapter = new DeviceListAdapter(ufoDeviceLinkedHashMap, getContext());
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.setAdapter(deviceListAdapter);
    deviceListAdapter.setOnItemClickListener(this);

    Pulse pulse = new Pulse();
    loader.setIndeterminateDrawable(pulse);

    if(!checkPermission()) {
        requestPermission();
    }else{
      if(SharedPrefManager.getInstance(getContext()).getAutoON()){
        if(!SharedPrefManager.getInstance(getContext()).getBeaconManagerStart()){
          if (!mBluetoothAdapter.isEnabled()) {
            showBottomSheetDialog();
          } else {
            ufoBeaconManager.startScan(new OnScanSuccessListener() {
              @Override
              public void onSuccess(final UFODevice ufodevice) {
                fab.setTitle("Stop");
                loader.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                SharedPrefManager.getInstance(getContext()).setBeaconManagerStart(true);
                Log.i("UFO Correct>>>>>", String.valueOf(ufodevice.getScanRecord()));
              }
            }, new OnFailureListener() {
              @Override
              public void onFailure(final int code, final String message) {
                Log.i("UFO Error>>>>>", String.valueOf(message));
              }
            });
          }
        }
      }else{
        fab.setTitle("Scan");
      }
    }


    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        if(!checkPermission()) {
          requestPermission();
        }else {
          Log.i("<<<<<<<<<<<<", String.valueOf(ufoBeaconManager.isScanRunning()));
          if (SharedPrefManager.getInstance(getContext()).getBeaconManagerStart()) {
            fab.setEnabled(false);
            fab.setClickable(true);
            Log.i("<<<<<<<<<<<<12", String.valueOf("Inner"));
            ufoBeaconManager.stopScan(new OnSuccessListener() {
              @Override
              public void onSuccess(boolean isStop) {
                Log.i(">>>>>>>>>>>", "onSuccess: " + String.valueOf(isStop));
                if (isStop) {
                  fab.setEnabled(true);
                  fab.setClickable(true);
                  fab.setTitle("Scan");
                  loader.setVisibility(View.GONE);
                  imageView.setVisibility(View.VISIBLE);
                  SharedPrefManager.getInstance(getContext()).setBeaconManagerStart(false);
                  Toasty.success(getContext(), "Scan stop successfully", Toast.LENGTH_SHORT, true)
                      .show();
                }
              }
            }, new OnFailureListener() {
              @Override
              public void onFailure(final int code, final String message) {
                fab.setEnabled(true);
                fab.setClickable(true);
                Toasty.warning(getContext(), message, Toast.LENGTH_SHORT, true).show();
              }
            });
          } else {

            if (!mBluetoothAdapter.isEnabled()) {
              showBottomSheetDialog();
            } else {
              fab.setEnabled(false);
              fab.setClickable(false);
              fab.setVisibility(View.GONE);
              imageView.setVisibility(View.GONE);
              loader.setVisibility(View.VISIBLE);
              runBeaconManager();
            }
          }
        }
      }
    });
  }

  public void runBeaconManager(){
    ufoBeaconManager.startScan(new OnScanSuccessListener() {
      @Override
      public void onSuccess(final UFODevice ufodevice) {
        fab.setEnabled(true);
        fab.setClickable(true);
        fab.setTitle("Stop");
        loader.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
        fab.setVisibility(View.VISIBLE);
        UfoDevice ufoDevice = new UfoDevice(ufodevice.getDeviceType().name(), ufodevice.getBtdevice().getAddress(), String.valueOf(ufodevice.getMajor()), String.valueOf(ufodevice.getMinor()),
            ufodevice.getProximityUUID(), String.valueOf(ufodevice.getTxPower()), String.valueOf(ufodevice.getRssi()), String.valueOf(ufodevice.getDistance()), ufodevice.getTemperature(), String.valueOf(ufodevice.getDate()), ufodevice.getScanRecord(), false);
        ufoDeviceLinkedHashMap.put(ufodevice.getBtdevice().getAddress(), ufoDevice);
        deviceListAdapter.notifyDataSetChanged();
        Log.i("UFO Correct>>>>>", String.valueOf(ufodevice.getModelId()));
      }
    }, new OnFailureListener() {
      @Override
      public void onFailure(final int code, final String message) {
        fab.setEnabled(true);
        fab.setClickable(true);
        if(code == 1006){
          ufoBeaconManager.stopScan(new OnSuccessListener() {
            @Override
            public void onSuccess(boolean isStop) {
              Log.i(">>>>>>>>>>>", "onSuccess: " + String.valueOf(isStop));
              if (isStop) {
                fab.setEnabled(true);
                fab.setClickable(true);
                fab.setTitle("Scan");
                loader.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                fab.setVisibility(View.VISIBLE);
                SharedPrefManager.getInstance(getContext()).setBeaconManagerStart(false);
                Toasty.success(getContext(), "Scan stop successfully", Toast.LENGTH_SHORT, true)
                    .show();
              }
            }
          }, new OnFailureListener() {
            @Override
            public void onFailure(final int code, final String message) {
              fab.setEnabled(true);
              fab.setClickable(true);
              fab.setVisibility(View.VISIBLE);
              loader.setVisibility(View.GONE);
              imageView.setVisibility(View.VISIBLE);
              Toasty.warning(getContext(), message, Toast.LENGTH_SHORT, true).show();
            }
          });
        }
        Log.i("UFO Error>>>>>", String.valueOf(message));
      }
    });

  }

  private boolean checkPermission() {
    int result = ContextCompat.checkSelfPermission(getContext(), ACCESS_FINE_LOCATION);
    return result == PackageManager.PERMISSION_GRANTED;
  }

  private void requestPermission() {
    requestPermissions(
        new String[]{ACCESS_FINE_LOCATION},
        PERMISSION_REQUEST_CODE);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
    Log.i(">>>>>>>>>>>>>>>>>", "onRequestPermissionsResult:  "+ String.valueOf(requestCode));
    switch (requestCode) {
      case PERMISSION_REQUEST_CODE:
        if (grantResults.length > 0) {

          boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

          Log.i(">>>>>>>>>>>>>>>>>", "onRequestPermissionsResult:  "+ String.valueOf(locationAccepted));

          if (locationAccepted)
            Toasty.success(getContext(), "Permission Granted, Now you can access location data.", Toast.LENGTH_SHORT, true).show();
          else {

            Toasty.info(getContext(), "Permission Denied, You cannot access location data.", Toast.LENGTH_SHORT, true).show();
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
    new AlertDialog.Builder(getContext())
        .setMessage(message)
        .setPositiveButton("OK", okListener)
        .setNegativeButton("Cancel", null)
        .create()
        .show();
  }

  public void showBottomSheetDialog() {
    View view = getLayoutInflater().inflate(R.layout.bluetooth_enable_bottom_sheet, null);
    BottomSheetDialog dialog = new BottomSheetDialog(getContext());
    dialog.setContentView(view);
    dialog.show();
    Button btnDeny = view.findViewById(R.id.btnDeny);
    Button btnAllow = view.findViewById(R.id.btnAllow);

    btnDeny.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Toasty.info(getContext(), "Please enable Blutooth from settings.", Toast.LENGTH_SHORT, true).show();
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

            fab.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
            loader.setVisibility(View.VISIBLE);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
              @Override
              public void run() {
                Toasty.success(getContext(), "Yout device bluetooth is on.", Toast.LENGTH_SHORT, true).show();
                runBeaconManager();
              }
            }, 500);
          }
        }, new OnFailureListener() {
          @Override
          public void onFailure(int code, String message) {
            dialog.dismiss();
            Toasty.warning(getContext(), "Your device bluetooth problem.", Toast.LENGTH_SHORT, true).show();
          }
        });
      }
    });

  }

  @Override
  public void onPrepareOptionsMenu(Menu menu) {
    super.onPrepareOptionsMenu(menu);
    menu.findItem(R.id.action_add).setVisible(true);
    menu.findItem(R.id.action_stop).setVisible(false);
  }

  @Override
  public void onItemClick(int position) {
    UfoDevice data = (new ArrayList<UfoDevice>(ufoDeviceLinkedHashMap.values())).get(position);
    Log.i(">>>>>>>>>>>>>", String.valueOf(data.getRSSI()));
    Intent intent = new Intent(getContext(), RegisterDevice.class);
    UfoDeviceDetails ufoDeviceDetails = new UfoDeviceDetails(data.getDeviceType(), data.getMacID(), data.getMajor(), data.getMinor(), data.getUUID(), data.getTxPower(), data.getRSSI(), data.getDistance(), data.getTemp(), data.getLastUpdate(), data.getScanRecord(), data.getRegisterDevice());
    intent.putExtra("deviceInfo",ufoDeviceDetails);
    startActivity(intent);
  }
}
