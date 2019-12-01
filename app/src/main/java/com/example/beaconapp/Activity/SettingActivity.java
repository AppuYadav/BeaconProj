package com.example.beaconapp.Activity;

import android.widget.CompoundButton;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import com.example.beaconapp.R;
import com.example.beaconapp.Utils.SharedPrefManager;

public class SettingActivity extends AppCompatActivity {

  private Toolbar toolbar;
  private Switch sb;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_setting);

    toolbar = (Toolbar) findViewById(R.id.my_toolbar);
    sb = (Switch) findViewById(R.id.autoscan);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
    getSupportActionBar().setTitle(R.string.title_setting);

    if(SharedPrefManager.getInstance(SettingActivity.this).getAutoON()) {
      sb.setTextOn("STOP");
      sb.setChecked(true);
    }

    sb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
          SharedPrefManager.getInstance(SettingActivity.this).setAutoON(true);
        } else {
          SharedPrefManager.getInstance(SettingActivity.this).setAutoON(false);
        }
      }
    });

  }

  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
  }
}
