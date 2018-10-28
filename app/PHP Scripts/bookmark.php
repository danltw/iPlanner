<?php

// include db connect class
require_once(__DIR__.'/include/DB_Connect.php');

// connecting to db
$db = new DB_CONNECT();
$con = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD) or die(mysqli_error($con));

//$sql = "SELECT * FROM id7469667_iplanner.bookmark JOIN id7469667_iplanner.poi ON bookmark.POI_ID = location_id";
$sql = "SELECT * FROM id7469667_iplanner.bookmark, id7469667_iplanner.poi, id7469667_iplanner.account WHERE bookmark.user_id = account.user_id AND bookmark.POI_ID = location_id ";

$res = mysqli_query($con, $sql);

// Get locations from poi table
$result = array();

while($row = mysqli_fetch_array($res))
{
    array_push($result, 
    array('bookmarkID' => $row[0],
    'poiID' => $row[1]));
    
    echo json_encode($row);

}

// echoing JSON response
echo json_encode($result);
mysqli_close($con);
?>
