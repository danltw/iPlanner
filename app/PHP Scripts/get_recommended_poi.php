<?php

// array for JSON response
$response = array();

// include db connect class
require_once(__DIR__.'/include/DB_Connect.php');

// connecting to db
$db = new DB_CONNECT();
$con = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD) or die(mysqli_error($con));

// Get locations from poi table
$result = mysqli_query($con, "SELECT * FROM id7469667_iplanner.poi");

if($result == true)
{
    $response["POI"] = array();
    foreach($result as $row)
    {
        $poi = array();

        $poi["location_id"] = $row["location_id"];
        $poi["location_name"] = $row["location_name"];
        $poi["location_cost"] = $row["location_cost"];
        $poi["location_rating"] = $row["location_cost"];

        array_push($response["POI"], $poi);
    }
    // success
    $response["success"] = 1;

    // echoing JSON response
    echo json_encode($response);
}

else
{
    // no poi found
    $response["success"] = 0;
    $response["message"] = "No locations found";

    // echo no users JSON
    echo json_encode($response);

    echo mysqli_error($con);
}
?>
