package com.example.bloodbank.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.example.bloodbank.adapters.SearchBloodBankAdapter;
import com.example.bloodbank.dataholder.BloodBankDataHolder;
import com.example.bloodbank.util.InternetWarningDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchBloodBankActivity extends AppCompatActivity {

    RecyclerView list_blood_bank;
    ArrayList<BloodBankDataHolder> items = new ArrayList<>();
    SearchBloodBankAdapter adapter;

    int city_id;

    SharedPreferences logPre;
    SharedPreferences.Editor preEditor;

    BottomNavigationView navigation;

    ProgressDialog progressDialog;

    ImageView img_empty_list2;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    return true;
                case R.id.navigation_registeration:

                    if (logPre.getBoolean("check_login", false)) {
                        Intent intent = new Intent(SearchBloodBankActivity.this, UserProfileActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    } else {

                        Intent intent = new Intent(SearchBloodBankActivity.this, RegisterUserActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }

                    return true;
                case R.id.navigation_add_blood_bank:
                    Intent intent = new Intent(SearchBloodBankActivity.this, AddBloodBankActivity.class);
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
        setContentView(R.layout.activity_search_blood_bank);

        logPre = getSharedPreferences("UserLogin", MODE_PRIVATE);
        preEditor = logPre.edit();

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        navigation.setSelectedItemId(R.id.navigation_home);
        if (logPre.getBoolean("check_login", false)) {
            navigation.getMenu().findItem(R.id.navigation_registeration).setTitle("Profile");
        }

        city_id = getIntent().getIntExtra("city_id",0);
        if(city_id==0){
            Toast.makeText(SearchBloodBankActivity.this,"Some thing went Wrong",Toast.LENGTH_SHORT).show();
            finish();
        }





        list_blood_bank = (RecyclerView) findViewById(R.id.list_blood_bank);
        img_empty_list2 = (ImageView) findViewById(R.id.img_empty_list2);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        list_blood_bank.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
        list_blood_bank.setHasFixedSize(true);
        list_blood_bank.setItemViewCacheSize(20);
        list_blood_bank.setDrawingCacheEnabled(true);
        list_blood_bank.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        adapter = new SearchBloodBankAdapter(SearchBloodBankActivity.this,items);
        list_blood_bank.setAdapter(adapter);






        getData(city_id);

    }



    public void getData(final int city_id) {

        String strings = getResources().getString(R.string.url) + "fetchbloodbank.php";
        StringRequest request = new StringRequest(Request.Method.POST, strings, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    if (error) {
                        progressDialog.dismiss();
                        Toast.makeText(SearchBloodBankActivity.this, jsonObject.getString("msg").toString(), Toast.LENGTH_LONG).show();
                    } else {

                        JSONArray jsonArray = jsonObject.getJSONArray("searchrecord");
                        if(jsonArray.length()==0){
                            img_empty_list2.setVisibility(View.VISIBLE);
                            list_blood_bank.setVisibility(View.GONE);
                        }else {
                            img_empty_list2.setVisibility(View.GONE);
                            list_blood_bank.setVisibility(View.VISIBLE);
                        }

                        for(int i=0; i<jsonArray.length();i++){
                            JSONObject user = jsonArray.getJSONObject(i);
                            items.add(new BloodBankDataHolder(user.getString("bb_name"),user.getString("bb_address"),
                                    user.getString("bb_email"),user.getString("bb_contact_no")));

                        }

                        adapter.notifyDataSetChanged();


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
                //Toast.makeText(SearchBloodBankActivity.this, "Connection Problem", Toast.LENGTH_LONG).show();
                InternetWarningDialog.showCustomDialog(SearchBloodBankActivity.this);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("blood_city_id", String.valueOf(city_id));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(SearchBloodBankActivity.this);
        requestQueue.add(request);
        progressDialog = new ProgressDialog(SearchBloodBankActivity.this);
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
            Intent intent = new Intent(SearchBloodBankActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_item_feed_back) {
            Intent intent = new Intent(SearchBloodBankActivity.this, FeedBack.class);
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
            Intent intent = new Intent(SearchBloodBankActivity.this, AboutUsActivity.class);
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
