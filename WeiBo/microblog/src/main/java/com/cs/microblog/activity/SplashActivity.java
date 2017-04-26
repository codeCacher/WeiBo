package com.cs.microblog.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cs.microblog.R;

import java.util.Timer;
import java.util.TimerTask;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //waite this activity to show a few seconds,start the MainActivity and finish it
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                MainActivity.openMainActivity(getApplicationContext(), Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
            }
        };
        timer.schedule(task,2000);
    }
}
