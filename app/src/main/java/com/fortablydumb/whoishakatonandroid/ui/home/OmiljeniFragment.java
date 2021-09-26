package com.fortablydumb.whoishakatonandroid.ui.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.fortablydumb.whoishakatonandroid.databinding.FragmentIstorijaBinding;
import com.fortablydumb.whoishakatonandroid.databinding.FragmentOmiljeniBinding;
import com.fortablydumb.whoishakatonandroid.ui.notifications.DomenListAdapter;
import com.fortablydumb.whoishakatonandroid.ui.notifications.SwipeCallbackIstorija;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OmiljeniFragment extends Fragment {

    private OmiljeniViewModel homeViewModel;
    private FragmentOmiljeniBinding binding;
    private RecyclerView rvOmiljeni;
    private DomenListAdapter listAdapter;
    private AppModule am;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOmiljeniBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        am = AppModule.getInstance(getActivity().getApplication());

        rvOmiljeni = binding.rvOmiljeni;
        listAdapter = new DomenListAdapter(new ArrayList<>(), getActivity()) {
            @Override
            public void itemKliknut(String naziv) {
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                String url = getString(R.string.pajinKomp);

                ProgressDialog pd = new ProgressDialog(getActivity());
                pd.setMessage("Komunikacija sa serverom...");
                pd.show();

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url + "/data?url=" + naziv,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                pd.cancel();
                                Domen d = am.getDomenRepo().getDomen(naziv);
                                am.getDomenRepo().refreshDomen(new Domen(naziv, d.getOmiljeni(), new Date(), response));
                                Intent i = new Intent(getActivity(), DetaljiActivity2.class);
                                i.putExtra("naziv", naziv);
                                startActivity(i);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.cancel();
                        Intent i = new Intent(getActivity(), DetaljiActivity2.class);
                        i.putExtra("naziv", naziv);
                        i.putExtra("kesirano", true);
                        startActivity(i);
                    }
                });

                queue.add(stringRequest);
            }

            @Override
            public void DodajUOmiljene(Domen d, int position) {
                return;
            }

            @Override
            public void IzbaciIzOmiljenih(Domen d, int position) {
                am.getDomenRepo().removeFromFavourites(d);
                listAdapter.deleteAtPosition(position);
            }

            @Override
            public void Izbrisi(Domen d, int position) {
                return;
            }
        };

        rvOmiljeni.setAdapter(listAdapter);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeCallbackOmiljeni(listAdapter));
        itemTouchHelper.attachToRecyclerView(rvOmiljeni);

        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvOmiljeni.getContext(),
                lm.getOrientation());
        rvOmiljeni.addItemDecoration(dividerItemDecoration);
        rvOmiljeni.setLayoutManager(lm);

        List<Domen> istorija = am.getDomenRepo().getFavourites();
        listAdapter.setLocalDataSet(istorija);

        return root;
    }

    @Override
    public void onResume() {
        List<Domen> istorija = am.getDomenRepo().getFavourites();
        listAdapter.setLocalDataSet(istorija);
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}