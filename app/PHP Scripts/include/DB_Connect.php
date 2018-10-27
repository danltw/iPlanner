<?php
 
/**
 * A class file to connect to database
 */

class DB_CONNECT {
	
    // constructor
    function __construct() {
        // connecting to database
        $this->connect();
    }
 
    // destructor
    function __destruct() {
        // closing db connection
        //$this->close();
    }
 
    /**
     * Function to connect with database
     */
    function connect() {
		
        // import database connection variables
        //require_once('D:\wamp64\www\iPlanner\include\DB_Config.php');
		require_once(__DIR__.'/DB_Config.php');
 
        // Connecting to mysql database
        $con = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD) or die(mysqli_error($con));
 
        // Selecing database
        $db = mysqli_select_db($con, DB_DATABASE) or die(mysqli_error($con)) or die(mysqli_error($con));
        
		// returing connection cursor
		//echo "Connected liao la cb!";
        return $con;
    }
 
    /**
     * Function to close db connection
     */
    /* function close() {
        // closing db connection
        mysqli_close($con);
    } */
 
}
 
?>