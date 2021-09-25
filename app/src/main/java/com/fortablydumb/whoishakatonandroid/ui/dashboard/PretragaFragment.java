package com.fortablydumb.whoishakatonandroid.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fortablydumb.whoishakatonandroid.AppModule;
import com.fortablydumb.whoishakatonandroid.DetaljiActivity;
import com.fortablydumb.whoishakatonandroid.DetaljiActivity2;
import com.fortablydumb.whoishakatonandroid.Domen;
import com.fortablydumb.whoishakatonandroid.DomenWebservice;
import com.fortablydumb.whoishakatonandroid.R;
import com.fortablydumb.whoishakatonandroid.databinding.FragmentPretragaBinding;

import org.json.JSONException;
import org.json.JSONObject;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;
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

                String naziv = textViewSuggestions.getText().toString();

                RequestQueue queue = Volley.newRequestQueue(getActivity());
                String url = getString(R.string.pajinKomp);

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url + "/data?url=" + naziv,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Domen d = am.getDomenRepo().getDomen(naziv);
                                if(d != null) {
                                    am.getDomenRepo().refreshDomen(new Domen(naziv, d.getOmiljeni(), new Date(), response));
                                }
                                else {
                                    am.getDomenRepo().insertDomen(new Domen(naziv, false, new Date(), response));
                                }
                                Intent i = new Intent(getActivity(), DetaljiActivity2.class);
                                i.putExtra("naziv", naziv);
                                startActivity(i);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Voley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                queue.add(stringRequest);
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
}