package com.project42.iplanner.Itineraries;

import com.project42.iplanner.POIs.POI;

import java.sql.Time;
import java.util.Date;
import java.util.HashMap;

public class Itinerary {

    private int itineraryID;

    private HashMap<POI, Time> lstPOIs;

    private Date visitDate;

    public Itinerary() {

    }

    public Itinerary(int itineraryID, HashMap<POI, Time> lstPOIs, Date visitDate) {
        this.itineraryID = itineraryID;
        this.lstPOIs = lstPOIs;
        this.visitDate = visitDate;
    }

    public int getItineraryID() {
        return itineraryID;
    }

    public void setItineraryID(int itineraryID) {
        this.itineraryID = itineraryID;
    }

    public HashMap<POI, Time> getLstPOIs() {
        return lstPOIs;
    }

    public void setLstPOIs(HashMap<POI, Time> lstPOIs) {
        this.lstPOIs = lstPOIs;
    }

    public Date getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(Date visitDate) {
        this.visitDate = visitDate;
    }
}
