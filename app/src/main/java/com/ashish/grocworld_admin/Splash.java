package com.ashish.grocworld_admin;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import common.AppController;


/**
 * Created by ashish.kumar on 03-07-2018.
 */

public class Splash extends Activity {

    AppController controller;

    private static int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        controller = (AppController) getApplicationContext();
        setContentView(R.layout.splash);

        runThread();
    }

    public void runThread()


    {

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                launchHomeScreen();
            }
        }, SPLASH_TIME_OUT);
    }

    public void launchHomeScreen() {
        Intent i;
        if (controller.isUserLoggedIn() == true) {
            i = new Intent(Splash.this, DashBoard.class);

        } else {
            i = new Intent(Splash.this, Login.class);
        }
        startActivity(i);
        finish();
    }

}
