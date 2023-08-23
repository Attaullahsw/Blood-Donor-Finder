<?php

include './database/config.php';

$response['error'] = FALSE;
$response['msg'] = "";
$users = array();




if ($_SERVER['REQUEST_METHOD'] == "POST" && isset($_POST['user_name']) || isset($_POST['user_id'])) {




    $fetch;
    if (isset($_POST['user_id'])) {
        $user_id = $_POST['user_id'];
        $fetch = $db->fetch_multi_row('user_tbl', array("*,DATEDIFF(DATE(NOW()),user_age) as age,DATEDIFF(DATE(NOW()),user_last_donation) as donation"), array('user_id' => $user_id));
    } else {
        $fetch = $db->fetch_multi_row('user_tbl', array("*,DATEDIFF(DATE(NOW()),user_age) as age,DATEDIFF(DATE(NOW()),user_last_donation) as donation"), array('user_email' => $_POST['user_name'], 'user_password' => $_POST['user_pass']));
    }


    foreach ($fetch as $key) {

        $id = $key->user_id;
        $name = $key->user_name;
        $city = $key->city_id;
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
        $image = $key->user_image;
        $age = intval(($key->age) / 365);
        $contact_no = $key->user_contact_no;
        $email = $key->user_email;
        
        $donation = 0;
        if($key->donation != null){
            $donation = $key->donation;
        }
        $users[] = array(
            'user_id' => $id,
            'user_pass' => $key->user_password,
            'user_name' => $name,
            'city_id' => $Address,
            'blood_id' => $blood_group,
            'user_image' => $image,
            'user_age' => $age,
            'date_of_birth' => $key->user_age,
            'user_contact_no' => $contact_no,
            'user_email' => $email,
            'user_last_donation' => $key->user_last_donation,
            'user_diseas_status' => $key->user_diseas_status,
            'user_diseas' => $key->user_diseas,
            'user_no_donation' => $key->user_no_donation,
            'user_gender' => $key->user_gender,
            'user_donation_notification' => $key->user_donation_notification,
            'donation_age' => $donation
        );

        // $users[]=$key;
    }

    $response['fetchuser'] = $users;




    $response['error'] = FALSE;
} else {
    $response['msg'] = "Invalid Request";
    $response['error'] = true;
}

echo json_encode($response);
?>