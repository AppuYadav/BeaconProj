package com.example.beaconapp.BottomSheet;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.example.beaconapp.Fragment.AddFragment;
import com.example.beaconapp.R;
import com.example.beaconapp.Utils.SharedPrefManager;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ufobeaconsdk.callback.OnFailureListener;
import com.ufobeaconsdk.callback.OnScanSuccessListener;
import com.ufobeaconsdk.main.UFOBeaconManager;
import com.ufobeaconsdk.main.UFODevice;
import es.dmoral.toasty.Toasty;

public class BluetoothEnableBottomSheetDialog extends BottomSheetDialogFragment {

  UFOBeaconManager ufoBeaconManager;

  public static BluetoothEnableBottomSheetDialog getInstance() {
    return new BluetoothEnableBottomSheetDialog();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.bluetooth_enable_bottom_sheet, container, false);
  }
}
