package com.project42.iplanner.Itineraries;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/** Represents an timeline.
 * @author Team42
 * @version 1.0
 */
public class Timeline implements Parcelable {

    private int POI_id;

    private String poi_name;

    private String visit_time;

    /** Creates a timeline for an itinerary with the POI_id, poi_name, visit_time..
     * @param POI_id The POI_id of the intended POI.
     * @param poi_name The name of the to-be visited poi.
     * @param visit_time The time of visit for the POI.
     */
    public Timeline(int POI_id, String poi_name,String visit_time) {

        this.POI_id = POI_id;
        this.poi_name = poi_name;
        this.visit_time = visit_time;

    }

    /**
     * Get the current timeline's poi_name property.
     * The poi_name property is essential to keep track of every POI in each itinerary created.
     * @return The current timeline's poi_name.
     */
    public String getPOIName() {
        return poi_name;
    }

    /**
     * Get the current itinerary's visit_time property.
     * The visit_time property is associated with each itinerary record.
     * @return The current itinerary's visit time.
     */
    public String getVisitTime() { return visit_time; }

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
