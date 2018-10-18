package com.project42.iplanner;


/*
File to store all the required URLs for iPlanner database transactions
 */

public class AppConfig {

    public static final String URL_LOGIN = "http://project42-iplanner.000webhostapp.com/login_account.php";
    public static final String URL_REGISTER = "http://project42-iplanner.000webhostapp.com/register_account.php";
    // If: app is tested within an emulator, change host of ip to 10.0.2.2
    // Else: change host of ip to host of server accordingly
    public static final String CONN_TESTER = "http://project42-iplanner.000webhostapp.com/conntester.php";
    public static final String get_recommended_poi = "http://project42-iplanner.000webhostapp.com/get_recommended_poi.php";
}
