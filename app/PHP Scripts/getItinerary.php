<?php

// include db connect class
require_once(__DIR__.'/include/DB_Connect.php');

// connecting to db
$db = new DB_CONNECT();
$con = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD) or die(mysqli_error($con));

 
$sql = "select * from id7469667_iplanner.itinerary";
 
$res = mysqli_query($con,$sql);
 
$result = array();
 
while($row = mysqli_fetch_array($res)){
array_push($result,
array('itinerary_id'=>$row[0],
'itinerary_name'=>$row[4],
'visit_date'=>$row[6]

));
}
 
echo json_encode($result);
 
mysqli_close($con);
 
?>



