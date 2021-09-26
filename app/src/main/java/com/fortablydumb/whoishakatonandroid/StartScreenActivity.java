package com.fortablydumb.whoishakatonandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class StartScreenActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 1200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent = new Intent(StartScreenActivity.this,GlavniActivity.class);
                StartScreenActivity.this.startActivity(mainIntent);
                StartScreenActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}