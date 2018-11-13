package com.project42.iplanner.Itineraries;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.vipulasri.timelineview.TimelineView;
import com.project42.iplanner.POIs.POI;
import com.project42.iplanner.R;
import com.project42.iplanner.Itineraries.Itinerary;
import com.project42.iplanner.Utilities.DateTimeUtils;
import com.project42.iplanner.Utilities.VectorDrawableUtils;

import java.sql.Time;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by HP-HP on 05-12-2015.
 */
public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder> {


    private List<Timeline> mFeedList;
    private Context mCtx;

    private LayoutInflater mLayoutInflater;

    public TimeLineAdapter( Context mCtx, List <Timeline> feedList) {

        this.mCtx = mCtx;
        mFeedList = feedList;


    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position,getItemCount());
    }

    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view=inflater.inflate(R.layout.item_timeline, null);
        return new TimeLineAdapter.TimeLineViewHolder(view,viewType);

    }


    @Override
    public void onBindViewHolder(TimeLineViewHolder holder, int position) {

        Timeline timeLineModel = mFeedList.get(position);


        if(!timeLineModel.getPOIName().isEmpty()) {
            holder.mDate.setVisibility(View.VISIBLE);

        }
        else
            holder.mDate.setVisibility(View.GONE);

        holder.mDate.setText(timeLineModel.getPOIName());

        if(timeLineModel.getVisitTime()!=null) {
            holder.mMessage.setVisibility(View.VISIBLE);

        }
        else
            holder.mMessage.setVisibility(View.GONE);

        holder.mMessage.setText(String.valueOf(timeLineModel.getVisitTime()));
    }

    @Override
    public int getItemCount() {
        return (mFeedList!=null? mFeedList.size():0);
    }


    public class TimeLineViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_timeline_date)
        TextView mDate;
        @BindView(R.id.text_timeline_title)
        TextView mMessage;
        @BindView(R.id.time_marker)
        TimelineView mTimelineView;

        public TimeLineViewHolder(View itemView, int viewType) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            mTimelineView.initLine(viewType);
        }
    }
}
