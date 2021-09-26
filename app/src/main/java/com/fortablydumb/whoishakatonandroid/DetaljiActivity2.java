package com.fortablydumb.whoishakatonandroid;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fortablydumb.whoishakatonandroid.databinding.ActivityDetalji2Binding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

public class DetaljiActivity2 extends AppCompatActivity {

    private ActivityDetalji2Binding binding;
    private AppModule am;
    private Domen d;
    private FloatingActionButton fab;
    private FloatingActionButton fabAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetalji2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String naziv = getIntent().getStringExtra("naziv");

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(naziv);

        if(!getIntent().hasExtra("kesirano"))
            findViewById(R.id.layoutKesirano).setVisibility(View.GONE);

        fab = binding.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(d.getOmiljeni()) {
                    am.getDomenRepo().removeFromFavourites(d);
                    d.setOmiljeni(false);
                    Snackbar.make(view, "Domen uklonjen iz omiljenih!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else {
                    am.getDomenRepo().addToFavourites(d);
                    d.setOmiljeni(true);
                    Snackbar.make(view, "Domen dodat u omiljene!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                promenaBojeFab(fab);
            }
        });

        fabAlarm = binding.fabAlarm;
        fabAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(DetaljiActivity2.this);
                builderSingle.setTitle("Alarm za istek domena");
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(DetaljiActivity2.this, android.R.layout.select_dialog_item);
                arrayAdapter.add("E-mail obaveštenje");
                arrayAdapter.add("Push notifikacija");

