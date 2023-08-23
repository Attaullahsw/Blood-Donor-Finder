<?php

include './database/config.php';

$response['error'] = TRUE;
$response['msg'] = "";


if ($_SERVER['REQUEST_METHOD'] == "POST" && isset($_POST['addnewuser'])) {

    $name = $_POST['name'];
    $email = $_POST['email'];
    $password = $_POST['password'];
    $date_birth = $_POST['date_birth'];
    $contact_no = $_POST['contact_no'];
    $blood_group = $_POST['blood_group'];
    $city = $_POST['city'];
    $Gender = $_POST['Gender'];
    $diseas_status = $_POST['diseas_status'];
    $imagename = $_POST['imagename'];
    $imagenameupdateold = $_POST['imagename'];
    $img = $_POST['image'];
    $imagename = explode(".", $imagename);
    $extension = $imagename[sizeof($imagename) - 1];
    $imagename = $imagename[0] . rand() . "." . $extension;

    $data = array(
        'user_name' => $name,
        'city_id' => $city,
        'user_email' => $email,
        'user_password' => $password,
        'user_contact_no' => $contact_no,
        'user_age' => $date_birth,
        'user_gender' => $Gender,
        'blood_id' => $blood_group,
        'user_image' => $imagename,
        'user_diseas_status' => $diseas_status,
        'user_diseas' => $_POST['diseas']
    );

    if (strcmp($img, "") == 0) {
        unset($data['user_image']);
    }



    if (isset($_POST['confirm_pass'])) {
        $user_id = $_POST['user_id'];
        $confirmpassword = $_POST['confirm_pass'];

        if (!$db->check_exist('user_tbl', array('user_id' => $user_id, 'user_password' => $confirmpassword))) {
            $response['confirm_password_error'] = TRUE;
            $response['confirm_password_msg'] = "Password is Incorrect!";
        } else {
            $response['confirm_password_error'] = FALSE;

            $c = $db->custom_query("select count(*) as c from user_tbl where user_id != '$user_id' and user_email='$email'");
            foreach ($c as $k) {
                if ($k->c > 0) {
                    $response['checkerror'] = TRUE;
                    $response['msg'] = "User Already Exists.";
                } else {
                    $response['checkerror'] = FALSE;


                    $folderpath = "images/";

                    if ((strcmp($img, "") != 0)) {
                        if (file_exists($folderpath.$imagenameupdateold)) {
                            unlink($folderpath.$imagenameupdateold);
                        }

                        $decodedImage = base64_decode("$img");


                        $return = file_put_contents("images/" . $imagename, $decodedImage);
                    }

                    if ($db->updateMultiWhereCondtion('user_tbl', $data, array('user_id' => $user_id))) {
                        $response['insert'] = TRUE;
                        $response['msg'] = "User Updated Successfully.";
                    } else {
                        $response['insert'] = FALSE;
                        $response['msg'] = "User Not Updated Successfully.";
                    }
                }
            }
        }
    } else {
        $datacheck = array(
            'user_email' => $email
        );

        $s = $db->check_exist('user_tbl', $datacheck);
        if ($s == true) {
            $response['checkerror'] = TRUE;
            $response['msg'] = "User Already Exists.";
        } else {

            $response['checkerror'] = FALSE;


            $folderpath = "images/";

            if ((strcmp($img, "") != 0)) {

                $decodedImage = base64_decode("$img");


                $return = file_put_contents("images/" . $imagename, $decodedImage);
            }

            if ($db->insert('user_tbl', $data)) {
                $response['insert'] = TRUE;
                $response['msg'] = "User Added Successfully.";
            } else {
                $response['insert'] = FALSE;
                $response['msg'] = "User Not Added Successfully.";
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