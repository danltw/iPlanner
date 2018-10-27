<?php

     if(isset($_POST['searchQuery']))
     {
     	  // include db connect class
        require_once(__DIR__.'/include/DB_Connect.php');
	  $search_query=$_POST['searchQuery'];
          $sql = 'SELECT * from id7469667_iplanner.poi where MATCH(location_name,location_address,location_postalcode) AGAINST(:search_query)';
          $statement = $connection->prepare($sql);
	  $statement->bindParam(':search_query', $search_query, PDO::PARAM_STR);
          $statement->execute();
          if($statement->rowCount())
          {
	    $row_all = $statement->fetchall(PDO::FETCH_ASSOC);
	    header('Content-type: application/json');
   	    echo json_encode($row_all);
          }  
          elseif(!$statement->rowCount())
          {
	    echo "no rows";
          }
     }
		  
?>