package com.example.beaconapp.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.beaconapp.Helper.DatabaseHelper;
import com.example.beaconapp.R;
import com.example.beaconapp.Utils.Utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class ExportActivity extends AppCompatActivity {

  @BindView(R.id.importdevice)
  Button importdevice;
  @BindView(R.id.export)
  Button export;
  private Toolbar toolbar;
  public static final int PICKFILE_RESULT_CODE = 1;
  private Uri fileUri;
  private String filePath;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_export);
    ButterKnife.bind(this);

    toolbar = findViewById(R.id.my_toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
    getSupportActionBar().setTitle(R.string.title_export);

    importdevice.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
          startActivityForResult(
              Intent.createChooser(intent, "Select a File to Upload"),
              PICKFILE_RESULT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
          // Potentially direct the user to the Market with a Dialog
          Toast.makeText(getApplicationContext(), "Please install a File Manager.",
              Toast.LENGTH_SHORT).show();
        }
      }
    });

    export.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        exportDB();
      }
    });

  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
      case PICKFILE_RESULT_CODE:
        if (resultCode == -1) {
          fileUri = data.getData();
          filePath = fileUri.getPath();
//          importDB(filePath);
          Log.i("<>>>>>>>>>>>>>>>", filePath);
        }

        break;
    }
  }
  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
  }

//  //importing database
//  private void importDB(String backupDBPath) {
//
//    try {
//      File sd = Environment.getExternalStorageDirectory();
//      File data = Environment.getDataDirectory();
//
//      if (sd.canWrite()) {
//        String currentDBPath = "//data//" + "com.example.beaconapp"
//            + "//databases//" + "Beacon.db";
//        File backupDB = new File(data, currentDBPath);
//        File currentDB = new File(sd, backupDBPath);
//
//        FileChannel src = new FileInputStream(currentDB).getChannel();
//        FileChannel dst = new FileOutputStream(backupDB).getChannel();
//        dst.transferFrom(src, 0, src.size());
//        src.close();
//        dst.close();
//        Toast.makeText(getBaseContext(), backupDB.toString(),
//            Toast.LENGTH_LONG).show();
//
//      }
//    } catch (Exception e) {
//
//      Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG)
//          .show();
//
//    }
//  }

  //exporting database
  private void exportDB() {
    try {
      File sd = Environment.getExternalStorageDirectory();
      File data = Environment.getDataDirectory();

      if (sd.canWrite()) {
        String currentDBPath = "//data//" + "com.example.beaconapp"
            + "//databases//" + "Beacon.db";
        String backupDBPath = "/document/";
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);

        FileChannel src = new FileInputStream(currentDB).getChannel();
        FileChannel dst = new FileOutputStream(backupDB).getChannel();
        dst.transferFrom(src, 0, src.size());
        src.close();
        dst.close();
        Toast.makeText(getBaseContext(), backupDB.toString(),
            Toast.LENGTH_LONG).show();

      }
    } catch (Exception e) {

      Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG)
          .show();

    }
  }

}
