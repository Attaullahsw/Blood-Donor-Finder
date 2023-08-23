<?php

include './database/config.php';

$response['error'] = FALSE;
$response['msg'] = "";
$blood = array();
$city = array();

if ($_SERVER['REQUEST_METHOD'] == "POST") {

    $bloodResult = $db->fetch_all('blood_group_tbl');

    foreach ($bloodResult as $key) {
        $blood[] = $key;
    }
    $response['blood_group'] = $blood;

    $fetchcity = $db->fetch_all_order('city_tbl','city_name');

    foreach ($fetchcity as $key) {
        $city[] = $key;
    }
    $response['city'] = $city;
    $response['error'] = FALSE;
    
    
} else {
    $response['msg'] = "Invalid Request";
    $response['error'] = true;
}

echo json_encode($response);

?>