<?php

// array for JSON response
$response = array();

// include db connect class
require_once(__DIR__.'/DB_Connect.php');

// connecting to db
$db = new DB_CONNECT();

// Get locations from locations table
$result = mysql_query("SELECT * FROM poi") or die(mysql_error());

if(mysql_num_rows($result) > 0)
{
    $response["POI"] = array();

    while($row = mysql_fetch_array($result))
    {
        $poi = array();

        $poi["location_id"] = $row["pid"];
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
}
?>
