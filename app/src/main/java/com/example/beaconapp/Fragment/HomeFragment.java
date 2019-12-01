package com.example.beaconapp.Fragment;

import static com.example.beaconapp.Utils.AppUtils.validateInputText;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.beaconapp.Activity.MainActivity;
import com.example.beaconapp.R;
import com.example.beaconapp.Utils.SharedPrefManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Date;

public class HomeFragment extends Fragment {

  @BindView(R.id.tvDate)
  TextView tvDate;
  @BindView(R.id.tvName)
  TextView tvName;
  @BindView(R.id.tvMessage)
  TextView tvMessage;

  AlertDialog.Builder dialogBuilder;
  AlertDialog alertDialog;
  private AlertDialog alertDialog1;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view =  inflater.inflate(R.layout.fragment_home, container, false);
    setHasOptionsMenu(true);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    Date d = new Date();
    tvDate.setText(DateFormat.format("MMM dd, yyyy", d.getTime()));
    String nickname = SharedPrefManager.getInstance(getContext()).getNickName() == "First" || SharedPrefManager.getInstance(getContext()).getNickName() == null ? "John" : SharedPrefManager.getInstance(getContext()).getNickName();
    tvName.setText(nickname);
  }


  @Override
  public void onPrepareOptionsMenu(Menu menu) {
    menu.findItem(R.id.action_add).setVisible(true);
    menu.findItem(R.id.action_stop).setVisible(false);
    super.onPrepareOptionsMenu(menu);
  }
}
