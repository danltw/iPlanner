package com.project42.iplanner;


/*
File to store all the required URLs for iPlanner database transactions
 */

public class AppConfig {

    public static final String SBAPP_ID = "661E390D-B94E-410D-BBFB-6FBF78BFFB62";

    /*public static final String URL_UPDATE= "http://10.0.2.2/iplanner/account.php";
    public static final String URL_REGISTER = "http://10.0.2.2/iplanner/register_account.php";*/

    public static final String URL_LOGGIN = "http://project42-iplanner.000webhostapp.com/account.php";
    public static final String URL_REG = "http://project42-iplanner.000webhostapp.com/account.php";
    public static final String URL_UPDATE= "http://project42-iplanner.000webhostapp.com/account.php";
    // If: app is tested within an emulator, change host of ip to 10.0.2.2
    // Else: change host of ip to host of server accordingly
    public static final String URL_RECOMMENDED = "http://project42-iplanner.000webhostapp.com/get_recommended_poi.php";
    public static final String URL_SEARCH = "http://project42-iplanner.000webhostapp.com/get_search_poi.php";
    public static final String URL_PSI ="https://api.data.gov.sg/v1/environment/psi?date=";
    public static final String URL_UVI ="https://api.data.gov.sg/v1/environment/uv-index?date=";
    public static final String URL_WEATHER ="https://api.data.gov.sg/v1/environment/24-hour-weather-forecast?date=";
    public static final String URL_ADDBOOKMARKS = "http://project42-iplanner.000webhostapp.com/add_bookmark.php";
    public static final String URL_ITINERARY = "http://project42-iplanner.000webhostapp.com/getItinerary.php";
    public static final String URL_ITINERARYSPINNER = "http://project42-iplanner.000webhostapp.com/getSpinner.php";
    public static final String URL_GROUP = "http://project42-iplanner.000webhostapp.com/group.php";
    public static final String URL_BOOKING = "http://project42-iplanner.000webhostapp.com/bookmark.php";
}
