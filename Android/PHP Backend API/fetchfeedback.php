<?php

include './database/config.php';

$response['error'] = FALSE;
$response['msg'] = "";
$blood = array();
$city = array();

if ($_SERVER['REQUEST_METHOD'] == "POST") {

    $bloodResult = $db->fetch_all_order('feedback_tbl','feedback_id');

    foreach ($bloodResult as $key) {
        $blood[] = $key;
    }
    $response['feedback_tbl'] = $blood;
    $response['error'] = FALSE;
    
    
} else {
    $response['msg'] = "Invalid Request";
    $response['error'] = true;
}

echo json_encode($response);

?>