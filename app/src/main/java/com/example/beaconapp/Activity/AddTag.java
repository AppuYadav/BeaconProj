package com.example.beaconapp.Activity;

import static com.example.beaconapp.Utils.AppUtils.validateInputText;

import android.os.Bundle;
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
import com.example.beaconapp.Helper.Tables.MappingTagTBL;
import com.example.beaconapp.Model.Database.Tag;
import com.example.beaconapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import es.dmoral.toasty.Toasty;

public class AddTag extends AppCompatActivity {

  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.tag_id)
  TextInputEditText tagId;
  @BindView(R.id.tag_id_input)
  TextInputLayout tagIdInput;
  @BindView(R.id.tag_verbose_name)
  TextInputEditText tagVerboseName;
  @BindView(R.id.tag_verbose_name_input)
  TextInputLayout tagVerboseNameInput;
  @BindView(R.id.message)
  TextInputEditText message;
  @BindView(R.id.message_input)
  TextInputLayout messageInput;
  @BindView(R.id.adddevice)
  Button adddevice;
  private Tag tag;
  private MappingTagTBL mappingTagTBL;
  Boolean TagDelete = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_tag);

    ButterKnife.bind(this);

    mappingTagTBL = new MappingTagTBL(this);
    mappingTagTBL.open();

    tag = getIntent().getParcelableExtra("tagInfo");
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);

    if(tag != null){
      TagDelete = true;
      tagId.setEnabled(false);
      invalidateOptionsMenu();
      adddevice.setText("UPDATE");
      tagId.setText(tag.getTagID());
      tagVerboseName.setText(tag.getVerboseTitle());
      message.setText(tag.getMessage());
      getSupportActionBar().setTitle(tag.getTagID());
    }else{
      getSupportActionBar().setTitle("Add Tag");
    }

    adddevice.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (validate()){
          if(adddevice.getText() != "UPDATE"){
            tag = new Tag(tagId.getText().toString().trim(), tagVerboseName.getText().toString().trim(), message.getText().toString().trim());
            long insertStatus = mappingTagTBL.insert(tag);
            if(insertStatus != -1){
              Toasty.success(getApplicationContext(), "Tag Registered successfully", Toast.LENGTH_SHORT, true)
                  .show();

              TagDelete = true;
              tagId.setEnabled(false);
              invalidateOptionsMenu();
              adddevice.setText("UPDATE");

            }else{
              Toasty.error(getApplicationContext(), "Tag Already Exist", Toast.LENGTH_SHORT, true)
                  .show();
            }
          }else {
            tag = new Tag(tagId.getText().toString().trim(), tagVerboseName.getText().toString().trim(), message.getText().toString().trim());
            long updateStatus = mappingTagTBL.update(tag);
            if(updateStatus != -1){
              Toasty.success(getApplicationContext(), "Tag update successfully", Toast.LENGTH_SHORT, true)
                  .show();

              TagDelete = true;
              invalidateOptionsMenu();
              adddevice.setText("UPDATE");


            }else{
              Toasty.error(getApplicationContext(), "Tag update problems", Toast.LENGTH_SHORT, true)
                  .show();
            }
          }
        }
      }
    });
  }

  private boolean validate() {
    return validateInputText("Tag ID", tagId, tagIdInput, AddTag.this) &&
        validateInputText("Tag Verbose Name", tagVerboseName, tagVerboseNameInput, AddTag.this) && validateInputText("Tag Message", message, messageInput, AddTag.this);
  }

  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    super.onPrepareOptionsMenu(menu);
    if(TagDelete){
      menu.findItem(R.id.action_delete).setVisible(true);
    }else{
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

    if(tag != null){
      deletetem.setVisible(true);
    }else{
      deletetem.setVisible(false);
    }
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    if (id == R.id.action_delete) {
      mappingTagTBL.delete(tag.getTagID());
      TagDelete = true;

      Toasty.success(getApplicationContext(), "Beacon delete successfully", Toast.LENGTH_SHORT, true)
          .show();

      onBackPressed();
    }

    return super.onOptionsItemSelected(item);
  }
}
