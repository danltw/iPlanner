<?php

require_once(__DIR__.'/include/DB_Functions.php');
$db = new DB_Functions();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['username']) && isset($_POST['password'])) {

	//echo $_POST['username'];
	//echo $_POST['password'];
	
    // receiving the post params
    $username = $_POST['username'];
    $password = $_POST['password'];

    // check if user is already existed with the same username
    if ($db->isUserExisted($username)) {
        // user already existed
        $response["error"] = TRUE;
        $response["error_msg"] = "User already existed with " . $username;
        echo json_encode($response);
    } else {
        // create a new user
        $user = $db->storeUser($username, $password);
        if ($user) {
            // user stored successfully
            $response["error"] = FALSE;
            $response["user"]["username"] = $user["username"];
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in registration!";
            echo json_encode($response);
        }
    }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters (username or password) is missing!";
    echo json_encode($response);
}
?>