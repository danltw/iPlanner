package com.project42.iplanner.POIs;

public class POI {

    private int locationID;

    private String locationName;

    private String address;

    private int postalCode;

    public enum EvnType {INDOOR, OUTDOOR};

    private double rating;

    private double cost;

    private String startHrs;

    private String endHrs;

    private String openingDays;

    private String description;

    public enum WeatherType {SUNNY, RAINY, CLOUDY, STORMY}

    private double UVI;

    private double PSI;

    public POI(int locationID, String locationName, String address, int postalCode, double rating, double cost, String startHrs,
               String endHrs, String openingDays, String description, double UVI, double PSI) {
        this.locationID = locationID;
        this.locationName = locationName;
        this.address = address;
        this.postalCode = postalCode;
        this.rating = rating;
        this.cost = cost;
        this.startHrs = startHrs;
        this.endHrs = endHrs;
        this.openingDays = openingDays;
        this.description = description;
        this.UVI = UVI;
        this.PSI = PSI;
    }

    public int getLocationID() {
        return locationID;
    }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getStartHrs() {
        return startHrs;
    }

    public void setStartHrs(String startHrs) {
        this.startHrs = startHrs;
    }

    public String getEndHrs() {
        return endHrs;
    }

    public void setEndHrs(String endHrs) {
        this.endHrs = endHrs;
    }

    public String getOpeningDays() {
        return openingDays;
    }

    public void setOpeningDays(String openingDays) {
        this.openingDays = openingDays;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getUVI() {
        return UVI;
    }

    public void setUVI(double UVI) {
        this.UVI = UVI;
    }

    public double getPSI() {
        return PSI;
    }

    public void setPSI(double PSI) {
        this.PSI = PSI;
    }
}
