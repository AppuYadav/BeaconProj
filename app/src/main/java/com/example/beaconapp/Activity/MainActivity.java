package com.example.beaconapp.Activity;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.example.beaconapp.Utils.AppUtils.validateInputText;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.beaconapp.Adapter.DeviceListAdapter;
import com.example.beaconapp.Adapter.DeviceListAdapter.OnItemClickListener;
import com.example.beaconapp.Fragment.AddFragment;
import com.example.beaconapp.Fragment.HomeFragment;
import com.example.beaconapp.Helper.Tables.BeaconDevices;
import com.example.beaconapp.Helper.Tables.DeviceLocationINFO;
import com.example.beaconapp.Helper.Tables.MappingBeaconTBL;
import com.example.beaconapp.Helper.Tables.MappingTagTBL;
import com.example.beaconapp.LocationSearch.Graph;
import com.example.beaconapp.LocationSearch.pathList;
import com.example.beaconapp.Model.Database.UfoDeviceDB;
import com.example.beaconapp.Model.DestInfo;
import com.example.beaconapp.Model.GraphData;
import com.example.beaconapp.Model.UfoDevice;
import com.example.beaconapp.Model.UfoDeviceDetails;
import com.example.beaconapp.R;
import com.example.beaconapp.Service.BackgroundSubscribeIntentService;
import com.example.beaconapp.Service.NearbyBackgroundService;
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
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageFilter;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.MessagesOptions;
import com.google.android.gms.nearby.messages.NearbyMessagesStatusCodes;
import com.google.android.gms.nearby.messages.NearbyPermissions;
import com.google.android.gms.nearby.messages.Strategy;
import com.google.android.gms.nearby.messages.SubscribeOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.ufobeaconsdk.callback.OnFailureListener;
import com.ufobeaconsdk.callback.OnScanSuccessListener;
import com.ufobeaconsdk.callback.OnSuccessListener;
import com.ufobeaconsdk.main.UFOBeaconManager;
import com.ufobeaconsdk.main.UFODevice;
import es.dmoral.toasty.Toasty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import net.skoumal.fragmentback.BackFragmentHelper;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

  private Drawable newdrawable;
  private Toolbar toolbar;
  boolean doubleBackToExitPressedOnce = false;
  UFOBeaconManager ufoBeaconManager;
  BluetoothAdapter mBluetoothAdapter;
  private static final int PERMISSION_REQUEST_CODE = 200;
  androidx.appcompat.app.AlertDialog.Builder dialogBuilder;
  androidx.appcompat.app.AlertDialog alertDialog;
  Boolean scanDevice = false;
  ProgressBar loader;
  ImageView imageView;
  FloatingTextButton fab;
  private static final String TAG = MainActivity.class.getSimpleName();
  private BeaconDevices beaconDevices;
  private MappingTagTBL mappingTagTBL;
  public LinearLayout searchBar, mic, lldestination;
  private MaterialSearchView searchView;
  private TextView txtSpeechInput, txtdestination;
  private ImageButton btnSpeak;
  private final int REQ_CODE_SPEECH_INPUT = 100;
  private List<UfoDeviceDB> ufoDeviceList = new ArrayList<UfoDeviceDB>();
  private MappingBeaconTBL mappingBeaconTBL;
  private List<GraphData> graphDataList = new ArrayList<GraphData>();
  private SensorManager mSensorManager;
  private Sensor sSensor;
  private long steps = 0;
  private TextInputLayout tag_id_input;
  private EditText tag_id;
  private TextToSpeech t1;
  private String destination, tag_message, currentlocation;
  private Graph graph;
  int source_id;
  String dirTxt = "";
  public ArrayList<Integer> shortPath = new ArrayList<Integer>();
  public DestInfo destInfo;
  public DeviceLocationINFO deviceLocationINFO;
  public String oneTimeSpeak;
  public Integer startTravel;
  public Boolean checkDist = false;
  public Boolean oneTime = true;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    loader = findViewById(R.id.loader);
    imageView = findViewById(R.id.imageView);
    fab = findViewById(R.id.fab);
    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    ufoBeaconManager = new UFOBeaconManager(MainActivity.this);
    toolbar = (Toolbar) findViewById(R.id.my_toolbar);
    searchBar = findViewById(R.id.lllocation);
    mic = findViewById(R.id.mic);
    searchView = (MaterialSearchView)findViewById(R.id.search_view);
    searchView.setVoiceSearch(true);
    searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
    txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
    btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
    tag_id_input = findViewById(R.id.tag_id_input);
    tag_id = findViewById(R.id.tag_id);
    txtdestination = findViewById(R.id.txtdestination);
    lldestination = findViewById(R.id.lldestination);
    mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

    Pulse pulse = new Pulse();
    loader.setIndeterminateDrawable(pulse);

    beaconDevices = new BeaconDevices(this);
    beaconDevices.open();

    mappingBeaconTBL = new MappingBeaconTBL(this);
    mappingBeaconTBL.open();

    mappingTagTBL = new MappingTagTBL(this);
    mappingTagTBL.open();

    deviceLocationINFO = new DeviceLocationINFO(this);
    deviceLocationINFO.open();


    tag_id_input.setVisibility(View.GONE);
    tag_id.setVisibility(View.GONE);

