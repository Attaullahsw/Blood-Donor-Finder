package com.example.bloodbank.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    Button btn_search;
    String blood_group[];
    int blood_group_id[];
    String city_name[];
    int city_id[];
    ProgressDialog progressDialog;
    BottomNavigationView navigation;
    Context context;
    SharedPreferences logPre;
    SharedPreferences.Editor preEditor;
    private MaterialSpinner spinner;
    private AutoCompleteTextView edt_chooseDistrict;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    return true;
                case R.id.navigation_registeration:

                    if (logPre.getBoolean("check_login", false)) {
                        Intent intent = new Intent(SearchActivity.this, UserProfileActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    } else {

                        Intent intent = new Intent(SearchActivity.this, RegisterUserActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }

                    return true;
                case R.id.navigation_add_blood_bank:
                    Intent intent = new Intent(SearchActivity.this, AddBloodBankActivity.class);
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
        setContentView(R.layout.activity_search);

        context = SearchActivity.this;

        logPre = getSharedPreferences("UserLogin", MODE_PRIVATE);
        preEditor = logPre.edit();


        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);

        if (logPre.getBoolean("check_login", false)) {
            navigation.getMenu().findItem(R.id.navigation_registeration).setTitle("Profile");
        }

        getData();


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
                        Toast.makeText(SearchActivity.this, jsonObject.getString("msg").toString(), Toast.LENGTH_LONG).show();
                    } else {

                        /***********GET BLOOD GROUP FROM SERVER**********/
                        JSONArray bloodGroupJsonArray = jsonObject.getJSONArray("blood_group");
                        blood_group = new String[bloodGroupJsonArray.length() + 1];
                        blood_group[0] = "Select Blood Group...";
                        blood_group_id = new int[bloodGroupJsonArray.length() + 1];
                        blood_group_id[0] = 0;

                        for (int i = 0; i < bloodGroupJsonArray.length(); i++) {
                            JSONObject blood = bloodGroupJsonArray.getJSONObject(i);
                            blood_group_id[i + 1] = blood.getInt("blood_group_id");
                            blood_group[i + 1] = blood.getString("blood_group");
                        }

                        spinner = (MaterialSpinner) findViewById(R.id.spn_blood_group);
                        spinner.setItems(blood_group);

                        /***********END GET BLOOD GROUP FROM SERVER**********/


                        /***********GET CITY NAME FROM SERVER**********/
                        JSONArray cityJsonArray = jsonObject.getJSONArray("city");
                        city_id = new int[cityJsonArray.length()];
                        city_name = new String[cityJsonArray.length()];

                        for (int i = 0; i < cityJsonArray.length(); i++) {
                            JSONObject city = cityJsonArray.getJSONObject(i);
                            city_id[i] = city.getInt("city_id");
                            city_name[i] = city.getString("city_name");
                        }
                        edt_chooseDistrict = (AutoCompleteTextView) findViewById(R.id.edt_chooseDistrict);
                        ArrayAdapter adapter = new ArrayAdapter(SearchActivity.this, android.R.layout.simple_list_item_1, city_name);
                        edt_chooseDistrict.setThreshold(1);
                        edt_chooseDistrict.setAdapter(adapter);


                        /***********END GET CITY NAME FROM SERVER**********/

                        Button btn_search = (Button) findViewById(R.id.btn_search);
                        btn_search.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int c_id = 0;
                                String selectedCity = edt_chooseDistrict.getText().toString();
                                for (int i = 0; i < city_name.length; i++) {
                                    if (selectedCity.equals(city_name[i])) {
                                        c_id = city_id[i];
                                    }
                                }
                                int b_id = blood_group_id[spinner.getSelectedIndex()];
                                Intent intent = new Intent(SearchActivity.this, SearchRecordActivity.class);

                                if (b_id == 0 && c_id == 0) {
                                    Toast.makeText(SearchActivity.this, "One Field Is Required!", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(SearchActivity.this, "Select the correct City", Toast.LENGTH_LONG).show();
                                } else if (b_id == 0) {
                                    intent.putExtra("city_id", c_id);
                                    startActivity(intent);
                                } else if (c_id == 0) {
                                    Toast.makeText(SearchActivity.this, "City is Empty", Toast.LENGTH_LONG).show();
                                    intent.putExtra("blood_id", b_id);
                                    startActivity(intent);
                                } else {
                                    intent.putExtra("blood_id", b_id);
                                    intent.putExtra("city_id", c_id);
                                    startActivity(intent);
                                }


                            }
                        });


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
                // Toast.makeText(SearchActivity.this,"Connection Problem", Toast.LENGTH_LONG).show();
                InternetWarningDialog.showCustomDialog(SearchActivity.this);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(SearchActivity.this);
        requestQueue.add(request);
        progressDialog = new ProgressDialog(SearchActivity.this);
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
            Intent intent = new Intent(SearchActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_item_feed_back) {
            Intent intent = new Intent(SearchActivity.this, FeedBack.class);
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
        } else if (id == R.id.menu_item_about) {
            Intent intent = new Intent(SearchActivity.this, AboutUsActivity.class);
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

    public void checkLogin(View view) {


        if (logPre.getBoolean("check_login", false)) {
//            if (ContextCompat.checkSelfPermission(this,
//                    Manifest.permission.ACCESS_FINE_LOCATION)
//                    == PackageManager.PERMISSION_GRANTED) {
//
////                Intent intent = new Intent(context,LiveLocationActivity.class);
////                startActivity(intent);
//            }

           if(checkLocationPermission()){

               if(statusCheck()){
                   int b_id = blood_group_id[spinner.getSelectedIndex()];
                   if(b_id == 0){
                       Toast.makeText(context,"Blood Group May Not Be Empty!",Toast.LENGTH_SHORT).show();
                   }else {
                       Intent intent = new Intent(context, LiveLocationActivity.class);
                       intent.putExtra("blood_id",b_id);
                       startActivity(intent);
                   }
               }
           }else{
               Toast.makeText(context,"Location Permission is needed for this Fetaure",Toast.LENGTH_SHORT).show();
           }

        } else {

            signin();
        }

    }

    public void signin() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.signuporlogin, null);
        dialogBuilder.setView(dialogView);

        Button btn_sign_in = (Button) dialogView.findViewById(R.id.btn_signin);
        Button btn_sign_up = (Button) dialogView.findViewById(R.id.btn_singUp);
        TextView txtCancel = (TextView) dialogView.findViewById(R.id.btn_sign_cancel);


        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        });
        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RegisterUserActivity.class);
                context.startActivity(intent);
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

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Do not turn off location!")
                        .setMessage("You can access other doner through this")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(SearchActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

//                       Intent intent = new Intent(context,LiveLocationActivity.class);
//                       startActivity(intent);

                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }


    public boolean statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
            return false;

        }else{
            return true;
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }



}
