package com.project42.iplanner.Itineraries;

import android.os.Parcel;
import android.os.Parcelable;

import com.project42.iplanner.POIs.POI;

import java.sql.Time;
import java.util.Date;
import java.util.HashMap;

/** Represents an itinerary.
 * @author Team42
 * @version 1.0
 */
public class Itinerary implements Parcelable {

    private int itinerary_id;

    private String POI_id;

    private String itinerary_name;

    private Date visit_date;

    private String visit_time;

    /** Creates a itinerary with the specified itinerary_id, itinerary_name, visit_date.
     * @param itinerary_id The itinerary ID assigned.
     * @param itinerary_name The name of the itinerary as entered by the creator.
     * @param visit_date The date of visit for the itinerary.
     * @param visit_time The time of visit for the itinerary.
     */
    public Itinerary(int itinerary_id,String POI_id, String itinerary_name, Date visit_date, String visit_time ) {
        this.itinerary_id = itinerary_id;
        this.POI_id = POI_id;
        this.itinerary_name = itinerary_name;
        // this.lstPOIs = lstPOIs;
        this.visit_date = visit_date;
        this.visit_time = visit_time;
    }

    /**
     * Get the current itinerary's itinerary_id property.
     * The itinerary_id property is essential to keep track of every itinerary created.
     * @return The current itinerary's itineraryID.
     */
    public int getItineraryID() {
        return itinerary_id;
    }

    /**
     * Set the current itinerary's itineraryID property.
     * The itinerary_id property is essential to keep track of every itinerary created.
     * @param itinerary_id The itinerary ID assigned to the itinerary.
     */
    public void setItineraryID(int itinerary_id) {
        this.itinerary_id = itinerary_id;
    }

    /**
     * Get the current itinerary's POI_id property.
     * The POI_id property is essential to keep track of every POI in each itinerary created.
     * @return The current itinerary's POI_ID.
     */
    public String getPOIID() {
        return POI_id;
    }

    /**
     * Set the current itinerary's POI_id property.
     * The POI_id property is essential to keep track of every POI in each itinerary created.
     * @param POI_id The current itinerary's POI_ID.
     */
    public void setPOI_id(String POI_id) {
        this.POI_id = POI_id;
    }

    /**
     * Get the current itinerary's itinerary_name property.
     * The itinerary_name property is essential to allow the user to distinguish each itinerary record.
     * @return The current itinerary's itinerary name.
     */
    public String getItineraryName() {
        return itinerary_name;
    }

    /**
     * Set the current itinerary's itinerary_name property.
     * The itinerary_name property is essential to allow the user to distinguish each itinerary record.
     * @param itinerary_name The itinerary name assigned to the itinerary.
     */
    public void setItineraryName(String itinerary_name) {
        this.itinerary_name = itinerary_name;
    }

    /**
     * Get the current itinerary's visit_date property.
     * The visit_date property is associated with each itinerary record.
     * @return The current itinerary's visit date.
     */
    public Date getVisitDate() {
        return visit_date;
    }

    /**
     * Get the current itinerary's visit_time property.
     * The visit_time property is associated with each itinerary record.
     * @return The current itinerary's visit time.
     */
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
