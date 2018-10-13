package com.project42.iplanner;


/*
File to store all the required URLs for iPlanner database transactions
 */

public class AppConfig {

    public static final String URL_LOGIN = "localhost/iPlanner/Login.php";

    // If: app is tested within an emulator, change host of ip to 10.0.2.2
    // Else: change host of ip to host of server accordingly
    public static final String CONN_TESTER = "http://10.0.2.2/iPlanner/conntester.php";

}
