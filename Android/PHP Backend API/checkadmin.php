<?php

include './database/config.php';

$response['error'] = TRUE;
$response['msg'] = "";

if ($_SERVER['REQUEST_METHOD'] == "POST") {

    $username = $_POST['username'];
    $pass = $_POST['pass'];
    $data = array(
        'admin_user_name' => $username,
        'admin_password' => $pass
    );

    $s = $db->check_exist('admin_tbl', $data);
    if ($s == true) {
            $response['adminerror'] = TRUE;
    } else {
        $response['adminerror'] = FALSE;
    }
    $response['error'] = FALSE;
    
} else {
    $response['msg'] = "Invalid Request";
    $response['error'] = true;
}

echo json_encode($response);
?>