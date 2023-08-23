package com.example.bloodbank.activities.adminactivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.example.bloodbank.activities.SearchBloodBankActivity;
import com.example.bloodbank.util.InternetWarningDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AdminAddBloodBankActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ProgressDialog progressDialog;


    EditText edt_blood_bank_name, edt_blood_bank_email, edt_blood_bank_contact_no, edt_blood_bank_address;
    AutoCompleteTextView auto_blood_bank_city;

    ArrayList<String> city_id = new ArrayList<>();
    ArrayList<String> city_name = new ArrayList<>();

    ArrayAdapter adapter;

    int blood_bank_id, city_id2;
    String name, email, con, address;

    private NavigationView navigationView;


    Button btn_blood_bank_add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_blood_bank);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        blood_bank_id = getIntent().getIntExtra("id", 0);

        edt_blood_bank_name = (EditText) findViewById(R.id.edt_blood_bank_name);
        edt_blood_bank_email = (EditText) findViewById(R.id.edt_blood_bank_email);
        edt_blood_bank_contact_no = (EditText) findViewById(R.id.edt_blood_bank_contact_no);
        edt_blood_bank_address = (EditText) findViewById(R.id.edt_blood_bank_address);
        auto_blood_bank_city = (AutoCompleteTextView) findViewById(R.id.auto_blood_bank_city);
        btn_blood_bank_add = (Button) findViewById(R.id.btn_blood_bank_add);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        adapter = new ArrayAdapter(AdminAddBloodBankActivity.this, android.R.layout.simple_list_item_1, city_name);
        auto_blood_bank_city.setThreshold(1);
        auto_blood_bank_city.setAdapter(adapter);

        if (blood_bank_id != 0) {

            name = getIntent().getStringExtra("name");
            email = getIntent().getStringExtra("email");
            con = getIntent().getStringExtra("con");
            address = getIntent().getStringExtra("address");
            city_id2 = getIntent().getIntExtra("city", 0);

            edt_blood_bank_name.setText(name);
            edt_blood_bank_email.setText(email);
            edt_blood_bank_address.setText(address);
            edt_blood_bank_contact_no.setText(con);


            btn_blood_bank_add.setText("Update Blood Bank");

        }

        getData();


        btn_blood_bank_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = edt_blood_bank_name.getText().toString();
                String email = edt_blood_bank_email.getText().toString();
                String contact = edt_blood_bank_contact_no.getText().toString();
                String address = edt_blood_bank_address.getText().toString();


                String c_id = "";
                String selectedCity = auto_blood_bank_city.getText().toString();
                for (int i = 0; i < city_name.size(); i++) {
                    if (selectedCity.equals(city_name.get(i))) {
                        c_id = city_id.get(i);
                    }
                }

                Intent intent = new Intent(AdminAddBloodBankActivity.this, SearchBloodBankActivity.class);

                if (name.equals("")) {
                    edt_blood_bank_name.requestFocus();
                    Toast.makeText(AdminAddBloodBankActivity.this, "Name Field Is Empty!", Toast.LENGTH_LONG).show();
                } else if (email.equals("")) {
                    Toast.makeText(AdminAddBloodBankActivity.this, "Email Field Is Empty!", Toast.LENGTH_LONG).show();
                    edt_blood_bank_email.requestFocus();
                } else if (contact.equals("")) {
                    Toast.makeText(AdminAddBloodBankActivity.this, "Contact NO Field Is Empty!", Toast.LENGTH_LONG).show();
                    edt_blood_bank_contact_no.requestFocus();
                } else if (address.equals("")) {
                    Toast.makeText(AdminAddBloodBankActivity.this, "Address Field Is Empty!", Toast.LENGTH_LONG).show();
                    edt_blood_bank_address.requestFocus();
                } else if (c_id.equals("")) {
                    auto_blood_bank_city.requestFocus();
                    Toast.makeText(AdminAddBloodBankActivity.this, "Select the correct City", Toast.LENGTH_LONG).show();
                } else {
                    insertData(name, email, contact, address, c_id);
                }

            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add_city) {
            Intent intent = new Intent(AdminAddBloodBankActivity.this, AdminHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);


        } else if (id == R.id.nav_add_blood_bank) {

        } else if (id == R.id.nav_all_city) {
            Intent intent = new Intent(AdminAddBloodBankActivity.this, AdminAddBloodBankActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_all_blood_bank) {
            Intent intent = new Intent(AdminAddBloodBankActivity.this,AllBloodBankAdmin.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }else if(id == R.id.nav_all_feedback){
            Intent intent = new Intent(AdminAddBloodBankActivity.this, AllFeedBackActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else if(id == R.id.nav_all_user){
            Intent intent = new Intent(AdminAddBloodBankActivity.this, AllUsersActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * Request to the server for data
     */
    public void insertData(final String name, final String email, final String contact, final String address, final String city) {

        String strings = getResources().getString(R.string.url) + "insertbloodbankorcity.php";
        StringRequest request = new StringRequest(Request.Method.POST, strings, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    if (error) {
                        progressDialog.dismiss();
                        Toast.makeText(AdminAddBloodBankActivity.this, jsonObject.getString("msg").toString(), Toast.LENGTH_LONG).show();
                    } else {


                        boolean insert = jsonObject.getBoolean("insert");
                        if (insert) {

                            if (blood_bank_id != 0) {
                                new SweetAlertDialog(AdminAddBloodBankActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Blood Bank Updated Successfully!")
                                        .setConfirmText("ok")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                                Intent intent = new Intent(AdminAddBloodBankActivity.this,AllBloodBankAdmin.class);
                                                finish();
                                                startActivity(intent);
                                            }
                                        })
                                        .show();
                            } else {
                                new SweetAlertDialog(AdminAddBloodBankActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Blood Bank Added Successfully!")
                                        .setConfirmText("ok")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                                edt_blood_bank_name.setText("");
                                                edt_blood_bank_email.setText("");
                                                edt_blood_bank_contact_no.setText("");
                                                edt_blood_bank_address.setText("");
                                                auto_blood_bank_city.setText("");
                                                edt_blood_bank_name.requestFocus();
                                            }
                                        })
                                        .show();
                            }
                        } else {
                            new SweetAlertDialog(AdminAddBloodBankActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("Something went wrong!")
                                    .show();
                        }

                        adapter.notifyDataSetChanged();


                        /***********END GET CITY NAME FROM SERVER**********/
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
                //Toast.makeText(AdminAddBloodBankActivity.this, "Connection Problem", Toast.LENGTH_LONG).show();
                InternetWarningDialog.showCustomDialog(AdminAddBloodBankActivity.this);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("conatct", contact);
                params.put("address", address);
                params.put("city_id", city);

                if (blood_bank_id != 0) {
                    params.put("blood_bank_id", String.valueOf(blood_bank_id));
                }

                params.put("blood_insert", "blood_insert");


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AdminAddBloodBankActivity.this);
        requestQueue.add(request);
        progressDialog = new ProgressDialog(AdminAddBloodBankActivity.this);
        progressDialog.setTitle("Loading!");
        progressDialog.setMessage("Please wait a while...");
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    /**
     * Request to the server for data
     */
    public void getData() {

        String strings = getResources().getString(R.string.url) + "fetchBloodCity.php";
        StringRequest request = new StringRequest(Request.Method.POST, strings, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    if (error) {
                        progressDialog.dismiss();
                        Toast.makeText(AdminAddBloodBankActivity.this, jsonObject.getString("msg").toString(), Toast.LENGTH_LONG).show();
                    } else {


                        int index = -1;
                        /***********GET CITY NAME FROM SERVER**********/
                        JSONArray cityJsonArray = jsonObject.getJSONArray("city");
                        for (int i = 0; i < cityJsonArray.length(); i++) {
                            JSONObject city = cityJsonArray.getJSONObject(i);
                            city_id.add(city.getString("city_id"));
                            int c = city.getInt("city_id");
                            city_name.add(city.getString("city_name"));
                            if (city_id2 == c) {
                                index = i;
                            }

                        }

                        adapter.notifyDataSetChanged();

                        if (index > -1) {
                            auto_blood_bank_city.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    auto_blood_bank_city.showDropDown();
                                }
                            }, 500);
                            auto_blood_bank_city.setText(city_name.get(index));
                            auto_blood_bank_city.setSelection(index);
                        }


                        /***********END GET CITY NAME FROM SERVER**********/
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
                Toast.makeText(AdminAddBloodBankActivity.this, "Connection Problem", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AdminAddBloodBankActivity.this);
        requestQueue.add(request);
        progressDialog = new ProgressDialog(AdminAddBloodBankActivity.this);
        progressDialog.setTitle("Loading!");
        progressDialog.setMessage("Please wait a while...");
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        navigationView.getMenu().getItem(2).setChecked(true);
    }

}
