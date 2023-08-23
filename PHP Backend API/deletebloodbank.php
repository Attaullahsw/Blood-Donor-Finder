<?php

include './database/config.php';

$response['error'] = TRUE;
$response['msg'] = "";

if ($_SERVER['REQUEST_METHOD'] == "POST" && (isset($_POST['blood_bank_id']))) {




       $blood_bank_id = $_POST['blood_bank_id'];
        $s = $db->delete('blood_bank_tbl', 'bb_id', $blood_bank_id);
        if ($s == true) {
            $response['deletebloodbank'] = TRUE;
        } else {
            $response['deletebloodbank'] = FALSE;
        }
    


    $response['error'] = FALSE;
} else {
    $response['msg'] = "Invalid Request";
    $response['error'] = true;
}

echo json_encode($response);
?>