<?php

// include db connect class
require_once(__DIR__.'/include/DB_Connect.php');

// connecting to db
$db = new DB_CONNECT();
$con = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD) or die(mysqli_error($con));

$sql = "SELECT * FROM id7469667_iplanner.poi WHERE UVI < 9 AND PSI < 100";

$res = mysqli_query($con, $sql);

// Get locations from poi table
$result = array();

while($row = mysqli_fetch_array($res))
{
    array_push($result, 
    array('locationID' => $row[0],
    'locationName' => $row[1],
    'address' => $row[2],
    'postalCode' => $row[3],
    'EvnType' => $row[4],
    'rating' => $row[5],
    'cost' => $row[6],
    'startHrs' => $row[7],
    'endHrs' => $row[8],
    'openingDays' => $row[9],
    'description' => $row[10],
    'WeatherType' => $row[11],
    'UVI' => $row[12],
    'PSI' => $row[13]));

}

// echoing JSON response
echo json_encode($result);
mysqli_close($con);
?>
