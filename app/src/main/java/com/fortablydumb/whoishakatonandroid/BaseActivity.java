package com.fortablydumb.whoishakatonandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;

public class BaseActivity extends AppCompatActivity{
    private String currentLangCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentLangCode = getResources().getConfiguration().locale.getLanguage();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        PodesavanjaActivity.updateLanguage(this, preferences.getString("pismo", "lat"));
    }
    @Override
    public void onResume(){
        super.onResume();
        if(!currentLangCode.equals(getResources().getConfiguration().locale.getLanguage())){
            currentLangCode = getResources().getConfiguration().locale.getLanguage();
            recreate();
        }
    }
}