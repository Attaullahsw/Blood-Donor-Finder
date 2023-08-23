<?php

include './database/config.php';

$response['error'] = FALSE;
$response['msg'] = "";
$users = array();




if ($_SERVER['REQUEST_METHOD'] == "POST") {



    if (isset($_POST['blood_city_id'])) {
        $blood_city_id = $_POST['blood_city_id'];
        $fetchbloodbank = $db->fetch_multi_row('blood_bank_tbl', array("*"), array('city_id' => $blood_city_id));
    } else {
        $fetchbloodbank = $db->fetch_all('blood_bank_tbl');
    }

    foreach ($fetchbloodbank as $key) {

        $id = $key->bb_id;
        $name = $key->bb_name;
        $city = $key->city_id;
        $address = $key->bb_address;
        $contact_no = $key->bb_contact_no;
        $email = $key->bb_email;
        $users[] = array(
            'bb_id' => $id,
            'bb_name' => $name,
            'city_id' => $city,
            'bb_address' => $address,
            'bb_contact_no' => $contact_no,
            'bb_email' => $email
        );

        // $users[]=$key;
    }

    $response['searchrecord'] = $users;



    $response['error'] = FALSE;
} else {
    $response['msg'] = "Invalid Request";
    $response['error'] = true;
}

echo json_encode($response);
?>