<?php

include './database/config.php';

$response['error'] = TRUE;
$response['msg'] = "";

if ($_SERVER['REQUEST_METHOD'] == "POST" && (isset($_POST['city_delete_id']) || isset($_POST['city_update_id']))) {




    if (isset($_POST['city_delete_id'])) {

        $city_delete_id = $_POST['city_delete_id'];
        $s = $db->delete('city_tbl', 'city_id', $city_delete_id);
        if ($s == true) {
            $response['deletecity'] = TRUE;
        } else {
            $response['deletecity'] = FALSE;
        }
    } else {
        $city_update_id = $_POST['city_update_id'];
        $city_update_name = $_POST['city_update_name'];
        $s = $db->updateMultiWhereCondtion('city_tbl', array('city_name' => $city_update_name), array('city_id' => $city_update_id));
        if ($s == true) {
            $response['deletecity'] = TRUE;
        } else {
            $response['deletecity'] = FALSE;
        }
    }


    $response['error'] = FALSE;
} else {
    $response['msg'] = "Invalid Request";
    $response['error'] = true;
}

echo json_encode($response);
?>