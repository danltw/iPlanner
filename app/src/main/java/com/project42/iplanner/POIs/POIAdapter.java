package com.project42.iplanner.POIs;

import android.content.Context;
import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RatingBar;
import android.widget.TextView;

import com.project42.iplanner.R;

import java.util.ArrayList;
import java.util.List;

public class POIAdapter extends RecyclerView.Adapter<POIAdapter.ViewHolder> implements Filterable {
    private Context context;
    private List<POI> poiList;
    private List<POI> filteredPoi;

    public POIAdapter(Context context, List<POI> poiList) {
        this.context = context;
        this.poiList = poiList;
        this.filteredPoi = poiList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recommended_poi_row, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        POI poi = filteredPoi.get(position);

        float ratingvalue  = (float) poi.getRating();
        holder.poiid.setText(String.valueOf(poi.getLocationID()));
        holder.poiname.setText(String.valueOf(poi.getLocationName()));
        holder.poicost.setText("S$" + String.valueOf(poi.getCost()));
        holder.poirating.setRating(ratingvalue);

    }

    @Override
    public int getItemCount() {
        return filteredPoi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView poiid, poiname, poicost;
        public RatingBar poirating;
        public ViewHolder(View itemView) {
            super(itemView);

            poiid = itemView.findViewById(R.id.poiid);
            poiname = itemView.findViewById(R.id.poiname);
            poicost = itemView.findViewById(R.id.poiprice);
            poirating = (RatingBar) itemView.findViewById(R.id.poirating);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredPoi = poiList;
                } else {
                    List<POI> filteredList = new ArrayList<>();
                    for (POI row : poiList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getLocationName().toLowerCase().contains(charString.toLowerCase()) || row.getAddress().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    poiList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredPoi;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredPoi = (ArrayList<POI>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