//    Drawable drawable = getResources().getDrawable(R.drawable.logo);
////    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
//    newdrawable = new BitmapDrawable(getResources(),
//        Bitmap.createScaledBitmap(bitmap, 75, 75, true));
//    newdrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
//    getSupportActionBar().setLogo(newdrawable);
//    toolbar.setNavigationIcon(newdrawable);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle("Beacon");

//    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//          @Override
//          public void onClick(View v) {
//            showAlertDialog(R.layout.dialog_nick_name_layout);
//          }
//        }
//    );

    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        stopScan();
        Intent intent = new Intent(MainActivity.this, ScanBeaconActivity.class);
        startActivity(intent);
      }
    });

    t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
      @Override
      public void onInit(int status) {
        if(status != TextToSpeech.ERROR) {
          t1.setLanguage(Locale.UK);
          t1.setSpeechRate(0.75f);
        }
      }
    });

    if(SharedPrefManager.getInstance(MainActivity.this).getAutoON()){
      scanDevice = true;
      imageView.setVisibility(View.GONE);
      loader.setVisibility(View.VISIBLE);
      mic.setVisibility(View.VISIBLE);
      invalidateOptionsMenu();
      if(!checkPermission()) {
        requestPermission();
      }else if (!mBluetoothAdapter.isEnabled()) {
        showBottomSheetDialog();
      }else{
        startScan();
      }
    }

    searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        //Do some magic
        return false;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        //Do some magic
        return false;
      }
    });

    graph = new Graph(beaconDevices.getBeaconDeviceCount() + 1);
    graphDataList = mappingBeaconTBL.getMappingData();
    for (GraphData data : graphDataList) {
      graph.addEdge(data.getSource(),data.getDest());
    }

    searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
      @Override
      public void onSearchViewShown() {
        //Do some magic
      }

      @Override
      public void onSearchViewClosed() {
        //Do some magic
      }
    });

    btnSpeak.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        promptSpeechInput();
      }
    });

