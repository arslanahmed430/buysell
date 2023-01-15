package com.arslans.buyandsell.Activites;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.arslans.buyandsell.R;
import com.arslans.buyandsell.Utils.SharedPrefs;
import com.otaliastudios.cameraview.filter.Filters;

public class Splash extends AppCompatActivity {
    public static int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                if(SharedPrefs.getUser()!=null){
                    Intent i = new Intent(Splash.this, MainActivity.class);
                    startActivity(i);
                }else{
                    Intent i = new Intent(Splash.this, Login.class);
                    startActivity(i);
                }

                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
