package com.samast.androidapptest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class Splashscreen extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splashscreen);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("user_info", 0);

        Boolean isLoggedIn = preferences.getBoolean(getString(R.string.isLoggedIn), false);


        if(isLoggedIn) {
            //checking if user is already logged in
            Intent intent = new Intent(getApplicationContext(), HomeScreen.class);

            startActivity(intent);

            return;
        }
        else{                         //if not we send him to candidate/recruiter screen

            Intent intent = new Intent(getApplicationContext(), LoginScreen.class);

            startActivity(intent);

            finish();

            return;
        }


    }
}
