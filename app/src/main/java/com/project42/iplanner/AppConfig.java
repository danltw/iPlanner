package com.project42.iplanner;


/*
File to store all the required URLs for iPlanner database transactions
 */

public class AppConfig {

    public static final String SBAPP_ID = "661E390D-B94E-410D-BBFB-6FBF78BFFB62";

    public static final String URL_LOGIN = "http://project42-iplanner.000webhostapp.com/login_account.php";
    public static final String URL_REGISTER = "http://project42-iplanner.000webhostapp.com/register_account.php";
    // If: app is tested within an emulator, change host of ip to 10.0.2.2
    // Else: change host of ip to host of server accordingly
    public static final String CONN_TESTER = "http://project42-iplanner.000webhostapp.com/conntester.php";
    public static final String URL_RECOMMENDED = "http://project42-iplanner.000webhostapp.com/get_recommended_poi.php";
    public static final String URL_ITINERARY = "http://project42-iplanner.000webhostapp.com/getItinerary.php";
    public static final String URL_GROUP = "http://10.0.2.2/iplanner/group.php";
}
