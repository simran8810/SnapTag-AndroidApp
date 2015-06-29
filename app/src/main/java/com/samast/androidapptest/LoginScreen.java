package com.samast.androidapptest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by ht on 28/6/15.
 */
public class LoginScreen extends Activity {

    private Button loginButton;
    private EditText userName, userNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }


        loginButton= (Button) findViewById(R.id.login);
        userName = (EditText) findViewById(R.id.login_name);
        userNumber = (EditText) findViewById(R.id.login_number);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                    if(validate()){
                        authUser();
                    }

                }

                else{
                    buildAlertMessageNoGps();
                }

            }
        });


    }

    /**
     * Validation check for login fields
     */

    private boolean validate(){

        String MobilePattern = "[0-9]{10}";
        String name = String.valueOf(userName.getText());
        String number = String.valueOf(userNumber.getText());


        if(name.length() > 25){
            Toast.makeText(getApplicationContext(), "pls enter less the 25 characher in user name",
                    Toast.LENGTH_SHORT).show();
            return true;
        }



        else if(name.length() == 0 || number.length() == 0 ){
            Toast.makeText(getApplicationContext(), "pls fill the empty fields",
                    Toast.LENGTH_SHORT).show();
            return false;
        }



        else if(number.matches(MobilePattern))
        {

            return true;
        }
        else if(!number.matches(MobilePattern))
        {
            Toast.makeText(getApplicationContext(), "Please enter valid 10" +
                    "digit phone number", Toast.LENGTH_SHORT).show();


            return false;
        }

        return false;

    }

    /**
     * authenticate user after validation
     */
    public void authUser(){

        String emptyString = "";
        if(!(String.valueOf(userName.getText()).equals(emptyString) ||
                String.valueOf(userNumber.getText()).equals(emptyString))){

            SharedPreferences preferences = getApplicationContext().getSharedPreferences("user_info", 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(getResources().getString(R.string.isLoggedIn), true);
            editor.putString(getResources().getString(R.string.userName), String.valueOf(userName.getText()));
            editor.putString(getResources().getString(R.string.userNumber), String.valueOf(userNumber.getText()));
            editor.apply();

            Intent intent = new Intent(getApplicationContext(), HomeScreen.class);

            startActivity(intent);
        }
        else{
            Toast.makeText(getApplicationContext(),
                    "Please enter compelete credentials..", Toast.LENGTH_SHORT)
                    .show();
        }

    }



    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, please enable it!")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

}


