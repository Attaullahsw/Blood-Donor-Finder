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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SelectCityBloodBankActivity extends AppCompatActivity {


    Button btn_search_blood_bank3;
    AutoCompleteTextView edt_choosecity;

    String city_name[];
    int city_id[];

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
                        Intent intent = new Intent(SelectCityBloodBankActivity.this, UserProfileActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    } else {

                        Intent intent = new Intent(SelectCityBloodBankActivity.this, RegisterUserActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }

                    return true;
                case R.id.navigation_add_blood_bank:
                    Intent intent = new Intent(SelectCityBloodBankActivity.this, AddBloodBankActivity.class);
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
        setContentView(R.layout.activity_select_city_blood_bank);


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
    public void getData(){

        String strings = getResources().getString(R.string.url)+"fetchBloodCity.php";
        StringRequest request = new StringRequest(Request.Method.POST, strings, new Response.Listener<String>() {
            @Override
            public void onResponse(String response){

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    if(error){
                        progressDialog.dismiss();
                        Toast.makeText(SelectCityBloodBankActivity.this,jsonObject.getString("msg").toString(),Toast.LENGTH_LONG).show();
                    }else {




                        /***********GET CITY NAME FROM SERVER**********/
                        JSONArray cityJsonArray = jsonObject.getJSONArray("city");
                        city_id = new int[cityJsonArray.length()];
                        city_name = new String[cityJsonArray.length()];

                        for (int i=0; i<cityJsonArray.length(); i++){
                            JSONObject city = cityJsonArray.getJSONObject(i);
                            city_id[i] = city.getInt("city_id");
                            city_name[i] = city.getString("city_name");
                        }
                        edt_choosecity = (AutoCompleteTextView) findViewById(R.id.edt_choosecity);
                        ArrayAdapter adapter = new ArrayAdapter(SelectCityBloodBankActivity.this,android.R.layout.simple_list_item_1,city_name);
                        edt_choosecity.setThreshold(1);
                        edt_choosecity.setAdapter(adapter);


                        /***********END GET CITY NAME FROM SERVER**********/

                        btn_search_blood_bank3 = (Button) findViewById(R.id.btn_search_blood_bank3);
                        btn_search_blood_bank3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int c_id = 0;
                                String selectedCity = edt_choosecity.getText().toString();
                                for(int i=0; i<city_name.length; i++){
                                    if(selectedCity.equals(city_name[i])){
                                        c_id=city_id[i];
                                    }
                                }

                                Intent intent = new Intent(SelectCityBloodBankActivity.this,SearchBloodBankActivity.class);

                                if(c_id==0) {
                                    Toast.makeText(SelectCityBloodBankActivity.this,"Select the correct City",Toast.LENGTH_LONG).show();
                                }else {
                                    intent.putExtra("city_id",c_id);
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
              //  Toast.makeText(SelectCityBloodBankActivity.this,"Connection Problem", Toast.LENGTH_LONG).show();
                InternetWarningDialog.showCustomDialog(SelectCityBloodBankActivity.this);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();



                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(SelectCityBloodBankActivity.this);
        requestQueue.add(request);
        progressDialog = new ProgressDialog(SelectCityBloodBankActivity.this);
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
            Intent intent = new Intent(SelectCityBloodBankActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_item_feed_back) {
            Intent intent = new Intent(SelectCityBloodBankActivity.this, FeedBack.class);
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
            Intent intent = new Intent(SelectCityBloodBankActivity.this, AboutUsActivity.class);
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
