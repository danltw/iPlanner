package com.project42.iplanner.Itineraries;

import android.os.Parcel;
import android.os.Parcelable;

import com.project42.iplanner.POIs.POI;

import java.sql.Time;
import java.util.Date;
import java.util.HashMap;

public class Itinerary implements Parcelable {



    private int itinerary_id;

    private String POI_id;

    private String itinerary_name;

    private Date visit_date;

    private String visit_time;

    public Itinerary() {

    }

    public Itinerary(int itinerary_id,String POI_id, String itinerary_name, Date visit_date, String visit_time ) {
        this.itinerary_id = itinerary_id;
        this.POI_id = POI_id;
        this.itinerary_name = itinerary_name;
        // this.lstPOIs = lstPOIs;
        this.visit_date = visit_date;
        this.visit_time = visit_time;
    }

    public int getItineraryID() {
        return itinerary_id;
    }

    public void setItineraryID(int itinerary_id) {
        this.itinerary_id = itinerary_id;
    }

    public String getPOIID() {
        return POI_id;
    }

    public void setPOI_id(String POI_id) {
        this.POI_id = POI_id;
    }


    public String getItineraryName() {
        return itinerary_name;
    }

    public void setItineraryName(String itinerary_name) {
        this.itinerary_name = itinerary_name;
    }


    public Date getVisitDate() {
        return visit_date;
    }

    public String getVisitTime() {
        return visit_time;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.itinerary_id);
        dest.writeString(this.itinerary_name);

    }

    protected Itinerary(Parcel in) {
        this.itinerary_id = in.readInt();
        this.itinerary_name = in.readString();

    }

    public static final Parcelable.Creator<Itinerary> CREATOR = new Parcelable.Creator<Itinerary>() {
        @Override
        public Itinerary createFromParcel(Parcel source) {
            return new Itinerary(source);
        }

        @Override
        public Itinerary[] newArray(int size) {
            return new Itinerary[size];
        }
    };

}
