package com.example.bloodbank.activities.adminactivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bloodbank.R;
import com.example.bloodbank.activities.UserProfileActivity;
import com.example.bloodbank.util.InternetWarningDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AdminLoginActivity extends AppCompatActivity {

    Button btn_login;
    EditText edt_user_name, edt_password;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);


        btn_login = (Button) findViewById(R.id.btn_adminlogin);
        edt_user_name = (EditText) findViewById(R.id.edt_adminuserName);
        edt_password = (EditText) findViewById(R.id.edt_adminpassword);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = edt_user_name.getText().toString();
                String pass = edt_password.getText().toString();

                if (username.equals("")) {
                    Toast.makeText(AdminLoginActivity.this, "user name field is empty!", Toast.LENGTH_SHORT).show();
                    edt_user_name.requestFocus();

                } else if (pass.equals("")) {
                    Toast.makeText(AdminLoginActivity.this, "Password field is empty!", Toast.LENGTH_SHORT).show();
                    edt_password.requestFocus();
                } else {
                    getData(username,pass);

                }

            }
        });

    }


    public void getData(final String username, final String password) {

        String strings = getResources().getString(R.string.url) + "checkadmin.php";
        StringRequest request = new StringRequest(Request.Method.POST, strings, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    if (error) {
                        progressDialog.dismiss();
                        Toast.makeText(AdminLoginActivity.this, jsonObject.getString("msg").toString(), Toast.LENGTH_LONG).show();
                    } else {
                        progressDialog.dismiss();
                        boolean check = jsonObject.getBoolean("adminerror");
                        if (check) {
                            Intent intent = new Intent(AdminLoginActivity.this,AdminHomeActivity.class);
                            finish();
                            startActivity(intent);
                        } else {
                            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(AdminLoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Invalid Admin Credential!")
                                    .setContentText("You Email or Password Is Incorrect!");
                            sweetAlertDialog.show();
                        }
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
               // Toast.makeText(AdminLoginActivity.this, "Connection Problem", Toast.LENGTH_LONG).show();
                InternetWarningDialog.showCustomDialog(AdminLoginActivity.this);
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

        RequestQueue requestQueue = Volley.newRequestQueue(AdminLoginActivity.this);
        requestQueue.add(request);
        progressDialog = new ProgressDialog(AdminLoginActivity.this);
        progressDialog.setTitle("Loading!");
        progressDialog.setMessage("Please wait a while...");
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

}
