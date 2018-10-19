package com.project42.iplanner.Itineraries;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.bumptech.glide.Glide;
import com.project42.iplanner.R;


public class ItineraryAdapter extends RecyclerView.Adapter<ItineraryAdapter.ItineraryViewHolder>{


    private Context mCtx;
    private List<Itinerary> itineraryList;

    public ItineraryAdapter(Context mCtx, List<Itinerary> itineraryList) {
        this.mCtx = mCtx;
        this.itineraryList = itineraryList;
    }

    @Override
    public ItineraryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.itinerary_list, null);
        return new ItineraryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItineraryViewHolder holder, int position) {
        Itinerary itinerary = itineraryList.get(position);

        holder.textViewTitle.setText(itinerary.getItineraryName());
        holder.textViewShortDesc.setText(itinerary.getVisitDate().toString());

//        holder.textViewRating.setText(String.valueOf(product.getRating()));
//        holder.textViewPrice.setText(String.valueOf(product.getPrice()));
    }

    @Override
    public int getItemCount() {
        return itineraryList.size();
    }

    class ItineraryViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewShortDesc, textViewRating, textViewPrice;
        //   ImageView imageView;

        public ItineraryViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewShortDesc = itemView.findViewById(R.id.textViewShortDesc);
//            textViewRating = itemView.findViewById(R.id.textViewRating);
//            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            // imageView = itemView.findViewById(R.id.imageView);
        }
    }


}
