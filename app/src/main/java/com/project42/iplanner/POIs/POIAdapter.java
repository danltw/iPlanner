package com.project42.iplanner.POIs;

import android.content.Context;
import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.project42.iplanner.R;

import java.util.List;

public class POIAdapter extends RecyclerView.Adapter<POIAdapter.ViewHolder> {
    private Context context;
    private List<POI> poiList;

    public POIAdapter(Context context, List<POI> poiList) {
        this.context = context;
        this.poiList = poiList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recommended_poi_row, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        POI poi = poiList.get(position);

        float ratingvalue  = (float) poi.getRating();
        holder.poiid.setText(String.valueOf(poi.getLocationID()));
        holder.poiname.setText(String.valueOf(poi.getLocationName()));
        holder.poicost.setText(String.valueOf(poi.getCost()));
        holder.poirating.setRating(ratingvalue);

    }

    @Override
    public int getItemCount() {
        return poiList.size();
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

}
