package com.example.bloodbank.activities.adminactivity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.example.bloodbank.adapters.CityListAdapter;
import com.example.bloodbank.dataholder.CityDataHolder;
import com.example.bloodbank.util.InternetWarningDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AllCityListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView list_all_city_record;
    ImageView img_empty_list3;

    ArrayList<CityDataHolder> items = new ArrayList<>();
    CityListAdapter adapter;

    private NavigationView navigationView;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_city_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        img_empty_list3 = (ImageView) findViewById(R.id.img_empty_list3);
        list_all_city_record = (RecyclerView) findViewById(R.id.list_all_city_record);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        list_all_city_record.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
        list_all_city_record.setHasFixedSize(true);
        list_all_city_record.setItemViewCacheSize(20);
        list_all_city_record.setDrawingCacheEnabled(true);
        list_all_city_record.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(list_all_city_record.getContext(),
                linearLayoutManager.getOrientation());
        list_all_city_record.addItemDecoration(dividerItemDecoration);

        adapter = new CityListAdapter(AllCityListActivity.this,items);
        list_all_city_record.setAdapter(adapter);

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
                ArrayList<CityDataHolder> tempList = new ArrayList<>();
                for(CityDataHolder temp : items){
                    if(temp.getName().toLowerCase().contains(s.toLowerCase())){
                        tempList.add(temp);
                    }
                }
                if(tempList.size() > 0) {
                    CityListAdapter adapter = new CityListAdapter(AllCityListActivity.this, tempList);
                    list_all_city_record.setAdapter(adapter);
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
            Intent intent = new Intent(AllCityListActivity.this,AdminHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        } else if (id == R.id.nav_add_blood_bank) {
            Intent intent = new Intent(AllCityListActivity.this, AdminAddBloodBankActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_all_city) {

        } else if (id == R.id.nav_all_blood_bank) {
            Intent intent = new Intent(AllCityListActivity.this,AllBloodBankAdmin.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else if(id == R.id.nav_all_feedback){
            Intent intent = new Intent(AllCityListActivity.this, AllFeedBackActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else if(id == R.id.nav_all_user){
            Intent intent = new Intent(AllCityListActivity.this, AllUsersActivity.class);
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
                        Toast.makeText(AllCityListActivity.this,jsonObject.getString("msg").toString(),Toast.LENGTH_LONG).show();
                    }else {




                        /***********GET CITY NAME FROM SERVER**********/
                        JSONArray cityJsonArray = jsonObject.getJSONArray("city");

                        if(cityJsonArray.length() == 0){
                            list_all_city_record.setVisibility(View.GONE);
                            img_empty_list3.setVisibility(View.VISIBLE);
                        }else{
                            list_all_city_record.setVisibility(View.VISIBLE);
                            img_empty_list3.setVisibility(View.GONE);
                        }

                        for (int i=0; i<cityJsonArray.length(); i++){
                            JSONObject city = cityJsonArray.getJSONObject(i);

                            items.add(new CityDataHolder(city.getInt("city_id"),city.getString("city_name")));
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
              //  Toast.makeText(AllCityListActivity.this,"Connection Problem", Toast.LENGTH_LONG).show();
                InternetWarningDialog.showCustomDialog(AllCityListActivity.this);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();



                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AllCityListActivity.this);
        requestQueue.add(request);
        progressDialog = new ProgressDialog(AllCityListActivity.this);
        progressDialog.setTitle("Loading!");
        progressDialog.setMessage("Please wait a while...");
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        navigationView.getMenu().getItem(1).setChecked(true);
    }
}
