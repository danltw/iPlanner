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
					if (isset($_POST['grpName']) && isset($_POST['memberIDs']) && isset($_POST['adminIDs'])) {
						$groupName = $_POST['grpName'];
						$memberIDs = $_POST['memberIDs'];
						$adminIDs = $_POST['adminIDs'];
						echo $this->addGroup($groupName, $memberIDs, $adminIDs);
					} 
					else 
						echo $this->displayError("grpName: ".$_POST['grpName'].", memberIDs: ".$_POST['memberIDs'].", adminIDs: ".$_POST['adminIDs']);
					break;
					
				case "updateGroup":
					if (isset($_POST['grpID']) && isset($_POST['grpName']) && isset($_POST['memberIDs']) && isset($_POST['adminIDs'])) {
						$groupID = $_POST['grpID'];
						$groupName = $_POST['grpName'];
						$memberIDs = $_POST['memberIDs'];
						$adminIDs = $_POST['adminIDs'];
						echo $this->updateGroup($groupID, $groupName, $memberIDs, $adminIDs);
					} 
					else 
						echo $this->displayError("grpID: ".$_POST['grpID']."grpName: ".$_POST['grpName'].", memberIDs: ".$_POST['memberIDs'].", adminIDs: ".$_POST['adminIDs']);
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
		}
		
		//echo $this->addGroup("Grouper", "2,5,6,7", "2");
		//echo $this->getGroups(null);
		//echo $this->getGroups();
		
	}
	
	function addGroup($groupName, $memberIDs, $adminIDs) {
		$query = "INSERT INTO id7469667_iplanner.group (group_name, member_ids, admin_ids) VALUES (?,?,?)";
		if ($stmt = $this->con->prepare($query)) {
		
			$stmt->bind_param('sss', $groupName, $memberIDs, $adminIDs);
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
		return $this->displayError($this->con->error);;
	}
	
	function getGroups($groupName) {
		
		if (isset($groupName)) {
			$query = "SELECT * FROM id7469667_iplanner.group WHERE group_name LIKE ?";
			$param = "%{$groupName}%";
		}
		else {
			$query = "SELECT * FROM id7469667_iplanner.group";
		}
		
		if ($stmt = $this->con->prepare($query)) {
			if (isset($groupName)) {
				$stmt->bind_param('s', $param);
			}
			$stmt->execute();
			$stmt->bind_result($groupID, $groupName, $memberIDs, $adminIDs);
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
			$group['member_ids'] = $memberIDs;
			$group['admin_ids'] = $adminIDs;
			
			array_push($groups, $group);
		}
		
		if (sizeOf($groups) == 0) {
			return $this->displayError("No results found");
		}
		
		return json_encode($groups);
	}
	
	function deleteGroup($groupID) {
		$query = "DELETE FROM id7469667_iplanner.group WHERE group_id = ?";
		$stmt = $this->con->prepare($query);
		$stmt->bind_param("s", $groupID);
		if ($stmt->execute()) 
			return $this->displaySuccess("Success");
		return $this->displayError($this->con->error);
	}
	
	function updateGroup($groupID, $groupName, $memberIDs, $adminIDs) {
		$query = "UPDATE id7469667_iplanner.group SET member_ids = ?, admin_ids = ? WHERE group_id = ? AND group_name = ?";
		$stmt = $this->con->prepare($query);
		$stmt->bind_param("ssis", $memberIDs, $adminIDs, $groupID, $groupName);
		$stmt->execute();
		if ($stmt->affected_rows === 1)
			return $this->displaySuccess("Success");
		else if ($stmt->affected_rows === 0 || $stmt->affected_rows > 1) {
			//die("Error: ". $this->con->error);
			return $this->displayError("Affected ".$stmt->affected_rows." rows");
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