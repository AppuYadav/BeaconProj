package com.example.beaconapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import butterknife.BindView;
import com.example.beaconapp.Adapter.RegisterDeviceListAdapter.OnItemClickListener;
import com.example.beaconapp.Adapter.TagAdapter.ViewHolder;
import com.example.beaconapp.Model.Database.Tag;
import com.example.beaconapp.Model.Database.UfoDeviceDB;
import com.example.beaconapp.R;
import java.util.ArrayList;
import java.util.List;

public class TagAdapter extends Adapter<ViewHolder> implements
    Filterable {

  private LayoutInflater layoutInflater;
  private OnItemClickListener mListener;
  private List<Tag> tagList = new ArrayList<Tag>();
  private List<Tag> tagListFiltered = new ArrayList<Tag>();
  private Context mContext;

  public TagAdapter(List<Tag> tag,
      Context mContext
  ) {
    this.tagList = tag;
    this.mContext = mContext;
    this.tagListFiltered = tag;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = layoutInflater.from(parent.getContext()).inflate(R.layout.tag_row, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    Tag tag = tagList.get(position);
    holder.tagId.setText(tag.getTagID());
    holder.verboseName.setText(tag.getVerboseTitle());
    holder.message.setText(tag.getMessage());
    holder.created_Date.setText(tag.getCreated_Date());
    holder.updated_Date.setText(tag.getUpdated_Date());
  }

  @Override
  public int getItemCount() {
    return tagListFiltered.size();
  }

  @Override
  public Filter getFilter() {
    return new Filter() {
      @Override
      protected FilterResults performFiltering(CharSequence charSequence) {
        String charString = charSequence.toString();

        if (charString.isEmpty()) {
          tagListFiltered = tagList;
        } else {
          List<Tag> filteredList = new ArrayList<>();
          for (Tag row : tagList) {

            // name match condition. this might differ depending on your requirement
            // here we are looking for name or phone number match
            if (row.getTagID().toLowerCase().contains(charString.toLowerCase()) || row.getMessage().toLowerCase().contains(charSequence)) {
              filteredList.add(row);
            }
          }

          tagListFiltered = filteredList;
        }

        FilterResults filterResults = new FilterResults();
        filterResults.values = tagListFiltered;
        return filterResults;
      }

      @Override
      protected void publishResults(CharSequence constraint, FilterResults results) {

        tagListFiltered = (ArrayList<Tag>) results.values;
        notifyDataSetChanged();
      }
    };
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    TextView tagId, verboseName, message, created_Date, updated_Date;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      tagId = itemView.findViewById(R.id.tvTagID);
      verboseName = itemView.findViewById(R.id.tvtagverbosename);
      message = itemView.findViewById(R.id.tvmessage);
      created_Date = itemView.findViewById(R.id.tvcreatedDate);
      updated_Date = itemView.findViewById(R.id.tvupdateDate);

      itemView.setOnClickListener(new OnClickListener() {
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

  public void setOnItemClickListener(TagAdapter.OnItemClickListener listener) {
    mListener = listener;
  }
}
