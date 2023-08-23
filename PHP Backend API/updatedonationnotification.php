<?php

include './database/config.php';

$response['error'] = FALSE;
$response['msg'] = "";
$users = array();




if ($_SERVER['REQUEST_METHOD'] == "POST" && isset($_POST['user_name'])) {




    if ($db->updateMultiWhereCondtion('user_tbl', array('user_donation_notification'=>1),
            array('user_email' => $_POST['user_name'],'user_password'=>$_POST['user_pass']))) {
        $response['update'] = TRUE;
        $response['msg'] = "User notification updated Successfully.";
    } else {
        $response['update'] = FALSE;
        $response['msg'] = "User notification not updated Successfully.";
    }


    $response['error'] = FALSE;
} else {
    $response['msg'] = "Invalid Request";
    $response['error'] = true;
}

echo json_encode($response);
?>