package com.example.bloodbank.activities;

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

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    TextView txt_profile_name, txt_no_of_bleed, txt_last_date, txt_profile_email, txt_profile_contact_no, txt_profile_address, txt_profile_gender,
            txt_profile_age, txt_profile_diseas,txt_profile_blood_group;

    ImageView img_profile, img_profile_background;

    ProgressDialog progressDialog;

    BottomNavigationView navigation;

    SharedPreferences logPre;
    SharedPreferences.Editor preEditor;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    return true;
                case R.id.navigation_registeration:

                    if (logPre.getBoolean("check_login", false)) {
                        Intent intent = new Intent(ProfileActivity.this, UserProfileActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    } else {

                        Intent intent = new Intent(ProfileActivity.this, RegisterUserActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }

                    return true;
                case R.id.navigation_add_blood_bank:
                    Intent intent = new Intent(ProfileActivity.this, AddBloodBankActivity.class);
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

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        logPre = getSharedPreferences("UserLogin", MODE_PRIVATE);
        preEditor = logPre.edit();

        navigation.setSelectedItemId(R.id.navigation_home);
        if (logPre.getBoolean("check_login", false)) {
            navigation.getMenu().findItem(R.id.navigation_registeration).setTitle("Profile");
        }


        int user_id = getIntent().getIntExtra("user_id", 0);
        if (user_id == 0) {
            Toast.makeText(ProfileActivity.this, "Some thing went wrong!", Toast.LENGTH_SHORT).show();
            finish();
        } else {

            getData(user_id);
        }

    }


    public void getData(final int user_id) {

        String strings = getResources().getString(R.string.url) + "getuserdetails.php";
        StringRequest request = new StringRequest(Request.Method.POST, strings, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    if (error) {
                        progressDialog.dismiss();
                        Toast.makeText(ProfileActivity.this, jsonObject.getString("msg").toString(), Toast.LENGTH_LONG).show();
                    } else {

                        JSONArray jsonArray = jsonObject.getJSONArray("fetchuser");

                        if (jsonArray.length() == 0) {

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

                        img_profile = (ImageView) findViewById(R.id.img_profile);
                        img_profile_background = (ImageView) findViewById(R.id.img_profile_background);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject user = jsonArray.getJSONObject(i);
                            //user.getInt("user_id"),
                            txt_profile_name.setText(user.getString("user_name").toString());
                            txt_no_of_bleed.setText(user.getString("user_no_donation").toString());
                            txt_profile_blood_group.setText(user.getString("blood_id").toString());
                            txt_last_date.setText(user.getString("user_last_donation").toString());
                            txt_profile_email.setText(user.getString("user_email").toString());
                            txt_profile_contact_no.setText(user.getString("user_contact_no").toString());
                            txt_profile_address.setText(user.getString("city_id").toString());
                            txt_profile_gender.setText(user.getString("user_gender").toString());
                            txt_profile_age.setText(user.getString("user_age").toString());
                            if(user.getInt("user_diseas_status")==0){
                                txt_profile_diseas.setText("No Diseas.");
                            }else{
                                txt_profile_diseas.setText(user.getString("user_diseas").toString());
                            }

                            Picasso.get().load(getResources().getString(R.string.url) + "images/" + user.getString("user_image").toString())
                                    .placeholder(R.drawable.breakimage)
                                    .error(R.drawable.breakimage)
                                    .into(img_profile);

                            Picasso.get().load(getResources().getString(R.string.url) + "images/" + user.getString("user_image").toString())
                                    .placeholder(R.drawable.breakimage)
                                    .error(R.drawable.breakimage)
                                    .into(img_profile_background);



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
               // Toast.makeText(ProfileActivity.this, "Connection Problem", Toast.LENGTH_LONG).show();
                InternetWarningDialog.showCustomDialog(ProfileActivity.this);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();


                params.put("user_id", String.valueOf(user_id));


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ProfileActivity.this);
        requestQueue.add(request);
        progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setTitle("Loading!");
        progressDialog.setMessage("Please wait a while...");
        progressDialog.setCancelable(false);
        progressDialog.show();

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
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_item_feed_back) {
            Intent intent = new Intent(ProfileActivity.this, FeedBack.class);
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
            Intent intent = new Intent(ProfileActivity.this, AboutUsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        navigation.setSelectedItemId(R.id.navigation_home);
    }


}
