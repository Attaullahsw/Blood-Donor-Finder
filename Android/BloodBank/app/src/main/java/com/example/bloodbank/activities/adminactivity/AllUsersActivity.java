package com.example.bloodbank.activities.adminactivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bloodbank.R;
import com.example.bloodbank.adapters.AllUserListAdapter;
import com.example.bloodbank.dataholder.SearchRecordDataHolder;
import com.example.bloodbank.util.InternetWarningDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AllUsersActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    RecyclerView recyclerView;

    ProgressDialog progressDialog;

    AllUserListAdapter adapter;
    ImageView img_empty_list;


    ArrayList<SearchRecordDataHolder> items = new ArrayList<>();

    SharedPreferences logPre;
    SharedPreferences.Editor preEditor;

    NavigationView navigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);






        // get the reference of RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.list_view_all_user);
        img_empty_list = (ImageView) findViewById(R.id.img_empty_list_user);
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





        adapter = new AllUserListAdapter(AllUsersActivity.this,items);
        recyclerView.setAdapter(adapter);

        getData();



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
        MenuItem searchItem = menu.findItem(R.id.item_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<SearchRecordDataHolder> tempList = new ArrayList<>();
                for(SearchRecordDataHolder temp : items){
                    if(temp.getName().toLowerCase().contains(s.toLowerCase()) || temp.getAddress().toLowerCase().contains(s.toLowerCase())){
                        tempList.add(temp);
                    }
                }
                if(tempList.size() > 0) {
                    AllUserListAdapter adapter = new AllUserListAdapter(AllUsersActivity.this, tempList);
                    recyclerView.setAdapter(adapter);
                }

                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
        });
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
            Intent intent = new Intent(AllUsersActivity.this,AdminHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_add_blood_bank) {
            Intent intent = new Intent(AllUsersActivity.this, AdminAddBloodBankActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_all_city) {
            Intent intent = new Intent(AllUsersActivity.this,AllCityListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        } else if (id == R.id.nav_all_blood_bank) {
            Intent intent = new Intent(AllUsersActivity.this,AllBloodBankAdmin.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if(id == R.id.nav_all_feedback){
            Intent intent = new Intent(AllUsersActivity.this, AllFeedBackActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        navigationView.getMenu().getItem(5).setChecked(true);
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
                        Toast.makeText(AllUsersActivity.this, jsonObject.getString("msg").toString(), Toast.LENGTH_LONG).show();
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
                InternetWarningDialog.showCustomDialog(AllUsersActivity.this);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();


                params.put("fetchRecord","fetch");
                params.put("fetchalluser","fetch");



                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AllUsersActivity.this);
        requestQueue.add(request);
        progressDialog = new ProgressDialog(AllUsersActivity.this);
        progressDialog.setTitle("Loading!");
        progressDialog.setMessage("Please wait a while...");
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

}
