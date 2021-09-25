package com.fortablydumb.whoishakatonandroid;

import android.content.res.Resources;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fortablydumb.whoishakatonandroid.databinding.ActivityDetalji2Binding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class DetaljiActivity2 extends AppCompatActivity {

    private ActivityDetalji2Binding binding;
    private AppModule am;
    private Domen d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetalji2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());

        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        String naziv = getIntent().getStringExtra("naziv");

        ((Toolbar)findViewById(R.id.toolbar)).setTitle(naziv);

        am = AppModule.getInstance(getApplication());
        d = am.getDomenRepo().getDomen(naziv);

        try {
            JSONObject obj = new JSONObject(d.getRawData());
            String osnovniPodaci[] = {"Domain Name", "Registrar", "Registration Date", "Expiration date"};
            String osnovniPodaciSrp[] = {"Naziv domena", "Registrar", "Datum registracije", "Datum isteka"};

            LinearLayout layoutOsnovniPodaci = findViewById(R.id.layoutOsnovniPodaci);
            for(int i = 0; i < osnovniPodaci.length; i++) {
                String s = osnovniPodaci[i];
                String sSrp = osnovniPodaciSrp[i];
                if(obj.has(s)) {
                    LinearLayout l = new LinearLayout(this);
                    l.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));

                    TextView txtNaziv = new TextView(this);
                    txtNaziv.setText(sSrp + ":");
                    txtNaziv.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));

                    TextView txtVrednost = new TextView(this);
                    txtVrednost.setText(obj.getString(s));
                    txtVrednost.setGravity(Gravity.RIGHT);
                    txtVrednost.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));

                    TypedValue tv = new TypedValue();
                    Resources.Theme theme = getTheme();
                    theme.resolveAttribute(R.attr.textAppearanceHeadline1, tv, true);
                    txtVrednost.setTextColor(tv.data);

                    l.addView(txtNaziv);
                    l.addView(txtVrednost);
                    l.setPadding(8, 8, 8, 8);

                    layoutOsnovniPodaci.addView(l);
                }

                LinearLayout layoutSviPodaci = findViewById(R.id.layoutSviPodaci);

                Iterator<String> iter = obj.keys();
                while (iter.hasNext()) {
                    s = iter.next();
                    LinearLayout l = new LinearLayout(this);
                    l.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));

                    TextView txtNaziv = new TextView(this);
                    txtNaziv.setText(s + ":");
                    txtNaziv.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));

                    TextView txtVrednost = new TextView(this);
                    txtVrednost.setText(obj.getString(s));
                    txtVrednost.setGravity(Gravity.RIGHT);
                    txtVrednost.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));

                    TypedValue tv = new TypedValue();
                    Resources.Theme theme = getTheme();
                    theme.resolveAttribute(R.attr.textAppearanceHeadline1, tv, true);
                    txtVrednost.setTextColor(tv.data);

                    l.addView(txtNaziv);
                    l.addView(txtVrednost);
                    l.setPadding(8, 8, 8, 8);

                    layoutSviPodaci.addView(l);
                }

            }

        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}