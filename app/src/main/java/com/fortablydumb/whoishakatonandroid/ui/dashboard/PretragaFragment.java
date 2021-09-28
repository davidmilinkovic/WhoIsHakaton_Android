package com.fortablydumb.whoishakatonandroid.ui.dashboard;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fortablydumb.whoishakatonandroid.AppModule;
import com.fortablydumb.whoishakatonandroid.DetaljiActivity2;
import com.fortablydumb.whoishakatonandroid.Domen;
import com.fortablydumb.whoishakatonandroid.R;
import com.fortablydumb.whoishakatonandroid.databinding.FragmentPretragaBinding;

import org.json.JSONObject;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.Date;
import java.util.List;

public class PretragaFragment extends Fragment {

    private PretragaViewModel pretragaVm;
    private FragmentPretragaBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        pretragaVm =
                new ViewModelProvider(this).get(PretragaViewModel.class);

        binding = FragmentPretragaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        AppModule am = AppModule.getInstance(getActivity().getApplication());

        AutoCompleteTextView textViewSuggestions = (AutoCompleteTextView) binding.autocompleteTextView;

        String [] listaPretrage = this.vratiListuPretrage(am);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listaPretrage);
        textViewSuggestions.setAdapter(adapter);

        binding.btnPretraga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String naziv = textViewSuggestions.getText().toString().trim();
                pretraga(getActivity(), naziv, am);
            }
        });


        return root;
    }

    String[] vratiListuPretrage(AppModule am){
        List<Domen> lista = am.getDomenRepo().getAll();
        String[] returnLista = new String[lista.size()];
        for(int i = 0; i < lista.size(); i++) {
            returnLista[i] = lista.get(i).getNaziv();
        }
        return returnLista;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public static void pretraga(Context c, String naziv, AppModule am) {
        RequestQueue queue = Volley.newRequestQueue(c);
        String url = c.getString(R.string.server);

        ProgressDialog pd = new ProgressDialog(c);
        pd.setMessage(c.getString(R.string.komunikacijaSaServerom));
        pd.setCancelable(false);
        pd.show();

        Domen d = am.getDomenRepo().getDomen(naziv);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + "/data?url=" + naziv,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.cancel();

                        try {
                            JSONObject obj = new JSONObject(response);
                            String status =obj.getString("status");
                            if(status.equals("active")) {
                                if(d != null)
                                    am.getDomenRepo().refreshDomen(new Domen(naziv, d.getOmiljeni(), new Date(), response));
                                else
                                    am.getDomenRepo().insertDomen(new Domen(naziv, false, new Date(), response));
                                Intent i = new Intent(c, DetaljiActivity2.class);
                                i.putExtra("naziv", naziv);
                                c.startActivity(i);
                            }
                            else if(status.equals("free")) {
                                Toast.makeText(c, "Domen je slobodan!", Toast.LENGTH_SHORT).show();
                            }
                            else if(status.equals("unknown")) {
                                Toast.makeText(c, "Uneti format domena nije podržan!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.cancel();
                if(d != null) {
                    Intent i = new Intent(c, DetaljiActivity2.class);
                    i.putExtra("naziv", naziv);
                    i.putExtra("kesirano", true);
                    c.startActivity(i);
                }
                else {
                    Toast.makeText(c, "Greška pri preuzimanju podataka.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        queue.add(stringRequest);
    }
}