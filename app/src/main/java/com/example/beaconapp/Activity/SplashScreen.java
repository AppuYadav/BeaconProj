package com.example.beaconapp.Activity;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.beaconapp.R;
import com.example.beaconapp.Utils.SharedPrefManager;

public class SplashScreen extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash_screen);

//    SharedPrefManager.getInstance(getApplicationContext()).setAutoON(true);

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        SplashScreen.this.startActivity(new Intent(SplashScreen.this, MainActivity.class));
      }
    }, 2000);
  }
}
