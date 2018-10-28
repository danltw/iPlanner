<?php

// include db connect class
require_once(__DIR__.'/include/DB_Connect.php');

// connecting to db
$db = new DB_CONNECT();
$con = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD) or die(mysqli_error($con));

 function createBookmark($poiid, $userid){
 $stmt = $this->con->prepare("INSERT INTO id7469667_iplanner.bookmark (POI_id, user_id) VALUES (?, ?)");
 $stmt->bind_param("ii", $poiid, $userid);
 if($stmt->execute())
 return true; 
 return false; 
 }

?>