//    tag_id.setFocusable(false);
//    tag_id.setFocusableInTouchMode(true);

    tag_id.addTextChangedListener(new TextWatcher(){
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Log.i(TAG, "onBextChanged: " + s);
//        t1.speak(s.toString(), TextToSpeech.QUEUE_FLUSH, null);
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.i(TAG, "onTextChanged: " + s);
        if(s.length() == 8){
          tag_message = mappingTagTBL.getIDToNameTag(s.toString());
          t1.speak(tag_message, TextToSpeech.QUEUE_FLUSH, null);
          tag_id.setText("");
        }
      }

      @Override
      public void afterTextChanged(Editable s) {
        Log.i(TAG, "onAextChanged: " + s);
//        t1.speak(s.toString(), TextToSpeech.QUEUE_FLUSH, null);
      }
    });

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main_menu, menu);
    MenuItem item = menu.findItem(R.id.action_search);
    searchView.setMenuItem(item);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_scan) {
      scanDevice = true;
      loader.setVisibility(View.VISIBLE);
      imageView.setVisibility(View.GONE);
//      mic.setVisibility(View.VISIBLE);
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
      loader.setVisibility(View.GONE);
      imageView.setVisibility(View.VISIBLE);
      searchBar.setVisibility(View.GONE);
      mic.setVisibility(View.GONE);
      lldestination.setVisibility(View.GONE
      );
      invalidateOptionsMenu();
      stopScan();
    }else if(id == R.id.action_all){
      Intent intent = new Intent(this, AllActivity.class);
      startActivity(intent);
      return true;
    }else if(id == R.id.action_all_tag){
      Intent intent = new Intent(this, AllTag.class);
      startActivity(intent);
      return true;
    }else if(id == R.id.action_setting){
      Intent intent = new Intent(this, SettingActivity.class);
      startActivity(intent);
      return true;
    }

    return super.onOptionsItemSelected(item);
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
          tag_id.setText("");
          loader.setVisibility(View.GONE);
          imageView.setVisibility(View.VISIBLE);
          tag_id_input.setVisibility(View.GONE);
          tag_id.setVisibility(View.GONE);
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

  @Override
  protected void onResume() {
    super.onResume();
    mSensorManager.registerListener( this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
    sSensor= mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
    if(sSensor != null){
      mSensorManager.registerListener(this, sSensor, SensorManager.SENSOR_DELAY_UI);
    }else{
      Toasty.info(this, "Sensor not found!", Toasty.LENGTH_SHORT).show();
    }
    if(SharedPrefManager.getInstance(MainActivity.this).getAutoON()){
      scanDevice = true;
      imageView.setVisibility(View.GONE);
      loader.setVisibility(View.VISIBLE);;
      mic.setVisibility(View.VISIBLE);
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

  public void startScan(){
    oneTimeSpeak = null;
    tag_id.setText("");
    ufoDeviceList = beaconDevices.getBeaconDeviceAll();

    ufoBeaconManager.startScan(new OnScanSuccessListener() {
      @Override
      public void onSuccess(final UFODevice ufodevice) {
        startService(new Intent(getApplicationContext(), TrackerService.class));
        Log.i(TAG, ">>>>>>>>>>>>>>>>" + ufodevice.getMinor());
        for(int i = 0; i< ufoDeviceList.size(); i++){
          if(ufodevice.getBtdevice().getAddress().equalsIgnoreCase(ufoDeviceList.get(i).getMacID())){
            mic.setVisibility(View.VISIBLE);
            if(Integer.valueOf(ufoDeviceList.get(i).getRSSI()) <= ufodevice.getRssi()){
              source_id = (int)beaconDevices.getNameTOID(currentlocation);
              currentlocation = ufoDeviceList.get(i).getDeviceLocationInfoDB().getLocationName();
              txtSpeechInput.setText(currentlocation);
              if(oneTimeSpeak == null){
                  t1.speak(ufoDeviceList.get(i).getDeviceLocationInfoDB().getBroadCastMessage(), TextToSpeech.QUEUE_FLUSH, null);
                  oneTimeSpeak = currentlocation;
              }else if(oneTimeSpeak != currentlocation){
//                if(oneTime){
//                  t1.speak(ufoDeviceList.get(i).getDeviceLocationInfoDB().getBroadCastMessage(), TextToSpeech.QUEUE_FLUSH, null);
//                }
                if(checkDist){
                  if(shortPath.size() >= startTravel) {
                    destInfo = deviceLocationINFO
                        .getDeviceInfo(shortPath.get(startTravel), shortPath.get(startTravel + 1));
                    if(ufodevice.getBtdevice().getAddress().equalsIgnoreCase(destInfo.getMac_id())){
                      startTravel = startTravel + 1;
                      t1.speak(destInfo.getMessage_mapping(),
                          TextToSpeech.QUEUE_FLUSH, null);
                    }
                  }else {
                    t1.speak("You Reached Location", TextToSpeech.QUEUE_FLUSH, null);
                    checkDist = false;
                  }
                }else{
                  t1.speak(ufoDeviceList.get(i).getDeviceLocationInfoDB().getBroadCastMessage(), TextToSpeech.QUEUE_FLUSH, null);
                  oneTimeSpeak = currentlocation;
                }
              }
            }
            searchBar.setVisibility(View.VISIBLE);
            tag_id_input.setVisibility(View.VISIBLE);
            tag_id.setVisibility(View.VISIBLE);
          }
        }
        loader.setVisibility(View.VISIBLE);
      }
    }, new OnFailureListener() {
      @Override
      public void onFailure(final int code, final String message) {
//        loader.setVisibility(View.GONE);
//        imageView.setVisibility(View.VISIBLE);
//        Toasty.warning(MainActivity.this, message, Toast.LENGTH_SHORT, true).show();
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
        mic.setVisibility(View.GONE);
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
                Toasty.success(MainActivity.this, "Yout device bluetooth is on.", Toast.LENGTH_SHORT, true).show();
                startScan();
              }
            }, 1000);
          }
        }, new OnFailureListener() {
          @Override
          public void onFailure(int code, String message) {
            dialog.dismiss();
            Toasty.warning(MainActivity.this, "Your device bluetooth problem.", Toast.LENGTH_SHORT, true).show();
          }
        });
      }
    });
  }

  @Override
  public void onBackPressed() {
    if (searchView.isSearchOpen()) {
      searchView.closeSearch();
    } else {
    if (doubleBackToExitPressedOnce) {
//        moveTaskToBack(true);
//        android.os.Process.killProcess(android.os.Process.myPid());
//        finish();
      this.finishAffinity();
    }
    this.doubleBackToExitPressedOnce = true;
    new Handler().postDelayed(new Runnable() {

      @Override
      public void run() {
        doubleBackToExitPressedOnce=false;
      }
    }, 1000);
    }
  }

  /**
   * Showing google speech input dialog
   * */
  private void promptSpeechInput() {
    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
    intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
        getString(R.string.speech_prompt));
    try {
      startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
    } catch (ActivityNotFoundException a) {
      Toast.makeText(getApplicationContext(),
          getString(R.string.speech_not_supported),
          Toast.LENGTH_SHORT).show();
    }
  }

  public String directionConvert(String s, String d){
    HashMap<String, String> east = new HashMap<String, String>();
    HashMap<String, String> west = new HashMap<String, String>();
    HashMap<String, String> north = new HashMap<String, String>();
    HashMap<String, String> south = new HashMap<String, String>();
    east.put("east", "Turn Oppsite");
    east.put("west", "Go straight");
    east.put("north", "Turn Left");
    east.put("south", "Turn Right");

    west.put("east", "Go straight");
    west.put("west", "Turn Oppsite");
    west.put("north", "Turn Right");
    west.put("south", "Turn Left");

    north.put("east", "Turn Right");
    north.put("west", "Turn Left");
    north.put("north", "Turn Oppsite");
    north.put("south", "Go straight");

    south.put("east", "Turn Left");
    south.put("west", "Turn Right");
    south.put("north", "Go straight");
    south.put("south", "Turn Oppsite");

    if(s.equalsIgnoreCase("east") || s.equalsIgnoreCase("NE")){
      return east.get(d.toLowerCase());
    }else if(s.equalsIgnoreCase("west") || s.equalsIgnoreCase("SW")){
      return west.get(d.toLowerCase());
    }else if(s.equalsIgnoreCase("north") || s.equalsIgnoreCase("NW")){
      return north.get(d.toLowerCase());
    }else if(s.equalsIgnoreCase("south") || s.equalsIgnoreCase("SE")){
      return south.get(d.toLowerCase());
    }

    return null;
  }

  /**
   * Receiving speech input
   * */
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    switch (requestCode) {
      case REQ_CODE_SPEECH_INPUT: {
        if (resultCode == RESULT_OK && null != data) {

          ArrayList<String> result = data
              .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

          int destination_id = (int)beaconDevices.getNameTOID(result.get(0));
          Log.i(TAG, "onActivityResult: " + result.get(0));
          if(destination_id != -1){
            destination = result.get(0);
            lldestination.setVisibility(View.VISIBLE);
            txtdestination.setText(destination);
            if(source_id != destination_id){

              ArrayList<pathList> all = graph.printAllPaths(source_id, destination_id);
              Log.i(TAG, "onCreate: " + all.size());

              if(all.size() != 1){
                pathList temp;
                for (int i = 0; i < all.size(); i++)
                {
                  for (int j = i + 1; j < all.size(); j++)
                  {
                    if (all.get(i).getNode().size() > all.get(j).getNode().size())
                    {
                      temp = all.get(i);
                      all.set(i, all.get(j));
                      all.set(j, temp);
                    }
                  }
                }
                shortPath = all.get(0).getNode();
              } else{
                shortPath = all.get(0).getNode();
              }

              if(shortPath != null){
                t1.speak("Lets Start".toString(), TextToSpeech.QUEUE_FLUSH, null);
                try
                {
                  Thread.sleep(1000);
                }
                catch(InterruptedException ex)
                {
                  Thread.currentThread().interrupt();
                }
                destInfo = deviceLocationINFO.getDeviceInfo(shortPath.get(0), shortPath.get(1));
                startTravel = 1;
                t1.speak(destInfo.getMessage_mapping(), TextToSpeech.QUEUE_FLUSH, null);
                checkDist = true;
              }else{
                t1.speak("The source and destination not available".toString(), TextToSpeech.QUEUE_FLUSH, null);
              }
            }else{
              t1.speak("The source and destination is same".toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
          }else{
            lldestination.setVisibility(View.GONE);
            t1.speak("The destination is wrong".toString(), TextToSpeech.QUEUE_FLUSH, null);
          }

          Log.i(TAG, "onActivityResult: " + destination);
//          searchBar.setVisibility(View.VISIBLE);
        }
        break;
      }

    }
  }

  @Override
  public void onSensorChanged(SensorEvent event) {
    Sensor sensor = event.sensor;
    float[] values = event.values;
    int value = -1;

    if (values.length > 0) {
      value = (int) values[0];
    }

//    Log.i(TAG, "onSensorChanged: "+ sensor.getType());

    float degree = Math.round(event.values[0]);
    updateTextDirection(degree);

    if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
      steps++;
    }

//    txtSpeechInput.setText(String.valueOf(steps));
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy) {

  }

  @Override
  protected void onPause() {
    super.onPause();
    mSensorManager.unregisterListener(this);
  }

  private void updateTextDirection(double bearing) {
    int range = (int) (bearing / (360f / 16f));

    if (range == 15 || range == 0)
      dirTxt = "north";
    if (range == 1 || range == 2)
      dirTxt = "NE";
    if (range == 3 || range == 4)
      dirTxt = "east";
    if (range == 5 || range == 6)
      dirTxt = "SE";
    if (range == 7 || range == 8)
      dirTxt = "south";
    if (range == 9 || range == 10)
      dirTxt = "SW";
    if (range == 11 || range == 12)
      dirTxt = "west";
    if (range == 13 || range == 14)
      dirTxt = "NW";

    Log.i(TAG, "updateTextDirection: " + dirTxt);
  }
}
