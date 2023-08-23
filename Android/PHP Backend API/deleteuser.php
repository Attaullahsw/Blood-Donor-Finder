<?php

include './database/config.php';

$response['error'] = TRUE;
$response['msg'] = "";
$response['deletestatus'] = FALSE;

if ($_SERVER['REQUEST_METHOD'] == "POST" && isset($_POST['deleteuserid'])) {

    $id = $_POST['deleteuserid'];
    
    
    if($db->delete('user_tbl','user_id',$id)){
        $response['deletestatus'] = True;
    }  else {
        $response['deletestatus'] = FALSE;
    }
    
    $response['error'] = FALSE;
    
} else {
    $response['msg'] = "Invalid Request";
    $response['error'] = true;
}

echo json_encode($response);
?>