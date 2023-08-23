<?php

include './database/config.php';

$response['error'] = TRUE;
$response['msg'] = "";

if ($_SERVER['REQUEST_METHOD'] == "POST" && (isset($_POST['city_name']) || isset($_POST['blood_insert']))) {

    if (isset($_POST['city_name'])) {
        $name = $_POST['city_name'];
        if ($db->insert('city_tbl', array('city_name' => $name))) {
            $response['insert'] = TRUE;
            $response['msg'] = "City Added Successfully.";
        } else {
            $response['insert'] = FALSE;
            $response['msg'] = "City Not Added Successfully.";
        }
    } else {
        $name = $_POST['name'];
        $email = $_POST['email'];
        $conatct = $_POST['conatct'];
        $address = $_POST['address'];
        $city_id = $_POST['city_id'];
        if (isset($_POST['blood_bank_id'])) {
            if ($db->updateMultiWhereCondtion('blood_bank_tbl', array('bb_name' => $name, 'bb_address' => $address, 'bb_email' => $email, 'bb_contact_no' => $conatct, 'city_id' => $city_id),array( 'bb_id'=>$_POST['blood_bank_id'] ))) {
                $response['insert'] = TRUE;
                $response['msg'] = "Blood Bank Added Successfully.";
            } else {
                $response['insert'] = FALSE;
                $response['msg'] = "Blood Bank Not Added Successfully.";
            }
        } else {
            if ($db->insert('blood_bank_tbl', array('bb_name' => $name, 'bb_address' => $address, 'bb_email' => $email, 'bb_contact_no' => $conatct, 'city_id' => $city_id))) {
                $response['insert'] = TRUE;
                $response['msg'] = "Blood Bank Added Successfully.";
            } else {
                $response['insert'] = FALSE;
                $response['msg'] = "Blood Bank Not Added Successfully.";
            }
        }
    }

    $response['error'] = FALSE;
} else {
    $response['msg'] = "Invalid Request";
    $response['error'] = true;
}


echo json_encode($response);
?>