<?php

// array for JSON response
$response = array();

// include db connect class
require_once(__DIR__.'/include/DB_Connect.php');

// connecting to db
$db = new DB_CONNECT();
$con = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD) or die(mysqli_error($con));

// Get locations from poi table
$result = mysqli_query($con, "SELECT * FROM id7469667_iplanner.poi WHERE UVI < 9 AND PSI < 100");

if($result == true)
{
    $response["POI"] = array();
    foreach($result as $row)
    {
        $poi = array();

        $poi["locationID"] = $row["location_id"];
        $poi["locationName"] = $row["location_name"];
        $poi["address"] = $row["location_address"];
        $poi["postalCode"] = $row["location_postalcode"];
        $poi["evnType"] = $row["evn_type"];
        $poi["rating"] = $row["location_rating"];
        $poi["cost"] = $row["location_cost"];
        $poi["startHrs"] = $row["start_hrs"];
        $poi["endHrs"] = $row["end_hrs"];
        $poi["openingDays"] = $row["opening_days"];
        $poi["description"] = $row["location_desc"];
        $poi["weatherType"] = $row["weather_type"];
        $poi["UVI"] = $row["UVI"];
        $poi["PSI"] = $row["PSI"];

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
