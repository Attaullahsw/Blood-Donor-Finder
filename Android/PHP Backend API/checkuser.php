<?php

include './database/config.php';

$response['error'] = TRUE;
$response['msg'] = "";

if ($_SERVER['REQUEST_METHOD'] == "POST") {

    $username = $_POST['username'];
    $pass = $_POST['pass'];
    $data = array(
        'user_email' => $username,
        'user_password' => $pass
    );

    $s = $db->check_exist('user_tbl', $data);
    if ($s == true) {
            $response['usererror'] = TRUE;
    } else {
        $response['usererror'] = FALSE;
    }
    $response['error'] = FALSE;
    
} else {
    $response['msg'] = "Invalid Request";
    $response['error'] = true;
}

echo json_encode($response);
?>