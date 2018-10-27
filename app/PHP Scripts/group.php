<?php

// NOTE: 
// - All indexes for $_POST to be in short form (eg, grpName / grpID)
// - All indexes for retrieved results to be in original column naming convention (eg, group_name, group_id)

error_reporting(E_ALL);
ini_set('display_errors', 1);
class Group
{
	private $PARAMS;

	private $con;
	
	function __construct() {
		
	
		// Getting the DB_Connect.php file
		require_once(__DIR__.'/include/DB_Connect.php');
		
		// Creating a DB_Connect object to connect to database
		$db = new DB_Connect();
		
		// Initializing the connection link of this class
		$this->con = $db->connect();
		
		if ($_POST) {
			
		$method = $_POST['method'];
		
			switch ($method) {
				case "getGroup":
					if (isset($_POST['grpName'])) { 
						$groupName = $_POST['grpName'];
						echo $this->getGroups($groupName);
					}
					else 
						echo $this->getGroups(null); 
					break;
					
				case "addGroup":
					if (isset($_POST['grpUrl']) && isset($_POST['grpName']) && isset($_POST['memberNames']) && isset($_POST['adminNames'])) {
						$groupUrl = $_POST['grpUrl'];
						$groupName = $_POST['grpName'];
						$memberNames = $_POST['memberNames'];
						$adminNames = $_POST['adminNames'];
						echo $this->addGroup($groupName, $groupUrl, $memberNames, $adminNames);
					} 
					else 
						echo $this->displayError("grpUrl: ".$_POST['grpUrl'].", grpName: ".$_POST['grpName'].", memberNames: ".$_POST['memberNames'].", adminNames: ".$_POST['adminNames']);
					break;
					
				case "updateGroup":
					if (isset($_POST['grpUrl']) && isset($_POST['memberNames']) && isset($_POST['adminNames'])) {
						$groupUrl = $_POST['grpUrl'];
						$memberNames = $_POST['memberNames'];
						$adminNames = $_POST['adminNames'];
						echo $this->updateGroup($groupUrl, $memberNames, $adminNames);
					} 
					else 
						echo $this->displayError("grpUrl: ".$_POST['grpUrl'].", memberNames: ".$_POST['memberNames'].", adminNames: ".$_POST['adminNames']);
					break;
					
				case "deleteGroup":
					if (isset($_POST['grpID'])) {
						$groupID = $_POST['grpID'];
						echo $this->deleteGroup($groupID);
					} 
					else 
						echo $this->displayError("grpID: ".$_POST['grpID']);
					break;
					
				default:
					echo $this->displayError("Method not specified");
			}
		}
		else {
			echo $this->displayError("Method not specified");
			/* $groupName = "Grouper5";
			$groupUrl = "sendbird_channel_195731461961359300";
			$memberNames = '"user1","a1"';
			$adminNames = '"user1","hardcoded user"';
			echo $this->addGroup($groupName, $groupUrl, $memberNames, $adminNames); */
		}
		
	}
	
	function getAdminsAndMembers($usernames) {
		
		// getting the userIDs of admins and members via their usernames
		$query = "SELECT `user_id` FROM account WHERE `username` IN ";
		
		// splitting string array and form a combined string
		//$string_split = implode('","', $arrUsernames);
		//$string_split = '"'.$string_split.'"';
		//$query.= "(".$string_split.");";
		$query.="(".$usernames.")";
		
		//echo $query;

		if ($stmt = $this->con->prepare($query)) {
			
			if ($stmt->execute()) {
				$stmt->bind_result($userID);
				$users = array();
		
				while ($stmt->fetch()) {
					
					// adding each userID into an array
					array_push($users, $userID);
				}
				
				// returns list of userIDs in string format without double quotes encapsulating each
				return implode(',', $users);
			
			}
			else {
				//die("Error: ". $this->con->error);
				return $this->displayError($this->con->error);
			}
		}
		else 
			//die("Error: ". $this->con->error);
			return $this->displayError($this->con->error);
		return $this->displayError($this->con->error);;
		
	}
	
