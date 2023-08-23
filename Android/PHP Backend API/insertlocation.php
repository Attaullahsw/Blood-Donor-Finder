<?php

include './database/config.php';

$response['error'] = TRUE;
$response['msg'] = "";

if ($_SERVER['REQUEST_METHOD'] == "POST" && isset($_POST['insertlocation'])) {

    $user_name = $_POST['user_name'];
    $user_pass = $_POST['user_pass'];
    $lat = $_POST['lat'];
    $lon = $_POST['lon'];
    
    
    
    $user = $db->fetch_multi_row("user_tbl",array("*"),array('user_email'=>$user_name,'user_password'=>$user_pass));
    
	
	date_default_timezone_set("Asia/Karachi");
	foreach ($user as $key) {
		$user_id = $key->user_id;
		$blood_id = $key->blood_id;
		
		$s = $db->check_exist('location_tbl', array('user_id'=> $user_id));
    if ($s == true) {
          
		  if($db->update("location_tbl",array(
        'blood_id'=> $blood_id,
        'location_lat'=> $lat,
        'location_lon'=> $lon,
        'date'=> date("d-m-Y"),
        'time'=> date("h:i:s")
        
        
    ), 'user_id',$user_id)){
      $response["inserterror"] = FALSE; 
  }else{
      $response["inserterror"] = TRUE; 
  }
    } else {
        
		if($db->insert("location_tbl",array(
        'user_id'=> $user_id,
        'blood_id'=> $blood_id,
        'location_lat'=> $lat,
        'location_lon'=> $lon,
        'date'=> date("d-m-Y"),
        'time'=> date("h:i:s")
        
        
    ))){
      $response["inserterror"] = FALSE; 
  }else{
      $response["inserterror"] = TRUE; 
  }
    }
		
		
	}
    
    

    

   

    
    $response['error'] = FALSE;
} else {
    $response['msg'] = "Invalid Request";
    $response['error'] = true;
}


echo json_encode($response);


?>