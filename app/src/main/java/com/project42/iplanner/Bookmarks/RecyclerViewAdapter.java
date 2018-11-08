package com.project42.iplanner.Bookmarks;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.project42.iplanner.POIs.POI;
import com.project42.iplanner.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>
{
    //private ArrayList<POI> poiDetail = new ArrayList<POI>();
    private ArrayList<POI> poiList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView poi_name;
        private TextView poi_address;
        private TextView poi_id;
        private TextView poi_desc;

        RelativeLayout relativeLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            poi_id = itemView.findViewById(R.id.poi_id);
            poi_name = itemView.findViewById(R.id.poi_name);
            //poi_desc = itemView.findViewById(R.id.poi_desc);
            poi_address = itemView.findViewById(R.id.poi_address);
            //poi_postal = itemView.findViewById(R.id.poi_postal);

            //poi_details_id = itemView.findViewById(R.id.poi_details_id);
        }
    }

    public RecyclerViewAdapter(Context context,ArrayList<POI> poiList) {
        this.context = context;
        this.poiList = poiList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        POI poi = poiList.get(position);

        holder.poi_id.setText(String.valueOf(poi.getLocationID()));
        //holder.poi_name.setText(poi.getLocationName());
        holder.poi_name.setText(String.valueOf(poi.getLocationName()));
        //holder.poi_desc.setText(String.valueOf(poi.getDescription()));
        holder.poi_address.setText(String.valueOf(poi.getAddress()));
        //holder.poi_address.setText(poi.getAddress());
        //holder.poi_postal.setText(poi.getPostalCode());
    }

    @Override
    public int getItemCount() {
        return poiList.size();
    }


    public void removeItem(int position) {
        //poiList.remove(position);
        poiList.remove(poiList.get(position));
        //String URL = "http://project42-iplanner.000webhostapp.com/bookmark.php";
        notifyItemRemoved(position);

    }

   /* public void restoreItem(String item, int position) {
        poiList.add(position, item);
        notifyItemInserted(position);
    }*/

    public ArrayList<POI> getData() {
        return poiList;
    }
}
