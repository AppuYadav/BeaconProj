package com.example.beaconapp.Utils;

import android.app.AlertDialog;
import android.content.Context;
import com.example.beaconapp.Helper.Tables.BeaconDevices;
import com.example.beaconapp.Helper.Tables.DeviceLocationINFO;
import com.example.beaconapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import dmax.dialog.SpotsDialog;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AppUtils {

  public static Boolean validateInputText(String text, TextInputEditText editText, TextInputLayout layout, Context context){
    if (editText.getText().toString().trim().length() == 0){
      layout.setError("Please enter your " + text);
      return false;
    }else{
      layout.setErrorEnabled(false);
      return true;
    }
  }

  public AlertDialog setProgress(Context context, AlertDialog alertDialog) {
    alertDialog = new
        SpotsDialog.Builder()
        .setContext(context)
        .setTheme(R.style.Custom)
        .build();
    alertDialog.show();
    return alertDialog;
  }

  /**
   * get datetime
   * */
  public static String getDateTime() {
    SimpleDateFormat dateFormat = new SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    Date date = new Date();
    return dateFormat.format(date);
  }

}
