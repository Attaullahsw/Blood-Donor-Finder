<?php

include 'database/config.php';

$res['msg'] = "";
$res['error'] = true;
if ($_SERVER['REQUEST_METHOD'] == "POST" && isset($_POST['name']) || isset($_POST['feedback_delete_id']) || isset($_POST['feedback_update_id'])) {

    if (isset($_POST['feedback_update_id'])) {
        
         if ($db->updateMultiWhereCondtion('feedback_tbl', array('feedback_status' => '1'), array('feedback_id' => $_POST['feedback_update_id']))) {
            $res['msg'] = "Mark Read successfully";
            $res['error'] = false;
        } else {
            $res['msg'] = "error occur";
            $res['error'] = true;
        }
        
    } else if (isset($_POST['feedback_delete_id'])) {
        if ($db->delete('feedback_tbl', 'feedback_id', $_POST['feedback_delete_id'])) {
            $res['msg'] = "Feed Back Deleted successfully";
            $res['error'] = false;
        } else {
            $res['msg'] = "error occur";
            $res['error'] = true;
        }
    } else {
        $name = $_POST['name'];
        $email = $_POST['email'];
        $msg = $_POST['msg'];


        if ($db->insert('feedback_tbl', array('feedback_name' => $name, 'feedback_email' => $email, 'feedback_msg' => $msg, 'feedback_status' => 0))) {
            $res['msg'] = "Feed Back Sended successfully";
            $res['error'] = false;
        } else {
            $res['msg'] = "error occur";
            $res['error'] = true;
        }
    }
} else {
    $res['msg'] = "Invalid Request";
    $res['error'] = true;
}




echo json_encode($res);
?>