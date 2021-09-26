package com.fortablydumb.whoishakatonandroid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.util.Locale;

public class PodesavanjaActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.title_activity_podesavanja));
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            Preference pref = findPreference("ponistiPush");
            pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    SharedPreferences sharedPref = getActivity().getSharedPreferences(
                            getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                    String token = sharedPref.getString("fcmToken", "");

                    RequestQueue queue = Volley.newRequestQueue(getActivity());
                    String url = getString(R.string.server);

                    ProgressDialog pd = new ProgressDialog(getActivity());
                    pd.setMessage(getString(R.string.komunikacijaSaServerom));
                    pd.setCancelable(false);
                    pd.show();

                    String query = "/unsubscribe-push?token=" + token;

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url + query,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    pd.cancel();
                                    Toast.makeText(getActivity(), R.string.uspesnoPush, Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pd.cancel();
                            Toast.makeText(getActivity(), getString(R.string.greskaVolley), Toast.LENGTH_SHORT).show();
                        }
                    });

                    queue.add(stringRequest);
                    
                    return false;
                }
            });

            ListPreference prefPismo = findPreference("pismo");
            prefPismo.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    updateLanguage(getActivity(), newValue.toString());
                    Intent intent = getActivity().getIntent();
                    getActivity().finish();
                    getActivity().startActivity(intent);
                    return true;
                }
            });
        }
    }
    public static void updateLanguage(Context context, String selectedLanguage) {
        if (!"".equals(selectedLanguage)) {
            if ("lat".equals(selectedLanguage)) {
                selectedLanguage = "";
            } else if ("cir".equals(selectedLanguage)) {
                selectedLanguage = "sr";
            }
            Locale locale = new Locale(selectedLanguage);
            Locale.setDefault(locale);
            Configuration config = context.getResources().getConfiguration();
            config.locale = locale;
            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        }
    }
}