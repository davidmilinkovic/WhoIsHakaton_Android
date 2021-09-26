package com.fortablydumb.whoishakatonandroid;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.fortablydumb.whoishakatonandroid.databinding.ActivityGlavniBinding;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.util.Date;

public class GlavniActivity extends BaseActivity {

    private ActivityGlavniBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGlavniBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_istorija, R.id.navigation_pretraga, R.id.navigation_omiljeni)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_glavni);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(GlavniActivity.this, "Nece token", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String token = task.getResult();

                        SharedPreferences sharedPref = getSharedPreferences(
                                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                        String stariToken = sharedPref.getString("fcmToken", "");
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("fcmToken", token);
                        editor.apply();

                        if(stariToken.length() > 0 && !stariToken.equals(token)) {
                            osveziToken(stariToken, token);
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.glavni_activity_menu, menu);
        return true;
    }

    private void osveziToken(String oldToken, String newToken) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.server);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + "/change-token?old=" + oldToken + "&new=" + newToken,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menuSettings) {
            Intent i = new Intent(GlavniActivity.this, PodesavanjaActivity.class);
            startActivity(i);
        }

        return true;
    }

}