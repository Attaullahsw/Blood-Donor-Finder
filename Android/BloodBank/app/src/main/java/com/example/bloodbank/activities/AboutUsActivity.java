package com.example.bloodbank.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.bloodbank.R;

public class AboutUsActivity extends AppCompatActivity {

    private TextView mTextMessage;
    SharedPreferences logPre;
    SharedPreferences.Editor preEditor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        logPre = getSharedPreferences("UserLogin", MODE_PRIVATE);
        preEditor = logPre.edit();

        mTextMessage = (TextView) findViewById(R.id.message);

    }



}
