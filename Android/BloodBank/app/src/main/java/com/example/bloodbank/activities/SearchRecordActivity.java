package com.example.bloodbank.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
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
import com.example.bloodbank.adapters.SearchRecordAdapter;
import com.example.bloodbank.dataholder.SearchRecordDataHolder;
import com.example.bloodbank.util.InternetWarningDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchRecordActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    ProgressDialog progressDialog;

    SearchRecordAdapter adapter;
    ImageView img_empty_list;

    BottomNavigationView navigation;

    ArrayList<SearchRecordDataHolder> items = new ArrayList<>();

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
                        Intent intent = new Intent(SearchRecordActivity.this, UserProfileActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    } else {

                        Intent intent = new Intent(SearchRecordActivity.this, RegisterUserActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }

                    return true;
                case R.id.navigation_add_blood_bank:
                    Intent intent = new Intent(SearchRecordActivity.this, AddBloodBankActivity.class);
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
        setContentView(R.layout.activity_search_record);




        logPre = getSharedPreferences("UserLogin", MODE_PRIVATE);
        preEditor = logPre.edit();

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        navigation.setSelectedItemId(R.id.navigation_home);
        if (logPre.getBoolean("check_login", false)) {
            navigation.getMenu().findItem(R.id.navigation_registeration).setTitle("Profile");
        }




        // get the reference of RecyclerView
         recyclerView = (RecyclerView) findViewById(R.id.list_view_sreach_record);
        img_empty_list = (ImageView) findViewById(R.id.img_empty_list);
// set a LinearLayoutManager with default orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);





        adapter = new SearchRecordAdapter(SearchRecordActivity.this,items);
        recyclerView.setAdapter(adapter);

        getData();

    }

    public void getData() {

        String strings = getResources().getString(R.string.url) + "fetchsearchuser.php";
        StringRequest request = new StringRequest(Request.Method.POST, strings, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    if (error) {
                        progressDialog.dismiss();
                        Toast.makeText(SearchRecordActivity.this, jsonObject.getString("msg").toString(), Toast.LENGTH_LONG).show();
                    } else {

                        JSONArray jsonArray = jsonObject.getJSONArray("searchrecord");
                        if(jsonArray.length()==0){
                            img_empty_list.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }else {
                            img_empty_list.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }

                        for(int i=0; i<jsonArray.length();i++){
                            JSONObject user = jsonArray.getJSONObject(i);
                            items.add(new SearchRecordDataHolder(user.getInt("user_id"),user.getString("user_name"),user.getString("city_id"),
                                    user.getString("blood_id"),user.getString("user_image"),user.getString("user_age"),user.getString("user_contact_no"),
                                    user.getString("user_email"),false
                                    ));

                        }

                        JSONArray uniAccepterUserArray = jsonObject.getJSONArray("uniuser");

                        if(uniAccepterUserArray.length()!=0){
                            img_empty_list.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                        for(int i=0; i<uniAccepterUserArray.length();i++){
                            JSONObject user = uniAccepterUserArray.getJSONObject(i);
                            items.add(new SearchRecordDataHolder(user.getInt("user_id"),user.getString("user_name"),user.getString("city_id"),
                                    user.getString("blood_id"),user.getString("user_image"),user.getString("user_age"),user.getString("user_contact_no"),
                                    user.getString("user_email"),true
                            ));

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
               // Toast.makeText(SearchRecordActivity.this, "Connection Problem", Toast.LENGTH_LONG).show();
                InternetWarningDialog.showCustomDialog(SearchRecordActivity.this);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                int city_id = getIntent().getIntExtra("city_id",0);
                int blood_id = getIntent().getIntExtra("blood_id",0);

                if(city_id!=0 && blood_id!=0){
                    params.put("city_id", String.valueOf(city_id));
                    params.put("blood_id", String.valueOf(blood_id));
                }else if(blood_id != 0){
                    params.put("blood_id", String.valueOf(blood_id));
                }else if(city_id != 0){
                    params.put("city_id", String.valueOf(city_id));
                }

                params.put("fetchRecord","fetch");



                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(SearchRecordActivity.this);
        requestQueue.add(request);
        progressDialog = new ProgressDialog(SearchRecordActivity.this);
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
            Intent intent = new Intent(SearchRecordActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_item_feed_back) {
            Intent intent = new Intent(SearchRecordActivity.this, FeedBack.class);
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
            Intent intent = new Intent(SearchRecordActivity.this, AboutUsActivity.class);
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