                builderSingle.setNegativeButton("Zatvori", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0) {
                            // email
                        }
                        else {
                            // push
                            SharedPreferences sharedPref = getSharedPreferences(
                                    getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                            String fcmToken = sharedPref.getString("fcmToken", "");
                            if(fcmToken != null && fcmToken.length() > 0) {
                                RequestQueue queue = Volley.newRequestQueue(DetaljiActivity2.this);
                                String url = getString(R.string.pajinKomp);

                                ProgressDialog pd = new ProgressDialog(DetaljiActivity2.this);
                                pd.setMessage("Komunikacija sa serverom...");
                                pd.show();

                                StringRequest stringRequest = new StringRequest(Request.Method.GET, url + "/subscribe?token=" + fcmToken + "&url=" + naziv,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                pd.cancel();

                                                try {
                                                    JSONObject obj = new JSONObject(response);
                                                    AlertDialog.Builder builderInner = new AlertDialog.Builder(DetaljiActivity2.this);
                                                    builderInner.setMessage(obj.getString("status").equals("ok") ? "Uspešno ste kreirali alarm za domen!" : "Alarm za ovaj domen je već kreiran.");
                                                    builderInner.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog,int which) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                                    builderInner.show();
                                                }
                                                catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        pd.cancel();
                                        Toast.makeText(DetaljiActivity2.this, "Greška pri preuzimanju podataka.", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                queue.add(stringRequest);
                            }
                        }
                    }
                });

                builderSingle.show();
            }
        });

        am = AppModule.getInstance(getApplication());
        d = am.getDomenRepo().getDomen(naziv);

        promenaBojeFab(fab);

        try {
            JSONObject obj = new JSONObject(d.getRawData());


            if(obj.has("dns")) {
                JSONObject objDns = obj.getJSONObject("dns");

                LinearLayout layoutDns = findViewById(R.id.layoutDns);

                for(int i = 0; i < objDns.names().length(); i++) {
                    String s = objDns.names().getString(i);

                    if(objDns.get(s) instanceof JSONArray) {
                        JSONArray arr = objDns.getJSONArray(s);
                        for(int j = 0; j < arr.length(); j++) {
                            dodajStavku(s.toUpperCase(Locale.ROOT), arr.getString(j), layoutDns);
                        }
                    }
                    else {
                        dodajStavku(s.toUpperCase(Locale.ROOT), objDns.getString(s), layoutDns);
                    }
                }
            }
            else {
                findViewById(R.id.cardDns).setVisibility(View.GONE);
            }
            String osnovniPodaci[] = {"Domain Name", "Registry", "Registrar", "Registrant Name", "Registration Date", "Expiration Date"};
            String osnovniPodaciSrp[] = {"Naziv domena", "Registar", "Registrar", "Registrant", "Datum registracije", "Datum isteka"};
            LinearLayout layoutOsnovniPodaci = findViewById(R.id.layoutOsnovniPodaci);
            for(int i = 0; i < osnovniPodaci.length; i++) {
                String s = osnovniPodaci[i];
                String sSrp = osnovniPodaciSrp[i];
                if (obj.has(s)) {
                    dodajStavku(sSrp, obj.getString(s), layoutOsnovniPodaci);
                }
            }

            LinearLayout layoutSviPodaci = findViewById(R.id.layoutSviPodaci);
            for(int i = 0; i < obj.names().length(); i++) {
                String s = obj.names().getString(i);
                if(obj.optJSONObject(s) != null) continue;
                dodajStavku(s, obj.getString(s), layoutSviPodaci);
            }


            if(obj.has("Registrant") && obj.getJSONObject("Registrant").names() != null) {
                JSONObject obj2 = obj.getJSONObject("Registrant");
                LinearLayout ll = findViewById(R.id.layoutRegistrant);
                for(int i = 0; i < obj2.names().length(); i++) {
                    String key = obj2.names().getString(i);
                    dodajStavku(key, obj2.getString(key), ll);
                }
            }
            else {
                findViewById(R.id.cardRegistrant).setVisibility(View.GONE);
            }

            if(obj.has("Admin") && obj.getJSONObject("Admin").names() != null) {
                JSONObject obj2 = obj.getJSONObject("Admin");
                LinearLayout ll = findViewById(R.id.layoutAdmin);
                for(int i = 0; i < obj2.names().length(); i++) {
                    String key = obj2.names().getString(i);
                    dodajStavku(key, obj2.getString(key), ll);
                }
            }
            else {
                findViewById(R.id.cardAdmin).setVisibility(View.GONE);
            }

            if(obj.has("Tech") && obj.getJSONObject("Tech").names() != null) {
                JSONObject obj2 = obj.getJSONObject("Tech");
                LinearLayout ll = findViewById(R.id.layoutTech);
                for(int i = 0; i < obj2.names().length(); i++) {
                    String key = obj2.names().getString(i);
                    dodajStavku(key, obj2.getString(key), ll);
                }
            }
            else {
                findViewById(R.id.cardTech).setVisibility(View.GONE);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void dodajStavku(String naziv, String vrednost, LinearLayout ll) {
        if(vrednost.trim().length() == 0) return;
        LinearLayout l = new LinearLayout(this);
        l.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        TextView txtNaziv = new TextView(this);
        txtNaziv.setText(naziv + ":");
        txtNaziv.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        TextView txtVrednost = new TextView(this);
        txtVrednost.setText(vrednost);
        txtVrednost.setGravity(Gravity.RIGHT);
        txtVrednost.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        TypedValue tv = new TypedValue();
        Resources.Theme theme = getTheme();
        theme.resolveAttribute(R.attr.textAppearanceHeadline1, tv, true);
        txtVrednost.setTextColor(tv.data);
        txtVrednost.setClickable(true);
        txtVrednost.setFocusable(true);
        TypedValue outValue = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        txtVrednost .setBackgroundResource(outValue.resourceId);
        txtVrednost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("vrednost", vrednost);
                clipboard.setPrimaryClip(clip);
                Snackbar.make(fab, "Tekst \""+vrednost+"\" kopiran!", Snackbar.LENGTH_LONG)
                        .show();
            }
        });

        l.addView(txtNaziv);
        l.addView(txtVrednost);
        l.setPadding(8, 8, 8, 8);

        ll.addView(l);
    }

    private void promenaBojeFab(FloatingActionButton fab) {
        if(d.getOmiljeni()) {
            fab.setSupportBackgroundTintList(ContextCompat.getColorStateList(this, R.color.fav));
        }
        else {
            fab.setSupportBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return  true;
    }
}