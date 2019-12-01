package com.example.beaconapp.Activity;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.beaconapp.Adapter.DeviceListAdapter;
import com.example.beaconapp.Adapter.DeviceListAdapter.OnItemClickListener;
import com.example.beaconapp.Model.UfoDevice;
import com.example.beaconapp.Model.UfoDeviceDetails;
import com.example.beaconapp.R;
import com.example.beaconapp.Service.BackgroundSubscribeIntentService;
import com.example.beaconapp.Service.TrackerService;
import com.example.beaconapp.Utils.SharedPrefManager;
import com.example.beaconapp.Utils.Utils;
import com.github.ybq.android.spinkit.style.Pulse;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.MessagesOptions;
import com.google.android.gms.nearby.messages.NearbyMessagesStatusCodes;
import com.google.android.gms.nearby.messages.NearbyPermissions;
import com.google.android.gms.nearby.messages.Strategy;
import com.google.android.gms.nearby.messages.SubscribeOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.ufobeaconsdk.callback.OnFailureListener;
import com.ufobeaconsdk.callback.OnScanSuccessListener;
import com.ufobeaconsdk.callback.OnSuccessListener;
import com.ufobeaconsdk.main.UFOBeaconManager;
import com.ufobeaconsdk.main.UFODevice;
import es.dmoral.toasty.Toasty;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class ScanActivity extends AppCompatActivity implements ConnectionCallbacks,
    OnConnectionFailedListener, SharedPreferences.OnSharedPreferenceChangeListener {

  private Toolbar toolbar;
  Boolean scanDevice = false;
  BluetoothAdapter mBluetoothAdapter;
  ProgressBar loader;
  private static final int PERMISSION_REQUEST_CODE = 200;
  ImageView imageView;
  private GoogleApiClient mGoogleApiClient;
  private static final String TAG = MainActivity.class.getSimpleName();
  private RelativeLayout mContainer;

  private static final int PERMISSIONS_REQUEST_CODE = 1111;

  private static final String KEY_SUBSCRIBED = "subscribed";
  private boolean mSubscribed = false;
  private ArrayAdapter<String> mNearbyMessagesArrayAdapter;
  private List<String> mNearbyMessagesList = new ArrayList<>();
  UFOBeaconManager ufoBeaconManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_scan);

    toolbar = (Toolbar) findViewById(R.id.my_toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
    getSupportActionBar().setTitle(R.string.title_scan);

    if (savedInstanceState != null) {
      mSubscribed = savedInstanceState.getBoolean(KEY_SUBSCRIBED, false);
    }
    mContainer = (RelativeLayout) findViewById(R.id.main_activity_container);

    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    ufoBeaconManager = new UFOBeaconManager(ScanActivity.this);
    loader = findViewById(R.id.loader);
    imageView = findViewById(R.id.imageView);

    Pulse pulse = new Pulse();
    loader.setIndeterminateDrawable(pulse);

    final ListView nearbyMessagesListView = (ListView) findViewById(
        R.id.nearby_messages_list_view);
    mNearbyMessagesArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
        mNearbyMessagesList);

    if (nearbyMessagesListView != null) {
      nearbyMessagesListView.setAdapter(mNearbyMessagesArrayAdapter);
    }

    if(!checkPermission()) {
      requestPermission();
    }else if (!mBluetoothAdapter.isEnabled()) {
      showBottomSheetDialog();
    }else{
      buildGoogleApiClient();
    }

    final List<String> cachedMessages = Utils.getCachedMessages(this);
    if (cachedMessages != null) {
      mNearbyMessagesList.addAll(cachedMessages);
      if(mNearbyMessagesList.size() == 0){
        loader.setVisibility(View.VISIBLE);
      }else {
        loader.setVisibility(View.GONE);
      }
    }
  }

  @Override
  public boolean onSupportNavigateUp() {
    unSubscribe();
    onBackPressed();
    return true;
  }

  private synchronized void buildGoogleApiClient() {
    if (mGoogleApiClient == null) {
      mGoogleApiClient = new GoogleApiClient.Builder(this)
          .addApi(Nearby.MESSAGES_API, new MessagesOptions.Builder()
              .setPermissions(NearbyPermissions.BLE).build())
          .addConnectionCallbacks(this)
          .enableAutoManage(this, this)
          .build();
    }
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
              buildGoogleApiClient();
            }
          } else {
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
                Toasty.success(ScanActivity.this, "Yout device bluetooth is on.", Toast.LENGTH_SHORT, true).show();
                buildGoogleApiClient();
              }
            }, 1000);
          }
        }, new OnFailureListener() {
          @Override
          public void onFailure(int code, String message) {
            dialog.dismiss();
            Toasty.warning(ScanActivity.this, "Your device bluetooth problem.", Toast.LENGTH_SHORT, true).show();
          }
        });
      }
    });
  }

  @Override
  public void onConnected(@Nullable Bundle bundle) {
    Log.i(TAG, "GoogleApiClient connected");
    subscribe();
  }

  @Override
  public void onConnectionSuspended(int i) {
    Log.w(TAG, "Connection suspended. Error code: " + i);
  }

  private void subscribe() {
    // In this sample, we subscribe when the activity is launched, but not on device orientation
    // change.
    if (mSubscribed) {
      Log.i(TAG, "Already subscribed.");
      return;
    }

    SubscribeOptions options = new SubscribeOptions.Builder()
        .setStrategy(Strategy.BLE_ONLY)
        .build();

    Nearby.Messages.subscribe(mGoogleApiClient, getPendingIntent(), options)
        .setResultCallback(new ResultCallback<Status>() {
          @Override
          public void onResult(@NonNull Status status) {
            if (status.isSuccess()) {
              Log.i(TAG, "Subscribed successfully.");
              startService(getBackgroundSubscribeServiceIntent());
            } else {
              Log.e(TAG, "Operation failed. Error: " +
                  NearbyMessagesStatusCodes.getStatusCodeString(
                      status.getStatusCode()));
            }
          }
        });
  }

  private PendingIntent getPendingIntent() {
    return PendingIntent.getService(this, 0,
        getBackgroundSubscribeServiceIntent(), PendingIntent.FLAG_UPDATE_CURRENT);
  }

  private Intent getBackgroundSubscribeServiceIntent() {
    return new Intent(this, BackgroundSubscribeIntentService.class);
  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    if (mContainer != null) {
      Snackbar.make(mContainer, "Exception while connecting to Google Play services: " +
              connectionResult.getErrorMessage(),
          Snackbar.LENGTH_INDEFINITE).show();
    }
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    if (TextUtils.equals(key, Utils.KEY_CACHED_MESSAGES)) {
      mNearbyMessagesList.clear();
      mNearbyMessagesList.addAll(Utils.getCachedMessages(this));
      if(mNearbyMessagesList.size() == 0){
        loader.setVisibility(View.VISIBLE);
      }else {
        loader.setVisibility(View.GONE);
      }
      mNearbyMessagesArrayAdapter.notifyDataSetChanged();
    }
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putBoolean(KEY_SUBSCRIBED, mSubscribed);
  }

  @Override
  protected void onResume() {
    super.onResume();

    getSharedPreferences(getApplicationContext().getPackageName(), Context.MODE_PRIVATE)
        .registerOnSharedPreferenceChangeListener(this);

    if (checkPermission()) {
      buildGoogleApiClient();
    }
  }

  @Override
  protected void onPause() {
    getSharedPreferences(getApplicationContext().getPackageName(), Context.MODE_PRIVATE)
        .unregisterOnSharedPreferenceChangeListener(this);
    super.onPause();
  }

  private void unSubscribe() {
//    Intent intent = new Intent(ScanActivity.this, BackgroundSubscribeIntentService.class);
//    PendingIntent pendingIntent = PendingIntent.getService(ScanActivity.this, 0, intent, 0);
    Nearby.Messages.unsubscribe(mGoogleApiClient, getPendingIntent());
  }

}
