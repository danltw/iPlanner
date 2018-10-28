<?php

class DB_Functions {

    private $con;

    // constructor
    function __construct() {
        // include db connect class
		require_once(__DIR__.'/DB_Connect.php');

		// connecting to db
		$db = new DB_CONNECT();
		$this->con = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD) or die(mysqli_error($con));
    }

    // destructor
    function __destruct() {

    }

    /**
     * Storing new user
     * returns user details
     */
    public function storeUser($username, $password) {
        $user_id = uniqid('', true);
        $hash = $this->hashSSHA($password);
        $enc_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt
 
        $stmt = $this->con->prepare("INSERT INTO id7469667_iplanner.account(user_id, username, enc_password, salt) VALUES(?, ?, ?, ?)");
        $stmt->bind_param("ssss", $user_id, $username,  $enc_password, $salt);
        $result = $stmt->execute();
        $stmt->close();
 
        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM id7469667_iplanner.account WHERE username = ?");
            $stmt->bind_param("s", $username);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $user;
        } else {
            return false;
        }
    }

    /**
     * Get user by username and password
     */
    public function getUserByEmailAndPassword($username, $password) {

        $stmt = $this->con->prepare("SELECT * FROM id7469667_iplanner.account WHERE username = ?");
		
		if ($stmt == false) {
			return NULL;
		}

        $stmt->bind_param("s", $username);

        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();

            // verifying user password
            $salt = $user['salt'];
            $enc_password = $user['enc_password'];
            $hash = $this->checkhashSSHA($salt, $password);
            // check for password equality
            if ($enc_password == $hash) {
                // user authentication details are correct
                return $user;
            }
        } else {
            return NULL;
        }
    }
	    

    /**
     * Check user is existed or not
     */
    public function isUserExisted($username) {
        $stmt = $this->con->prepare("SELECT username from id7469667_iplanner.account WHERE username = ?");

        $stmt->bind_param("s", $username);

        $stmt->execute();

        $stmt->store_result();

        if ($stmt->num_rows > 0) {
            // user existed
            $stmt->close();
            return true;
        } else {
            // user not existed
            $stmt->close();
            return false;
        }
    }

    /**
     * Encrypting password
     * @param password
     * returns salt and encrypted password
     */
    public function hashSSHA($password) {

        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }

    /**
     * Decrypting password
     * @param salt, password
     * returns hash string
     */
    public function checkhashSSHA($salt, $password) {

        $hash = base64_encode(sha1($password . $salt, true) . $salt);

        return $hash;
    }

}

?>