	function addGroup($groupName, $groupUrl, $memberNames, $adminNames) {
		$memberIDs = $this->getAdminsAndMembers($memberNames);
		$adminIDs = $this->getAdminsAndMembers($adminNames);
		
		if ((!isset($memberIDs) && !isset($adminIDs)) || (preg_match('/\berror\b/', $memberIDs) || preg_match('/\berror\b/',$adminIDs))) 
			return $this->displayError("Unable to retrieve IDs corresponding to the members and admins provided");
		
		else {
			$query = "INSERT INTO groups (group_name, group_url, member_ids, admin_ids) VALUES (?,?,?,?)";
			if ($stmt = $this->con->prepare($query)) {
			
				$stmt->bind_param('ssss', $groupName, $groupUrl, $memberIDs, $adminIDs);
				$stmt->execute();
				if ($stmt->affected_rows === 1)
					return $this->displaySuccess("Success");
				else if ($stmt->affected_rows === 0 || $stmt->affected_rows > 1) {
					//die("Error: ". $this->con->error);
					return $this->displayError("Affected ".$stmt->affected_rows." rows");
				}
			}
			else 
				//die("Error: ". $this->con->error);
				return $this->displayError($this->con->error);
			return $this->displayError($this->con->error);
		}
	}
	
	function getGroups($groupName) {
		
		// only for searching purpose
		if (isset($groupName)) {
			$query = "SELECT * FROM groups WHERE group_name LIKE ?";
			$param = "%{$groupName}%";
		}
		
		else {
			$query = "SELECT * FROM groups";
		}
		
		if ($stmt = $this->con->prepare($query)) {
			if (isset($groupName)) {
				$stmt->bind_param('s', $param);
			}
			$stmt->execute();
			$stmt->bind_result($groupID, $groupName, $groupUrl, $memberIDs, $adminIDs);
		}
		else {
			//die("Error: ". $this->con->error);
			return $this->displayError($this->con->error);
		}
		
		$groups = array();
		
		while ($stmt->fetch()) {
			$group = array();
			$group['group_id'] = $groupID;
			$group['group_name'] = $groupName;
			$group['group_url'] = $groupUrl;
			$group['member_ids'] = $memberIDs;
			$group['admin_ids'] = $adminIDs;
			
			array_push($groups, $group);
		}
		
		if (sizeOf($groups) == 0) {
			return $this->displayError("No results found");
		}
		
		return json_encode($groups);
	}
	
	// havent fully implemented in app yet
	function deleteGroup($groupID) {
		$query = "DELETE FROM groups WHERE group_id = ?";
		$stmt = $this->con->prepare($query);
		$stmt->bind_param("s", $groupID);
		if ($stmt->execute()) 
			return $this->displaySuccess("Success");
		return $this->displayError($this->con->error);
	}
	
	function updateGroup($groupUrl, $memberNames, $adminNames) {
		$memberIDs = $this->getAdminsAndMembers($memberNames);
		$adminIDs = $this->getAdminsAndMembers($adminNames);
		
		if ((!isset($memberIDs) && !isset($adminIDs)) || (preg_match('/\berror\b/', $memberIDs) || preg_match('/\berror\b/',$adminIDs))) 
			return $this->displayError("Unable to retrieve IDs corresponding to the members and admins provided");
		
		else {
			$query = "UPDATE groups SET member_ids = ?, admin_ids = ? WHERE group_url = ? ";
			$stmt = $this->con->prepare($query);
			$stmt->bind_param("sss", $memberIDs, $adminIDs, $groupUrl);
			$stmt->execute();
			if ($stmt->affected_rows === 1)
				return $this->displaySuccess("Success");
			else if ($stmt->affected_rows === 0 || $stmt->affected_rows > 1) {
				//die("Error: ". $this->con->error);
				return $this->displayError("Affected ".$stmt->affected_rows." rows");
			}
		}
		return $this->displayError($this->con->error);
	}
	
	function displayError($errMsg) {
		$responses = array();
		$response = array();
		$response['error'] = $errMsg;
		array_push($responses, $response);
		return json_encode($responses);
	}
	
	function displaySuccess($succMsg) {
		$responses = array();
		$response = array();
		$response['success'] = $succMsg;
		array_push($responses, $response);
		return json_encode($responses);
	}

}

$Group = new Group();
?>