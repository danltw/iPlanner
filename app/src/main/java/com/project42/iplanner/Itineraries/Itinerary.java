package com.project42.iplanner.Itineraries;

import com.project42.iplanner.POIs.POI;

import java.sql.Time;
import java.util.Date;
import java.util.HashMap;

public class Itinerary {

    private int itinerary_id;

    private String itinerary_name;

    private Date visit_date;

    public Itinerary() {

    }

    public Itinerary(int itinerary_id, String itinerary_name, Date visit_date) {
        this.itinerary_id = itinerary_id;
        this.itinerary_name = itinerary_name;
        // this.lstPOIs = lstPOIs;
        this.visit_date = visit_date;
    }

    public int getItineraryID() {
        return itinerary_id;
    }

    public void setItineraryID(int itinerary_id) {
        this.itinerary_id = itinerary_id;
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

}
