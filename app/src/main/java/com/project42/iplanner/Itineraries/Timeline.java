package com.project42.iplanner.Itineraries;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Timeline implements Parcelable {





    private int POI_id;

    private String poi_name;

    private String visit_time;



    public Timeline() {

    }

    public Timeline(int POI_id, String poi_name,String visit_time) {

        this.POI_id = POI_id;
        this.poi_name = poi_name;
        this.visit_time = visit_time;

    }



    public int getPOIID() {
        return POI_id;
    }

    public void setPOI_id(int POI_id) {
        this.POI_id = POI_id;
    }



    public String getPOIName() {
        return poi_name;
    }

    public void setPOIName(String poi_name) {
        this.poi_name = poi_name;
    }

    public String getVisitTime() { return visit_time; }

    public void setVisitTime(String visit_time) {
        this.visit_time = visit_time;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.POI_id);
        dest.writeString(this.poi_name);

    }

    protected Timeline(Parcel in) {
        this.POI_id = in.readInt();
        this.poi_name = in.readString();

    }

    public static final Creator<Timeline> CREATOR = new Creator<Timeline>() {
        @Override
        public Timeline createFromParcel(Parcel source) {
            return new Timeline(source);
        }

        @Override
        public Timeline[] newArray(int size) {
            return new Timeline[size];
        }
    };

}
