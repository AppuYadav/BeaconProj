package com.example.beaconapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.beaconapp.Activity.AllMapping;
import com.example.beaconapp.Activity.DeviceMapping;
import com.example.beaconapp.Activity.ExportActivity;
import com.example.beaconapp.Drawable.RoundedBackgroundSpan;
import com.example.beaconapp.Model.Database.UfoDeviceDB;
import com.example.beaconapp.Model.UfoDevice;
import com.example.beaconapp.R;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class RegisterDeviceListAdapter extends RecyclerView.Adapter<RegisterDeviceListAdapter.ViewHolder> implements
    Filterable {

  // UfoDeviceDB
  private LayoutInflater layoutInflater;
  private OnItemClickListener mListener;
  private List<UfoDeviceDB> ufoDeviceList = new ArrayList<UfoDeviceDB>();
  private List<UfoDeviceDB> ufoDeviceListFiltered = new ArrayList<UfoDeviceDB>();
  private Context mContext;

  public RegisterDeviceListAdapter(List<UfoDeviceDB> ufoDeviceDB,
      Context mContext
      ) {
    this.ufoDeviceList = ufoDeviceDB;
    this.mContext = mContext;
    this.ufoDeviceListFiltered = ufoDeviceDB;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = layoutInflater.from(parent.getContext()).inflate(R.layout.register_device_row, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    UfoDeviceDB data = ufoDeviceList.get(position);
    holder.tvDeviceType.setText(data.getDeviceType());
    holder.tvMacID.setText(data.getMacID());
    holder.tvLocation.setText(data.getDeviceLocationInfoDB().getLocationName());
    holder.tvMajor.setText(data.getMajor());
    holder.tvMinor.setText(data.getMinor());
    holder.tvCreatedDate.setText(data.getCreated_date());
    SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
    SpannableString tag2 = new SpannableString(data.getRegisterDevice() ? "Registered" : "Not Registered");
    stringBuilder.append(tag2);
    stringBuilder.setSpan(new RoundedBackgroundSpan(ContextCompat.getColor(mContext, R.color.purple), ContextCompat.getColor(mContext, R.color.white)), stringBuilder.length() - tag2.length(), stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//    tagsTextView.setText(stringBuilder, TextView.BufferType.SPANNABLE);
    holder.tvStatus.setText(stringBuilder, TextView.BufferType.SPANNABLE);
    holder.tvBoardCastMessage.setText(data.getDeviceLocationInfoDB().getBroadCastMessage());

    holder.devicemapping.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putString("mac_id", data.getDeviceType());
        bundle.putString("location1", data.getDeviceLocationInfoDB().getLocationName());
        bundle.putString("major", data.getMajor());
        bundle.putString("minor", data.getMinor());
        Intent intent = new Intent(mContext, AllMapping.class);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
        Log.i(">>>>>>>>>>>>>>>>>>>", "TEST");
      }
    });
  }

  @Override
  public int getItemCount() {
    return ufoDeviceListFiltered.size();
  }

  @Override
  public Filter getFilter() {
    return new Filter() {
      @Override
      protected FilterResults performFiltering(CharSequence charSequence) {
        String charString = charSequence.toString();

        if (charString.isEmpty()) {
          ufoDeviceListFiltered = ufoDeviceList;
        } else {
          List<UfoDeviceDB> filteredList = new ArrayList<>();
          for (UfoDeviceDB row : ufoDeviceList) {

            // name match condition. this might differ depending on your requirement
            // here we are looking for name or phone number match
            if (row.getDeviceLocationInfoDB().getLocationName().toLowerCase().contains(charString.toLowerCase()) || row.getMacID().contains(charSequence)) {
              filteredList.add(row);
            }
          }

          ufoDeviceListFiltered = filteredList;
        }

        FilterResults filterResults = new FilterResults();
        filterResults.values = ufoDeviceListFiltered;
        return filterResults;
      }

      @Override
      protected void publishResults(CharSequence constraint, FilterResults results) {

        ufoDeviceListFiltered = (ArrayList<UfoDeviceDB>) results.values;
        notifyDataSetChanged();
      }
    };
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    TextView tvDeviceType, tvMacID, tvMajor, tvMinor, tvLocation, tvCreatedDate, tvStatus, tvBoardCastMessage;
    Button devicemapping;
    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      tvDeviceType = itemView.findViewById(R.id.tvDeviceType);
      tvMacID = itemView.findViewById(R.id.tvMacID);
      tvMajor = itemView.findViewById(R.id.tvMajor);
      tvMinor = itemView.findViewById(R.id.tvMinor);
      tvLocation = itemView.findViewById(R.id.tvDeviceLocation);
      tvCreatedDate = itemView.findViewById(R.id.tvUpdate);
      tvStatus = itemView.findViewById(R.id.tvStatus);
      tvBoardCastMessage = itemView.findViewById(R.id.tvBoardCastMessage);
      devicemapping = itemView.findViewById(R.id.devicemapping);

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

  public void setOnItemClickListener(RegisterDeviceListAdapter.OnItemClickListener listener) {
    mListener = listener;
  }
}
