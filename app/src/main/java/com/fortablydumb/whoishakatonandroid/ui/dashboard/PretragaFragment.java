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
import com.fortablydumb.whoishakatonandroid.Domen;
import com.fortablydumb.whoishakatonandroid.DomenWebservice;
import com.fortablydumb.whoishakatonandroid.R;
import com.fortablydumb.whoishakatonandroid.databinding.FragmentPretragaBinding;

import org.json.JSONException;
import org.json.JSONObject;

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

        binding.btnPretraga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String naziv = binding.editProba.getText().toString();

                RequestQueue queue = Volley.newRequestQueue(getActivity());
                String url = getString(R.string.pajinKomp);

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url + "/data?url=" + naziv,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    Domen d = Domen.FromJSONObject(new JSONObject(response));
                                    Toast.makeText(getActivity(), d.getNaziv() + " " + d.getRegistar(), Toast.LENGTH_SHORT).show();
                                    am.getDomenRepo().refreshDomen(d);
                                    Intent i = new Intent(getActivity(), DetaljiActivity.class);
                                    i.putExtra("naziv", naziv);
                                    startActivity(i);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
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

        final TextView textView = binding.textViewProba;
        pretragaVm.getDomen().observe(getViewLifecycleOwner(), new Observer<Domen>() {
            @Override
            public void onChanged(@Nullable Domen d) {
                if(d != null) {
                    textView.setText(d.getRegistar());
                }
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}