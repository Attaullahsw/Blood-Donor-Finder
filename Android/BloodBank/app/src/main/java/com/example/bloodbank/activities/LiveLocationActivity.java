package com.example.bloodbank.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bloodbank.R;
import com.example.bloodbank.dataholder.SearchRecordDataHolder;
import com.example.bloodbank.util.GPSTracker;
import com.example.bloodbank.util.InternetWarningDialog;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LiveLocationActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;

    ProgressDialog progressDialog;

    boolean doubleBackToExitPressedOnce = false;

    //////////////////////////////////////////////////////////////////
    GPSTracker Gps;
    String lat, lon;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    /////////////////////////////////////////////////////////////////

    LatLng mylocaton;

    Marker tempM[];

    int user_id[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_location);

        ////////////////////////////////////////////////////////////////////////////////////////////
        Gps = new GPSTracker(this);

        address();
        ////////////////////////////////////////////////////////////////////////////////////////////

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (lat.equals("0.0")){
            Toast.makeText(LiveLocationActivity.this,"Please Turn On Your Location First!",Toast.LENGTH_SHORT).show();
        }else {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }



            mylocaton = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
            Marker tempM = mMap.addMarker(new MarkerOptions().position(mylocaton).title("Your Location"));
            tempM.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocaton, 6.0f));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);


            // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,20));
//            // Zoom in, animating the camera.
//            mMap.animateCamera(CameraUpdateFactory.zoomIn());
//            // Zoom out to zoom level 10, animating with a duration of 2 seconds.
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

            getData();
            mMap.setOnMarkerClickListener(LiveLocationActivity.this);
        }

}


    public void getData() {

        String strings = getResources().getString(R.string.url) + "allliveuser.php";
        StringRequest request = new StringRequest(Request.Method.POST, strings, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    if (error) {
                        progressDialog.dismiss();
                        Toast.makeText(LiveLocationActivity.this, jsonObject.getString("msg").toString(), Toast.LENGTH_LONG).show();
                    } else {

                        JSONArray jsonArray = jsonObject.getJSONArray("fetchuser");
                        if(jsonArray.length()==0){

                        }else {

                        }

                        LatLng tempMark[] = new LatLng[jsonArray.length()];
                       user_id = new int[jsonArray.length()];
                         tempM = new Marker[jsonArray.length()];

                        for(int i=0; i<jsonArray.length();i++){


                            JSONObject user = jsonArray.getJSONObject(i);

                             tempMark[i] = new LatLng(Double.parseDouble( user.getString("location_lat")), Double.parseDouble(user.getString("location_lon")));
                             tempM[i] = mMap.addMarker(new MarkerOptions()
                                    .anchor(0.0f, 1.0f).snippet("last Active on: "+ user.getString("location_time")+" , "+user.getString("location_date"))
                                    .position(tempMark[i]).title(user.getString("user_name"))
                                    .visible(true)
                            );

                            tempM[i].setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                            user_id[i] = user.getInt("user_id");




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
                // Toast.makeText(SearchRecordActivity.this, "Connection Problem", Toast.LENGTH_LONG).show();
                InternetWarningDialog.showCustomDialog(LiveLocationActivity.this);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("lat", String.valueOf(lat));
                params.put("lon", String.valueOf(lon));
                params.put("livelocation", "done");

                int blood_id = getIntent().getIntExtra("blood_id",0);
                if(blood_id != 0){
                    params.put("blood_id", String.valueOf(blood_id));
                }




                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(LiveLocationActivity.this);
        requestQueue.add(request);
        progressDialog = new ProgressDialog(LiveLocationActivity.this);
        progressDialog.setTitle("Loading!");
        progressDialog.setMessage("Please wait a while...");
        progressDialog.show();

    }


    void address() {
        lat = String.valueOf(Gps.getLatitude());
        lon = String.valueOf(Gps.getLongitude());
        Log.d("mes", "address: " + lat + "   " + lon);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {


        if (doubleBackToExitPressedOnce) {
            for(int i = 0; i<tempM.length; i++){
                if(marker.getId().equals(tempM[i].getId())){
                    Intent intent = new Intent(LiveLocationActivity.this, ProfileActivity.class);
                    intent.putExtra("user_id",user_id[i]);
                    startActivity(intent);

                }
            }

        } else {
            this.doubleBackToExitPressedOnce = true;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }




        return false;
    }
}
