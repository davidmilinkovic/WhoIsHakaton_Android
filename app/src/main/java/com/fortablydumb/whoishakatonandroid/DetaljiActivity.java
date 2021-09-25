package com.fortablydumb.whoishakatonandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class DetaljiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalji);

        AppModule am = AppModule.getInstance(getApplication());
        Domen d = am.getDomenRepo().getDomen(getIntent().getStringExtra("naziv"));
        ((TextView)findViewById(R.id.textView)).setText(d.getRegistar());
    }
}