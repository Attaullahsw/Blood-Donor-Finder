<?php

include './database/config.php';

$response['error'] = FALSE;
$response['msg'] = "";
$users = array();




if ($_SERVER['REQUEST_METHOD'] == "POST" && isset($_POST['last_donation_date'])) {




    $date = $_POST['last_donation_date'];
    $bleed = 1;
    $fetchcountdonation = $db->fetch_multi_row('user_tbl', array("user_no_donation"), 
            array('user_email' => $_POST['user_name'],'user_password'=>$_POST['user_pass']));
    foreach ($fetchcountdonation as $k){
        $bleed = $k->user_no_donation + 1;
    }
    if ($db->updateMultiWhereCondtion('user_tbl', array('user_last_donation'=>$date,'user_no_donation'=>$bleed,'user_donation_notification'=>0),
            array('user_email' => $_POST['user_name'],'user_password'=>$_POST['user_pass']))) {
        $response['update'] = TRUE;
        $response['msg'] = "User Updated Successfully.";
    } else {
        $response['update'] = FALSE;
        $response['msg'] = "User Not Updated Successfully.";
    }


    $response['error'] = FALSE;
} else {
    $response['msg'] = "Invalid Request";
    $response['error'] = true;
}

echo json_encode($response);
?>