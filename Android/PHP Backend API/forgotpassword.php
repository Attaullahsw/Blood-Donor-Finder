<?php

include './database/config.php';

$response['error'] = TRUE;
$response['msg'] = "";

if ($_SERVER['REQUEST_METHOD'] == "POST" && isset($_POST['User_email'])) {

    $User_email = $_POST['User_email'];
    $data = array(
        'user_email' => $username
    );
    $fetch = $db->fetch_multi_row('user_tbl',array("user_password"),$data);
    $password = null;
    foreach($fetch as $key){
        $password = $key->user_password;
    }
    
    if($password!=null){
        $response['error'] = FALSE;
        $response['msg'] = "done";
    }
    
} else {
    $response['msg'] = "Invalid Request";
    $response['error'] = true;
}

echo json_encode($response);
?>