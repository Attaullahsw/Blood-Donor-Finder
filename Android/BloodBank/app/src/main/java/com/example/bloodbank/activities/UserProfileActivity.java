package com.example.bloodbank.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.example.bloodbank.activities.adminactivity.AdminAddBloodBankActivity;
import com.example.bloodbank.util.InternetWarningDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class UserProfileActivity extends AppCompatActivity {

    SharedPreferences logPre;
    SharedPreferences.Editor preEditor;

    TextView txt_profile_name, txt_no_of_bleed, txt_last_date, txt_profile_email, txt_profile_contact_no, txt_profile_address, txt_profile_gender,
            txt_profile_age, txt_profile_diseas, txt_profile_blood_group;

    ImageView img_profile, img_profile_background;
    ImageButton btn_edit;
    String tempUser;
    String tempPass;

    Calendar myCalendar;

    String last_donation_date;

    BottomNavigationView navigation;

    ProgressDialog progressDialog;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent2 = new Intent(UserProfileActivity.this, HomeActivity.class);
                    finish();
                    startActivity(intent2);

                    return true;
                case R.id.navigation_registeration:



                    return true;
                case R.id.navigation_add_blood_bank:
                    Intent intent = new Intent(UserProfileActivity.this, AddBloodBankActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        logPre = getSharedPreferences("UserLogin", MODE_PRIVATE);
        preEditor = logPre.edit();

        tempUser = logPre.getString("temp_user", "");
        tempPass = logPre.getString("temp_pass", "");

        if (tempUser.equals("") || tempPass.equals("")) {
            Toast.makeText(this, "Some thing went wrong!", Toast.LENGTH_SHORT).show();
            finish();
        }

        txt_profile_name = (TextView) findViewById(R.id.txt_profile_name);
        txt_no_of_bleed = (TextView) findViewById(R.id.txt_no_of_bleed);
        txt_last_date = (TextView) findViewById(R.id.txt_last_date);
        txt_profile_email = (TextView) findViewById(R.id.txt_profile_email);
        txt_profile_contact_no = (TextView) findViewById(R.id.txt_profile_contact_no);
        txt_profile_address = (TextView) findViewById(R.id.txt_profile_address);
        txt_profile_gender = (TextView) findViewById(R.id.txt_profile_gender);
        txt_profile_age = (TextView) findViewById(R.id.txt_profile_age);
        txt_profile_diseas = (TextView) findViewById(R.id.txt_profile_diseas);
        txt_profile_blood_group = (TextView) findViewById(R.id.txt_profile_blood_group);

        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateBleed();
            }

        };



        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



        if (logPre.getBoolean("check_login", false)) {
            navigation.getMenu().findItem(R.id.navigation_registeration).setTitle("Profile");
        }

        txt_no_of_bleed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(UserProfileActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        //  getData(tempUser,tempPass);

    }

    public void getData(final String user, final String pass, final boolean updatebleed, String url) {

        String strings = getResources().getString(R.string.url) + url;
        StringRequest request = new StringRequest(Request.Method.POST, strings, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    if (error) {
                        progressDialog.dismiss();
                        Toast.makeText(UserProfileActivity.this, jsonObject.getString("msg").toString(), Toast.LENGTH_LONG).show();
                    } else {


                        if (updatebleed) {
                            if (jsonObject.getBoolean("update")) {
                                new SweetAlertDialog(UserProfileActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Your donation is updated!")
                                        .setConfirmText("ok")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();

                                                finish();
                                                startActivity(getIntent());
                                            }
                                        })
                                        .show();
                            } else {
                                new SweetAlertDialog(UserProfileActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText("User With This Email already Exits!")
                                        .show();
                            }
                        } else {

                            JSONArray jsonArray = jsonObject.getJSONArray("fetchuser");

                            if (jsonArray.length() == 0) {

                            }


                            btn_edit = findViewById(R.id.btn_edit);
                            btn_edit.setVisibility(View.VISIBLE);

                            img_profile = (ImageView) findViewById(R.id.img_profile);
                            img_profile_background = (ImageView) findViewById(R.id.img_profile_background);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject user = jsonArray.getJSONObject(i);
                                final int id = user.getInt("user_id");
                                final String name = user.getString("user_name").toString();
                                final String user_pass = user.getString("user_pass").toString();
                                final String no_of_bleed = user.getString("user_no_donation").toString();
                                final String blood_group = user.getString("blood_id").toString();
                                final String last_donation_date = user.getString("user_last_donation").toString();
                                final String email = user.getString("user_email").toString();
                                final String contact_no = user.getString("user_contact_no").toString();
                                final String city = user.getString("city_id").toString();
                                final String gender = user.getString("user_gender").toString();
                                final String age = user.getString("user_age").toString();
                                final String date_of_birth = user.getString("date_of_birth").toString();
                                final String diseas = user.getString("user_diseas").toString();
                                final int diseas_status = user.getInt("user_diseas_status");
                                final String image = user.getString("user_image");

                                txt_profile_name.setText(name);
                                if(last_donation_date.equals("0")) {
                                    txt_last_date.setText("null");
                                }else{
                                    txt_last_date.setText(last_donation_date);
                                }
                                txt_no_of_bleed.setText(no_of_bleed + " + ");
                                txt_profile_blood_group.setText(blood_group);

                                txt_profile_email.setText(email);
                                txt_profile_contact_no.setText(contact_no);
                                txt_profile_address.setText(city);
                                txt_profile_gender.setText(gender);
                                txt_profile_age.setText(age);
                                if (diseas_status == 0) {
                                    txt_profile_diseas.setText("No Diseas.");
                                } else {
                                    txt_profile_diseas.setText(diseas);
                                }

                                Picasso.get().load(getResources().getString(R.string.url) + "images/" + image.toString())
                                        .placeholder(R.drawable.breakimage)
                                        .error(R.drawable.breakimage)
                                        .into(img_profile);

                                Picasso.get().load(getResources().getString(R.string.url) + "images/" + image.toString())
                                        .placeholder(R.drawable.breakimage)
                                        .error(R.drawable.breakimage)
                                        .into(img_profile_background);

                                btn_edit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(UserProfileActivity.this, RegisterUserActivity.class);
                                        intent.putExtra("user_profile_id", id);
                                        intent.putExtra("user_name", name);
                                        intent.putExtra("email", email);
                                        intent.putExtra("user_pass", user_pass);
                                        intent.putExtra("blood_group", blood_group);
                                        intent.putExtra("date_of_birth", date_of_birth);
                                        intent.putExtra("contact", contact_no);
                                        intent.putExtra("city", city);
                                        intent.putExtra("gender", gender);
                                        intent.putExtra("image", image);
                                        intent.putExtra("diseas_status", diseas_status);
                                        intent.putExtra("diseas", diseas);
                                        startActivity(intent);
//

                                    }
                                });
                            }

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
                //Toast.makeText(UserProfileActivity.this, "Connection Problem", Toast.LENGTH_LONG).show();
                InternetWarningDialog.showCustomDialog(UserProfileActivity.this);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();


                params.put("user_name", String.valueOf(user));
                params.put("user_pass", String.valueOf(pass));
                if (updatebleed) {
                    params.put("last_donation_date", last_donation_date);
                }


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(UserProfileActivity.this);
        requestQueue.add(request);
        progressDialog = new ProgressDialog(UserProfileActivity.this);
        progressDialog.setTitle("Loading!");
        progressDialog.setMessage("Please wait a while...");
        progressDialog.setCancelable(false);
        progressDialog.show();

    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        getData(tempUser, tempPass, false, "getuserdetails.php");
    }

    void updateBleed() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        last_donation_date = sdf.format(myCalendar.getTime());
        getData(tempUser, tempPass, true, "updatebleed.php");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!logPre.getBoolean("check_login", false)) {
            menu.findItem(R.id.menu_item_logout).setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_item_logout) {
            preEditor.remove("user");
            preEditor.remove("pass");
            preEditor.apply();
            preEditor.commit();
            Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_item_feed_back) {
            Intent intent = new Intent(UserProfileActivity.this, FeedBack.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_item_share) {
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

            // Add data to the intent, the receiving app will decide
            // what to do with it.
            share.putExtra(Intent.EXTRA_SUBJECT, "Price Finder");
            share.putExtra(Intent.EXTRA_TEXT, R.string.applink);

            startActivity(Intent.createChooser(share, "Share link!"));
            return true;
        }else if(id == R.id.menu_item_about){
            Intent intent = new Intent(UserProfileActivity.this, AboutUsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        navigation.setSelectedItemId(R.id.navigation_registeration);
    }
}
