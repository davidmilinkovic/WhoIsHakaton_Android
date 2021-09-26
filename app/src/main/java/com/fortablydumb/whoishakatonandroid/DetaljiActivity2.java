package com.fortablydumb.whoishakatonandroid;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

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
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import android.os.Handler;
import android.os.Looper;
import android.provider.CalendarContract;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fortablydumb.whoishakatonandroid.databinding.ActivityDetalji2Binding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DetaljiActivity2 extends BaseActivity {

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
                    Snackbar.make(view, R.string.domenUklonjen, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else {
                    am.getDomenRepo().addToFavourites(d);
                    d.setOmiljeni(true);
                    Snackbar.make(view, R.string.dodatUOmiljene, Snackbar.LENGTH_LONG)
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
                builderSingle.setTitle(R.string.alarmIstekDomena);
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(DetaljiActivity2.this, android.R.layout.select_dialog_item);
                arrayAdapter.add(getString(R.string.emailObavestenje));
                arrayAdapter.add(getString(R.string.pushNotifikacija));

                builderSingle.setNegativeButton(R.string.zatvori, new DialogInterface.OnClickListener() {
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
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(DetaljiActivity2.this);
                            String email = preferences.getString("email", "");
                            if(email.length() == 0) {
                                new AlertDialog.Builder(DetaljiActivity2.this)
                                        .setTitle(R.string.emailAlarm)
                                        .setMessage(R.string.obavestenjeEmail)
                                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = new Intent(DetaljiActivity2.this, PodesavanjaActivity.class);
                                                startActivity(i);
                                            }
                                        })
                                        .show();
                                return;
                            }
                            else {
                                subscribe(email, null, naziv);
                            }
                        }
                        else {
                            // push
                            SharedPreferences sharedPref = getSharedPreferences(
                                    getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                            String fcmToken = sharedPref.getString("fcmToken", "");
                            if(fcmToken != null && fcmToken.length() > 0) {
                                subscribe(null, fcmToken, naziv);
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
                int recordCount = 0;

                for(int i = 0; i < objDns.names().length(); i++) {
                    String s = objDns.names().getString(i);

                    if(objDns.get(s) instanceof JSONArray) {
                        JSONArray arr = objDns.getJSONArray(s);
                        for(int j = 0; j < arr.length(); j++) {
                            dodajStavku(s.toUpperCase(Locale.ROOT), arr.getString(j), layoutDns);
                            recordCount++;
                        }
                    }
                    else {
                        dodajStavku(s.toUpperCase(Locale.ROOT), objDns.getString(s), layoutDns);
                        recordCount++;
                    }
                }
                if(recordCount == 0)
                    findViewById(R.id.cardDns).setVisibility(View.GONE);
            }
            else {
                findViewById(R.id.cardDns).setVisibility(View.GONE);
            }
            String osnovniPodaci[] = {"Domain Name", "Registry", "Registrar", "Registrant Name", "Registration Date", "Expiration Date", "Expires in"};
            String[] osnovniPodaciSrp;
            osnovniPodaciSrp = new String[]{getString(R.string.nazivDomena), getString(R.string.registar), getString(R.string.registrar), getString(R.string.registrant), getString(R.string.datumRegistracije), getString(R.string.datumIsteka), getString(R.string.isticeZa)};
            LinearLayout layoutOsnovniPodaci = findViewById(R.id.layoutOsnovniPodaci);

            Date exipration = null;
            SimpleDateFormat fIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if(obj.has("Expiration Date")) {
                exipration = fIn.parse(obj.getString("Expiration Date"));
            }
            for(int i = 0; i < osnovniPodaci.length; i++) {
                String s = osnovniPodaci[i];
                String sSrp = osnovniPodaciSrp[i];
                if (obj.has(s)) {
                    String value = obj.getString(s);

                    if(s.equals("Expires in")) {
                        value += " " + getString(R.string.dana);
                    }
                    else if(s.equals("Registration Date") || s.equals("Expiration Date")) {
                        SimpleDateFormat fOut = new SimpleDateFormat("dd.MM.yyyy. HH:mm:ss");
                        value = fOut.format(fIn.parse(value));
                    }

                    if((s.equals("Expires in") || s.equals("Expiration Date")) && exipration != null) {
                        Date finalExipration = exipration;
                        dodajStavku(sSrp, value, layoutOsnovniPodaci, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                pushAppointmentsToCalender(DetaljiActivity2.this, "Istek domena \""+naziv+"\"", finalExipration.getTime());
                            }
                        });
                    }
                    else {
                        dodajStavku(sSrp, value, layoutOsnovniPodaci);
                    }
                }
            }

            LinearLayout layoutSviPodaci = findViewById(R.id.layoutSviPodaci);
            for(int i = 0; i < obj.names().length(); i++) {
                String s = obj.names().getString(i);
                if(obj.optJSONObject(s) != null) continue;
                dodajStavku(s, obj.getString(s), layoutSviPodaci);
            }

            Map<String, String> engToSrp = new HashMap<>();
            engToSrp.put("Registrant", getString(R.string.registrant));
            engToSrp.put("registrant", getString(R.string.registrant));
            engToSrp.put("Address", getString(R.string.adresa));
            engToSrp.put("address", getString(R.string.adresa));
            engToSrp.put("Contact", getString(R.string.kontakt));
            engToSrp.put("contact", getString(R.string.kontakt));
            engToSrp.put("Postal Code", getString(R.string.postanski));
            engToSrp.put("Postal code", getString(R.string.postanski));
            engToSrp.put("postal code", getString(R.string.postanski));
            engToSrp.put("Country", getString(R.string.drzava));
            engToSrp.put("Organization", getString(R.string.organizacija));
            engToSrp.put("State/Province", getString(R.string.region));
            engToSrp.put("ID Number", getString(R.string.matBr));
            engToSrp.put("Tax ID", getString(R.string.pib));

            if(obj.has("Registrant") && obj.getJSONObject("Registrant").names() != null) {
                JSONObject obj2 = obj.getJSONObject("Registrant");
                LinearLayout ll = findViewById(R.id.layoutRegistrant);
                for(int i = 0; i < obj2.names().length(); i++) {
                    String key = obj2.names().getString(i);
                    String lokKey = key;
                    if(engToSrp.containsKey(key)) lokKey = engToSrp.get(key);
                    dodajStavku(lokKey, obj2.getString(key), ll);
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
                    String lokKey = key;
                    if(engToSrp.containsKey(key)) lokKey = engToSrp.get(key);
                    dodajStavku(lokKey, obj2.getString(key), ll);
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
                    String lokKey = key;
                    if(engToSrp.containsKey(key)) lokKey = engToSrp.get(key);
                    dodajStavku(lokKey, obj2.getString(key), ll);
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
        dodajStavku(naziv, vrednost, ll, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("vrednost", vrednost);
                clipboard.setPrimaryClip(clip);
                Snackbar.make(fab, "Tekst \""+vrednost+"\" kopiran!", Snackbar.LENGTH_LONG)
                        .show();
            }
        });
    }
    public static void pushAppointmentsToCalender(Activity curActivity,
                                                  String title, long date) {

        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", date);
        intent.putExtra("endTime", date);
            intent.putExtra(CalendarContract.Events.TITLE, title);
        curActivity.startActivity(intent);
    }

    private void dodajStavku(String naziv, String vrednost, LinearLayout ll, View.OnClickListener valueClick) {
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
        txtVrednost.setOnClickListener(valueClick);

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

    private void subscribe(String email, String fcmToken, String naziv) {
        RequestQueue queue = Volley.newRequestQueue(DetaljiActivity2.this);
        String url = getString(R.string.server);

        ProgressDialog pd = new ProgressDialog(DetaljiActivity2.this);
        pd.setMessage(getString(R.string.komunikacijaSaServerom));
        pd.setCancelable(false);
        pd.show();

        String query = "/subscribe?url=" + naziv;
        if(email != null) query += "&email="+email;
        else query += "&token=" + fcmToken;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + query,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.cancel();

                        try {
                            JSONObject obj = new JSONObject(response);
                            String status = obj.getString("status");
                            String poruka = status.equals("ok") ? getString(R.string.alarmUspesnoKreiran) : getString(R.string.alarmVecPostoji);
                            Snackbar.make(fab, poruka, Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            if(status.equals("ok")) {
                                if(!d.getOmiljeni()) {
                                    d.setOmiljeni(true);
                                    final Handler handler = new Handler(Looper.getMainLooper());
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Snackbar.make(fab, R.string.dodatUOmiljene, Snackbar.LENGTH_LONG)
                                                    .setAction("Action", null).show();
                                        }
                                    }, 2000);

                                    am.getDomenRepo().addToFavourites(d);
                                }
                                promenaBojeFab(fab);
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.cancel();
                Snackbar.make(fab, R.string.greskaVolley, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        queue.add(stringRequest);
    }
}