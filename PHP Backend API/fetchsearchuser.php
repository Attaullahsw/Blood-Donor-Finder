<?php

include './database/config.php';

$response['error'] = FALSE;
$response['msg'] = "";
$users = array();
$uniuser = array();



if ($_SERVER['REQUEST_METHOD'] == "POST" && isset($_POST['fetchRecord'])) {



    $q = NULL;
    $uniQ = NULL;
    if (isset($_POST['city_id']) && isset($_POST['blood_id'])) {
        $blood_id = $_POST['blood_id'];
        $city_id = $_POST['city_id'];
        if ($blood_id == 1) {
            $uniQ = "SELECT *,DATEDIFF(DATE(NOW()),user_age) as age FROM user_tbl WHERE city_id='$city_id' and (blood_id=7 OR blood_id=8 OR blood_id=2) and user_diseas_status=0 and DATEDIFF(DATE(NOW()),user_age) > 6570 AND (user_last_donation = 0 OR DATEDIFF(DATE(NOW()),user_last_donation)>90);";
        } else if ($blood_id == 2 || $blood_id == 4) {
            $uniQ = "SELECT *,DATEDIFF(DATE(NOW()),user_age) as age FROM user_tbl WHERE city_id='$city_id' and (blood_id=8) and user_diseas_status=0 and DATEDIFF(DATE(NOW()),user_age) > 6570 AND (user_last_donation = 0 OR DATEDIFF(DATE(NOW()),user_last_donation)>90);";
        } else if ($blood_id == 3) {
            $uniQ = "SELECT *,DATEDIFF(DATE(NOW()),user_age) as age FROM user_tbl WHERE city_id='$city_id' and (OR blood_id=7 OR blood_id=8 OR blood_id=4) and user_diseas_status=0 and DATEDIFF(DATE(NOW()),user_age) > 6570 AND (user_last_donation = 0 OR DATEDIFF(DATE(NOW()),user_last_donation)>90);";
        } else if ($blood_id == 5) {
            $uniQ = "SELECT *,DATEDIFF(DATE(NOW()),user_age) as age FROM user_tbl WHERE city_id='$city_id' and blood_id!='$blood_id' and user_diseas_status=0 and DATEDIFF(DATE(NOW()),user_age) > 6570 AND (user_last_donation = 0 OR DATEDIFF(DATE(NOW()),user_last_donation)>90);";
        } else if ($blood_id == 6) {
            $uniQ = "SELECT *,DATEDIFF(DATE(NOW()),user_age) as age FROM user_tbl WHERE city_id='$city_id' and (blood_id=8 OR blood_id=2 OR blood_id=5) and user_diseas_status=0 and DATEDIFF(DATE(NOW()),user_age) > 6570 AND (user_last_donation = 0 OR DATEDIFF(DATE(NOW()),user_last_donation)>90);";
        } else if ($blood_id == 7) {
            $uniQ = "SELECT *,DATEDIFF(DATE(NOW()),user_age) as age FROM user_tbl WHERE city_id='$city_id' and (blood_id=8) and user_diseas_status=0 and DATEDIFF(DATE(NOW()),user_age) > 6570 AND (user_last_donation = 0 OR DATEDIFF(DATE(NOW()),user_last_donation)>90);";
        }
        $q = "SELECT *,DATEDIFF(DATE(NOW()),user_age) as age FROM user_tbl WHERE city_id='$city_id' and blood_id='$blood_id' and user_diseas_status=0 and DATEDIFF(DATE(NOW()),user_age) > 6570 AND (user_last_donation = 0 OR DATEDIFF(DATE(NOW()),user_last_donation)>90);";
    } else if (isset($_POST['blood_id'])) {
        $blood_id = $_POST['blood_id'];
         if ($blood_id == 1) {
            $uniQ = "SELECT *,DATEDIFF(DATE(NOW()),user_age) as age FROM user_tbl WHERE  (blood_id=7 OR blood_id=8 OR blood_id=2) and user_diseas_status=0 and DATEDIFF(DATE(NOW()),user_age) > 6570 AND (user_last_donation = 0 OR DATEDIFF(DATE(NOW()),user_last_donation)>90);";
        } else if ($blood_id == 2 || $blood_id == 4) {
            $uniQ = "SELECT *,DATEDIFF(DATE(NOW()),user_age) as age FROM user_tbl WHERE  (blood_id=8) and user_diseas_status=0 and DATEDIFF(DATE(NOW()),user_age) > 6570 AND (user_last_donation = 0 OR DATEDIFF(DATE(NOW()),user_last_donation)>90);";
        } else if ($blood_id == 3) {
            $uniQ = "SELECT *,DATEDIFF(DATE(NOW()),user_age) as age FROM user_tbl WHERE  (blood_id=7 OR blood_id=8 OR blood_id=4) and user_diseas_status=0 and DATEDIFF(DATE(NOW()),user_age) > 6570 AND (user_last_donation = 0 OR DATEDIFF(DATE(NOW()),user_last_donation)>90);";
        } else if ($blood_id == 5) {
            $uniQ = "SELECT *,DATEDIFF(DATE(NOW()),user_age) as age FROM user_tbl WHERE  blood_id!='$blood_id' and user_diseas_status=0 and DATEDIFF(DATE(NOW()),user_age) > 6570 AND (user_last_donation = 0 OR DATEDIFF(DATE(NOW()),user_last_donation)>90);";
        } else if ($blood_id == 6) {
            $uniQ = "SELECT *,DATEDIFF(DATE(NOW()),user_age) as age FROM user_tbl WHERE  (blood_id=8 OR blood_id=2 OR blood_id=4) and user_diseas_status=0 and DATEDIFF(DATE(NOW()),user_age) > 6570 AND (user_last_donation = 0 OR DATEDIFF(DATE(NOW()),user_last_donation)>90);";
        } else if ($blood_id == 7) {
            $uniQ = "SELECT *,DATEDIFF(DATE(NOW()),user_age) as age FROM user_tbl WHERE  (blood_id=8) and user_diseas_status=0 and DATEDIFF(DATE(NOW()),user_age) > 6570 AND (user_last_donation = 0 OR DATEDIFF(DATE(NOW()),user_last_donation)>90);";
        }
        $q = "SELECT *,DATEDIFF(DATE(NOW()),user_age) as age FROM user_tbl WHERE blood_id='$blood_id' and user_diseas_status=0 and DATEDIFF(DATE(NOW()),user_age) > 6570 AND (user_last_donation = 0 OR DATEDIFF(DATE(NOW()),user_last_donation)>90);";
    } else if (isset($_POST['city_id'])) {
        $city_id = $_POST['city_id'];
        $q = "SELECT *,DATEDIFF(DATE(NOW()),user_age) as age FROM user_tbl WHERE city_id='$city_id' and  user_diseas_status=0 and DATEDIFF(DATE(NOW()),user_age) > 6570 AND (user_last_donation = 0 OR DATEDIFF(DATE(NOW()),user_last_donation)>90)";
    } else if(isset ($_POST['fetchalluser'])){
        $q = "SELECT *,DATEDIFF(DATE(NOW()),user_age) as age FROM user_tbl order by user_name ASC";
    }

    $fetchuser = $db->custom_query($q);

    foreach ($fetchuser as $key) {

        $id = $key->user_id;
        $name = $key->user_name;
        $city = $key->city_id;
        $fetchcity = $db->fetch_multi_row('city_tbl', array("*"), array('city_id' => $city));
        $Address = "";
        foreach ($fetchcity as $c) {
            $Address = $c->city_name;
        }

        $fetchblood = $db->fetch_multi_row('blood_group_tbl', array("*"), array('blood_group_id' => $key->blood_id));
        $blood_group = "";
        foreach ($fetchblood as $b) {
            $blood_group = $b->blood_group;
        }
        $image = $key->user_image;
        $age = intval(($key->age) / 365);
        $contact_no = $key->user_contact_no;
        $email = $key->user_email;
        $users[] = array(
            'user_id' => $id,
            'user_name' => $name,
            'city_id' => $Address,
            'blood_id' => $blood_group,
            'user_image' => $image,
            'user_age' => $age,
            'user_contact_no' => $contact_no,
            'user_email' => $email
        );

        // $users[]=$key;
    }

    $response['searchrecord'] = $users;

    if ($uniQ != null) {
        $fetchuser = $db->custom_query($uniQ);

        foreach ($fetchuser as $key) {

            $id = $key->user_id;
            $name = $key->user_name;
            $city = $key->city_id;
            $fetchcity = $db->fetch_multi_row('city_tbl', array("*"), array('city_id' => $city));
            $Address = "";
            foreach ($fetchcity as $c) {
                $Address = $c->city_name;
            }

            $fetchblood = $db->fetch_multi_row('blood_group_tbl', array("*"), array('blood_group_id' => $key->blood_id));
            $blood_group = "";
            foreach ($fetchblood as $b) {
                $blood_group = $b->blood_group;
            }
            $image = $key->user_image;
            $age = intval(($key->age) / 365);
            $contact_no = $key->user_contact_no;
            $email = $key->user_email;
            $uniuser[] = array(
                'user_id' => $id,
                'user_name' => $name,
                'city_id' => $Address,
                'blood_id' => $blood_group,
                'user_image' => $image,
                'user_age' => $age,
                'user_contact_no' => $contact_no,
                'user_email' => $email
            );
        }
    }
    $response['uniuser'] = $uniuser;



//    $data = NULL;
//    if(isset($_POST['city_id']) && isset($_POST['blood_id'])){
//        $data = array(
//            'city_id'=>$_POST['city_id'],
//            'blood_id'=>$_POST['blood_id']
//        );
//    }else if(isset ($_POST['blood_id'])){
//        $data = array(
//            'blood_id'=>$_POST['blood_id']
//        );
//    }else if(isset ($_POST['city_id'])){
//        $data = array(
//            'city_id'=>$_POST['city_id']
//        );
//    }

    $response['error'] = FALSE;
} else {
    $response['msg'] = "Invalid Request";
    $response['error'] = true;
}

echo json_encode($response);
?>