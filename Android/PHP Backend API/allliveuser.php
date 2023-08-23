<?php

include './database/config.php';

$response['error'] = TRUE;
$response['msg'] = "";




if ($_SERVER['REQUEST_METHOD'] == "POST" && isset($_POST['livelocation'])) {

    
    $lat = $_POST['lat'];
    $lon = $_POST['lon'];
    $blood_id = $_POST['blood_id'];
	

$query = "SELECT 
                  *,(6371 * ACOS( COS( RADIANS($lat) ) * COS( RADIANS(`location_lat`) ) *
                        COS( RADIANS(`location_lon`) - RADIANS($lon)) +
                        SIN(RADIANS($lat)) * SIN(RADIANS(`location_lat`)) )
                  ) AS distance
    FROM  location_tbl
	where blood_id='$blood_id'
    HAVING distance < 500
    LIMIT  25;";

$detail = $db->custom_query($query);
foreach($detail as $key){
	$user_id = $key->user_id;
	$blood_id = $key->blood_id;
	$location_lat = $key->location_lat;
	$location_lon = $key->location_lon;
	$date = $key->date;
	$time = $key->time;
	
	
//	$q = "SELECT *,DATEDIFF(DATE(NOW()),user_age) as age FROM user_tbl WHERE user_id='$user_id' and blood_id='$blood_id' and user_diseas_status=0 and DATEDIFF(DATE(NOW()),user_age) > 6570 AND (user_last_donation = 0 OR DATEDIFF(DATE(NOW()),user_last_donation)>90);";
	    $fetch = $db->fetch_multi_row('user_tbl', array("*,DATEDIFF(DATE(NOW()),user_age) as age,DATEDIFF(DATE(NOW()),user_last_donation) as donation"), array('user_id' => $user_id));
//	    $fetch = $db->custom_query($q);
    
    foreach ($fetch as $key2) {

        $id = $key2->user_id;
        $name = $key2->user_name;
        $city = $key2->city_id;
        $fetchcity = $db->fetch_multi_row('city_tbl', array("*"), array('city_id' => $city));
        $Address = "";
        foreach ($fetchcity as $c) {
            $Address = $c->city_name;
        }

        $fetchblood = $db->fetch_multi_row('blood_group_tbl', array("*"), array('blood_group_id' => $key->blood_id));
        $blood_group = "";
        foreach ($fetchblood as $b) {
            $blood_group = $b->blood_group;
        }
        $image = $key2->user_image;
        $age = intval(($key2->age) / 365);
        $contact_no = $key2->user_contact_no;
        $email = $key2->user_email;
        
        $donation = 0;
        if($key2->donation != null){
            $donation = $key2->donation;
        }
        $users[] = array(
            'user_id' => $id,
            'user_pass' => $key2->user_password,
            'user_name' => $name,
            'city_id' => $Address,
            'blood_id' => $blood_group,
            'user_image' => $image,
            'user_age' => $age,
            'date_of_birth' => $key2->user_age,
            'user_contact_no' => $contact_no,
            'user_email' => $email,
            'user_last_donation' => $key2->user_last_donation,
            'user_diseas_status' => $key2->user_diseas_status,
            'user_diseas' => $key2->user_diseas,
            'user_no_donation' => $key2->user_no_donation,
            'user_gender' => $key2->user_gender,
            'user_donation_notification' => $key2->user_donation_notification,
            'donation_age' => $donation,
            'location_lat' => $location_lat,
            'location_lon' => $location_lon,
            'location_date' => $date,
            'location_time' => $time
			
        );

        // $users[]=$key;
    }
	
	
    $response['fetchuser'] = $users;

	
	
	

}
    
    
    
   
    
    

    

   

    
    $response['error'] = FALSE;
} else {
    $response['msg'] = "Invalid Request";
    $response['error'] = true;
}


echo json_encode($response);


?>