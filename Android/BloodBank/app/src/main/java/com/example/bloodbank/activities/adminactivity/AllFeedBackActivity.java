package com.example.bloodbank.activities.adminactivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bloodbank.R;
import com.example.bloodbank.adapters.FeedBackListAdapter;
import com.example.bloodbank.dataholder.FeedBackDataHolder;
import com.example.bloodbank.util.InternetWarningDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AllFeedBackActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView list_all_feed_back_record;
    ImageView img_empty_list3;

    ArrayList<FeedBackDataHolder> items = new ArrayList<>();
    FeedBackListAdapter adapter;

    private NavigationView navigationView;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_feed_back);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        img_empty_list3 = (ImageView) findViewById(R.id.img_empty_list_f);
        list_all_feed_back_record = (RecyclerView) findViewById(R.id.list_all_feed_back_record);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        list_all_feed_back_record.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
        list_all_feed_back_record.setHasFixedSize(true);
        list_all_feed_back_record.setItemViewCacheSize(20);
        list_all_feed_back_record.setDrawingCacheEnabled(true);
        list_all_feed_back_record.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(list_all_feed_back_record.getContext(),
                linearLayoutManager.getOrientation());
        list_all_feed_back_record.addItemDecoration(dividerItemDecoration);

        adapter = new FeedBackListAdapter(AllFeedBackActivity.this,items);
        list_all_feed_back_record.setAdapter(adapter);

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
            Intent intent = new Intent(AllFeedBackActivity.this,AdminHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_add_blood_bank) {
            Intent intent = new Intent(AllFeedBackActivity.this, AdminAddBloodBankActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_all_city) {
            Intent intent = new Intent(AllFeedBackActivity.this,AllCityListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        } else if (id == R.id.nav_all_blood_bank) {
            Intent intent = new Intent(AllFeedBackActivity.this,AllBloodBankAdmin.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if(id == R.id.nav_all_feedback){

        }else if(id == R.id.nav_all_user){
            Intent intent = new Intent(AllFeedBackActivity.this, AllUsersActivity.class);
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

        String strings = getResources().getString(R.string.url)+"fetchfeedback.php";
        StringRequest request = new StringRequest(Request.Method.POST, strings, new Response.Listener<String>() {
            @Override
            public void onResponse(String response){

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    if(error){
                        progressDialog.dismiss();
                        Toast.makeText(AllFeedBackActivity.this,jsonObject.getString("msg").toString(),Toast.LENGTH_LONG).show();
                    }else {




                        /***********GET CITY NAME FROM SERVER**********/
                        JSONArray feedback_tbl = jsonObject.getJSONArray("feedback_tbl");

                        if(feedback_tbl.length() == 0){
                            list_all_feed_back_record.setVisibility(View.GONE);
                            img_empty_list3.setVisibility(View.VISIBLE);
                        }else{
                            list_all_feed_back_record.setVisibility(View.VISIBLE);
                            img_empty_list3.setVisibility(View.GONE);
                        }

                        for (int i=0; i<feedback_tbl.length(); i++){
                            JSONObject city = feedback_tbl.getJSONObject(i);

                            items.add(new FeedBackDataHolder(city.getInt("feedback_id"),city.getString("feedback_name"),city.getString("feedback_email"),
                                    city.getString("feedback_msg"),city.getInt("feedback_status")));
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
                InternetWarningDialog.showCustomDialog(AllFeedBackActivity.this);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();



                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AllFeedBackActivity.this);
        requestQueue.add(request);
        progressDialog = new ProgressDialog(AllFeedBackActivity.this);
        progressDialog.setTitle("Loading!");
        progressDialog.setMessage("Please wait a while...");
        progressDialog.setCancelable(false);
        progressDialog.show();

    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
        navigationView.getMenu().getItem(4).setChecked(true);
    }

}
