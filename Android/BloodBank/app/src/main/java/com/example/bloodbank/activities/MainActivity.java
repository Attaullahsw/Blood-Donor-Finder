package com.example.bloodbank.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bloodbank.R;
import com.example.bloodbank.activities.adminactivity.AdminLoginActivity;
import com.example.bloodbank.util.InternetWarningDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {


    Button btn_login;
    EditText edt_user_name, edt_password;
    CheckBox chb_user_remember;
    ProgressDialog progressDialog;

    SharedPreferences logPre;
    SharedPreferences.Editor preEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logPre = getSharedPreferences("UserLogin", MODE_PRIVATE);
        preEditor = logPre.edit();
        rememberLogin();


        btn_login = (Button) findViewById(R.id.btn_login);
        edt_user_name = (EditText) findViewById(R.id.edt_user_name);
        edt_password = (EditText) findViewById(R.id.edt_password);
        chb_user_remember = (CheckBox) findViewById(R.id.chb_user_remember);



        preEditor.remove("check_login");
        preEditor.remove("temp_user");
        preEditor.remove("temp_pass");
        preEditor.apply();
        preEditor.commit();


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edt_user_name.getText().toString();
                String pass = edt_password.getText().toString();

                if (username.equals("")) {
                    Toast.makeText(MainActivity.this, "user name field is empty!", Toast.LENGTH_SHORT).show();
                    edt_user_name.requestFocus();

                } else if (pass.equals("")) {
                    Toast.makeText(MainActivity.this, "Password field is empty!", Toast.LENGTH_SHORT).show();
                    edt_password.requestFocus();
                } else {
                    getData(username, pass);

                }


            }
        });


    }

    public void skipNow(View view) {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
    }

//    public void goToAdmin(View view) {
//        Intent intent = new Intent(MainActivity.this, AdminLoginActivity.class);
//        startActivity(intent);
//    }

    public void forgotPassword(View view) {

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.forgot_password_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editText = (EditText) dialogView.findViewById(R.id.edt_forgot_password);
        Button btn = (Button) dialogView.findViewById(R.id.btn_forgot_send);
        TextView txtCancel = (TextView) dialogView.findViewById(R.id.btn_forgot_cancel);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editText.getText().toString();
                if (email.equals("") || !email.contains("@")) {
                    Toast.makeText(MainActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                } else {
                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Password Send To:")
                            .setContentText(email)
                            .show();
                }
            }
        });
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

    }

    public void signUp(View view) {

        Intent intent = new Intent(MainActivity.this, RegisterUserActivity.class);
        startActivity(intent);

    }

    public void getData(final String username, final String password) {
 
        String strings = getResources().getString(R.string.url) + "checkuser.php";
        StringRequest request = new StringRequest(Request.Method.POST, strings, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    if (error) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, jsonObject.getString("msg").toString(), Toast.LENGTH_LONG).show();
                    } else {

                        boolean check = jsonObject.getBoolean("usererror");
                        if (check) {

                            preEditor.putBoolean("check_login", true);
                            if (chb_user_remember.isChecked()) {
                                preEditor.putString("user", username);
                                preEditor.putString("pass", password);
                            }

                            preEditor.putString("temp_user", username);
                            preEditor.putString("temp_pass", password);

                            preEditor.apply();
                            preEditor.commit();
                            finish();
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                        } else {

                            preEditor.remove("user");
                            preEditor.remove("pass");
                            preEditor.commit();
                            preEditor.apply();
                            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Invalid Credential!")
                                    .setContentText("You Email or Password Is Incorrect!");
                            sweetAlertDialog.show();
                        }
                    }

                    progressDialog.dismiss();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
              //  Toast.makeText(MainActivity.this, "Connection Problem", Toast.LENGTH_LONG).show();
                InternetWarningDialog.showCustomDialog(MainActivity.this);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("username", username);
                params.put("pass", password);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(request);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Loading!");
        progressDialog.setMessage("Please wait a while...");
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    // Remeber user and skip get login data
    public void rememberLogin() {
        String tempUser = logPre.getString("user", "");
        String tempPass = logPre.getString("pass", "");
        if (!tempUser.isEmpty() && !tempPass.isEmpty()) {
            getData(tempUser, tempPass);
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }
}
