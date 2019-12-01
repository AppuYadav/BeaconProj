package com.example.beaconapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.beaconapp.Model.MappingBeacon;
import com.example.beaconapp.R;
import java.util.ArrayList;
import java.util.List;

public class MappingList extends RecyclerView.Adapter<MappingList.ViewHolder> {

  private LayoutInflater layoutInflater;
  private List<MappingBeacon> MappingList = new ArrayList<MappingBeacon>();
  private Context mContext;
  private OnItemClickListener mListener;

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = layoutInflater.from(parent.getContext()).inflate(R.layout.mapping_row, parent, false);
    return new MappingList.ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    MappingBeacon mappingBeacon = MappingList.get(position);
    holder.tvDestination.setText(mappingBeacon.getLocation_name());
    holder.tvnoofHops.setText(mappingBeacon.getNumberofhops().toString());
    holder.tvDistance.setText(mappingBeacon.getDistance().toString());
    holder.tvDirection.setText(mappingBeacon.getDirection().toString());
    holder.created_date.setText(mappingBeacon.getCreated_date());
    holder.updated_date.setText(mappingBeacon.getUpdated_date());
  }

  public MappingList(List<MappingBeacon> mappingBeacons,
      Context mContext
  ) {
    this.MappingList = mappingBeacons;
    this.mContext = mContext;
  }

  @Override
  public int getItemCount() {
    return MappingList.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    TextView tvDestination, tvnoofHops, tvDistance, tvDirection, updated_date, created_date;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      tvDestination = itemView.findViewById(R.id.tvDestination);
      tvnoofHops = itemView.findViewById(R.id.tvnoofHops);
      tvDistance = itemView.findViewById(R.id.tvDistance);
      tvDirection = itemView.findViewById(R.id.tvDirection);
      created_date = itemView.findViewById(R.id.tvCreatedDate);
      updated_date = itemView.findViewById(R.id.tvUpdatedDate);

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

  public void setOnItemClickListener(MappingList.OnItemClickListener listener) {
    mListener = listener;
  }
}
