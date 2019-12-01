package com.example.beaconapp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.beaconapp.Drawable.RoundedBackgroundSpan;
import com.example.beaconapp.Model.UfoDevice;
import com.example.beaconapp.R;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.ViewHolder> {

  private LinkedHashMap<String, UfoDevice> ufoDeviceLinkedHashMap;
  private LayoutInflater layoutInflater;
  private Context mContext;
  private OnItemClickListener mListener;

  public DeviceListAdapter(LinkedHashMap<String, UfoDevice> ufoDeviceLinkedHashMap, Context context) {
    this.ufoDeviceLinkedHashMap = ufoDeviceLinkedHashMap;
    this.mContext = context;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = layoutInflater.from(parent.getContext()).inflate(R.layout.row_device_layout, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    UfoDevice data = (new ArrayList<UfoDevice>(ufoDeviceLinkedHashMap.values())).get(position);
    holder.tvDeviceType.setText(data.getDeviceType());
    holder.tvMacID.setText(data.getMacID());
    holder.tvUUID.setText(data.getUUID());
    holder.tvMajor.setText(data.getMajor());
    holder.tvMinor.setText(data.getMinor());
    holder.tvTxPower.setText(data.getTxPower());
    holder.tvRSSI.setText(data.getRSSI());
    holder.tvDis.setText(data.getDistance());
    holder.tvTemp.setText(data.getTemp());
    holder.tvUpdate.setText(data.getLastUpdate());
//    Spannable spanna = new SpannableString(data.getRegisterDevice() ? "Registered" : "Not Registered");
//    spanna.setSpan(new BackgroundColorSpan(Color.rgb(85,	26,	139)),0, (data.getRegisterDevice() ? "Registered" : "Not Registered").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
    SpannableString tag2 = new SpannableString(data.getRegisterDevice() ? "Registered" : "Not Registered");
    stringBuilder.append(tag2);
    stringBuilder.setSpan(new RoundedBackgroundSpan(ContextCompat.getColor(mContext, R.color.purple), ContextCompat.getColor(mContext, R.color.white)), stringBuilder.length() - tag2.length(), stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//    tagsTextView.setText(stringBuilder, TextView.BufferType.SPANNABLE);
    holder.tvStatus.setText(stringBuilder, TextView.BufferType.SPANNABLE);
    holder.tvScanRecord.setText(data.getScanRecord());
  }

  @Override
  public int getItemCount() {
    return ufoDeviceLinkedHashMap.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    TextView tvDeviceType, tvMacID, tvUUID, tvMajor, tvMinor, tvTxPower, tvRSSI, tvDis, tvTemp, tvUpdate, tvStatus, tvScanRecord;
    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      tvDeviceType = itemView.findViewById(R.id.tvDeviceType);
      tvMacID = itemView.findViewById(R.id.tvMacID);
      tvUUID = itemView.findViewById(R.id.tvUUID);
      tvMajor = itemView.findViewById(R.id.tvMajor);
      tvMinor = itemView.findViewById(R.id.tvMinor);
      tvTxPower = itemView.findViewById(R.id.tvTxPower);
      tvRSSI = itemView.findViewById(R.id.tvRSSI);
      tvDis = itemView.findViewById(R.id.tvDis);
      tvTemp = itemView.findViewById(R.id.tvTemp);
      tvUpdate = itemView.findViewById(R.id.tvUpdate);
      tvStatus = itemView.findViewById(R.id.tvStatus);
      tvScanRecord = itemView.findViewById(R.id.tvScanRecord);

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (mListener != null) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
              mListener.onItemClick(position);
            }
          }
        }
      });
    }
  }

  public interface OnItemClickListener {
    void onItemClick(int position);
  }

  public void setOnItemClickListener(OnItemClickListener listener) {
    mListener = listener;
  }

}
