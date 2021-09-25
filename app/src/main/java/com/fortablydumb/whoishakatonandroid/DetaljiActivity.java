package com.fortablydumb.whoishakatonandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DetaljiActivity extends AppCompatActivity {

    AppModule am;
    Domen d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalji);

        am = AppModule.getInstance(getApplication());
        d = am.getDomenRepo().getDomen(getIntent().getStringExtra("naziv"));
        ((TextView)findViewById(R.id.textView)).setText(d.getRegistar());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.up_nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.e("Detalji", "id: " + id);
        if(id == R.id.action_favorite) {
            boolean favorite = d.getOmiljeni();
            if(favorite) {
                am.getDomenRepo().removeFromFavourites(d);
            }
            else {
                am.getDomenRepo().addToFavourites(d);
            }
            Log.d("Detalji", "favorites"+favorite);
            d.setOmiljeni(!favorite);
        }
        if(id == R.id.action_alarm) {
            //TODO Alarm
        }
        return true;
    }
}