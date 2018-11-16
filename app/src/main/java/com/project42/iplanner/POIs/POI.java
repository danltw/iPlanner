package com.project42.iplanner.POIs;

/** Represents a Place of Interest.
 * @author Team42
 * @version 1.0
 */
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

    private String img;

    /** Creates a Place of Interest (POI) with the specified itinerary_id, itinerary_name, visit_date.
     * @param locationID The location ID that is essential to keep track of each POI.
     * @param locationName The location name associated with the POI.
     * @param address The full address needed to locate the POI.
     * @param postalCode The postal code needed to locate the region of the POI.
     * @param rating A metric which is gathered from all users, as an indication of the popularity of the POI.
     * @param cost The estimated to-be incurred expenses for every visit to the POI.
     * @param startHrs The start of the POI's operating hours.
     * @param endHrs The end of the POI's operating hours.
     * @param openingDays The opening days of the POI.
     * @param description A brief description of the POI, to entice the users.
     * @param UVI A weather-related metric which indicates the intensity of the sunlight.
     * @param PSI A environment-related metric which indicates the extent of the pollution.
     * @param img The image path associated with the POI.
     */
    public POI(int locationID, String locationName, String address, int postalCode, double rating, double cost, String startHrs,
               String endHrs, String openingDays, String description, double UVI, double PSI, String img) {
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
        this.img = img;
    }

    /**
     * Get the current POI's locationID property.
     * The locationID property is essential to keep track of every POI record.
     * @return The current location's locationID.
     */
    public int getLocationID() {
        return locationID;
    }

    /**
     * Set the current POI's locationID property.
     * The locationID property is essential to keep track of every POI record.
     * @param locationID The location ID assigned to the POI.
     */
    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }

    /**
     * Get the current POI's locationName property.
     * The locationName property is essential to keep track of every POI record.
     * @return The current location's locationName.
     */
    public String getLocationName() {
        return locationName;
    }

    /**
     * Set the current POI's locationName property.
     * The locationName property is associated with every POI record.
     * @param locationName The locationName assigned to the POI.
     */
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    /**
     * Get the current POI's address property.
     * The address property is essential to keep track of every POI record.
     * @return The current location's address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Set the current POI's address property.
     * The address property is associated with every POI record.
     * @param address The address assigned to the POI.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Get the current POI's postalCode property.
     * The postalCode property is essential to pinpoint the exact location of every POI record.
     * @return The current location's postalCode.
     */
    public int getPostalCode() {
        return postalCode;
    }

    /**
     * Set the current POI's postalCode property.
     * The postalCode property is associated with every POI record.
     * @param postalCode The current location's postalCode.
     */
    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Get the current POI's rating property.
     * The rating property is essential as an indication of the popularity of the current poi.
     * @return The current poi's rating.
     */
    public double getRating() {
        return rating;
    }

    /**
     * Set the current POI's rating property.
     * The rating property is associated with every POI record.
     * @param rating The current location's popularity rating.
     */
    public void setRating(double rating) {
        this.rating = rating;
    }

    /**
     * Get the current POI's cost property.
     * The cost property is an indication of the estimated cost of the current poi (whenever applicable).
     * @return The current poi's rating.
     */
    public double getCost() {
        return cost;
    }

    /**
     * Set the current POI's cost property.
     * The cost property is associated with every POI record.
     * @param cost The current location's cost.
     */
    public void setCost(double cost) {
        this.cost = cost;
    }

    /**
     * Get the current POI's startHrs property.
     * The startHrs property is the time which the current poi opens at (whenever applicable).
     * @return The current poi's startHrs.
     */
    public String getStartHrs() {
        return startHrs;
    }

    /**
     * Set the current POI's startHrs property.
     * The startHrs property is associated with every POI record.
     * @param startHrs The current location's startHrs.
     */
    public void setStartHrs(String startHrs) {
        this.startHrs = startHrs;
    }

    /**
     * Get the current POI's endHrs property.
     * The endHrs property is the time which the current poi closes at (whenever applicable).
     * @return The current poi's endHrs.
     */
    public String getEndHrs() {
        return endHrs;
    }

    /**
     * Set the current POI's endHrs property.
     * The endHrs property is associated with every POI record.
     * @param endHrs The current location's endHrs.
     */
    public void setEndHrs(String endHrs) {
        this.endHrs = endHrs;
    }

    /**
     * Get the current POI's openingDays property.
     * The openingDays property are the operational days of current poi (whenever applicable).
     * @return The current poi's endHrs.
     */
    public String getOpeningDays() {
        return openingDays;
    }

    /**
     * Set the current POI's openingDays property.
     * The openingDays property is associated with every POI record.
     * @param openingDays The current location's openingDays.
     */
    public void setOpeningDays(String openingDays) {
        this.openingDays = openingDays;
    }

    /**
     * Get the current POI's description property.
     * The description property is the brief description on current poi.
     * @return The current poi's description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the current POI's description property.
     * The description property is associated with every POI record.
     * @param description The current location's description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the current POI's UVI property.
     * The UVI property is the sunlight intensity of current poi.
     * @return The current poi's UVI.
     */
    public double getUVI() {
        return UVI;
    }

    /**
     * Set the current POI's UVI property.
     * The UVI property is associated with every POI record.
     * @param UVI The current location's UVI.
     */
    public void setUVI(double UVI) {
        this.UVI = UVI;
    }

    /**
     * Get the current POI's PSI property.
     * The PSI property is how polluted the current poi is (whenever applicable).
     * @return The current poi's PSI.
     */
    public double getPSI() {
        return PSI;
    }

    /**
     * Set the current POI's PSI property.
     * The PSI property is associated with every POI record.
     * @param PSI The current location's PSI.
     */
    public void setPSI(double PSI) {
        this.PSI = PSI;
    }

    /**
     * Get the current POI's img property.
     * The img property is the image file path of current poi.
     * @return The current poi's img.
     */
    public String getImg() {
        return img;
    }

    /**
     * Set the current POI's img property.
     * The img property is associated with every POI record.
     * @param img The current location's img.
     */
    public void setImg(String img) {
        this.img = img;
    }
}
