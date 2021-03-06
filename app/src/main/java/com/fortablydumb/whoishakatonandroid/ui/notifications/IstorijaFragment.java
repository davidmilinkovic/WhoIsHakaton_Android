package com.fortablydumb.whoishakatonandroid.ui.notifications;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
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
import com.fortablydumb.whoishakatonandroid.ui.dashboard.PretragaFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IstorijaFragment extends Fragment {
    private FragmentIstorijaBinding binding;
    private RecyclerView rvIstorija;
    private DomenListAdapter listAdapter;
    private AppModule am;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentIstorijaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        am = AppModule.getInstance(getActivity().getApplication());

        rvIstorija = binding.rvIstorija;
        listAdapter = new DomenListAdapter(new ArrayList<>(), getActivity()) {
            @Override
            public void itemKliknut(String naziv) {
                PretragaFragment.pretraga(getActivity(), naziv, am);
            }

            @Override
            public void DodajUOmiljene(Domen d, int position) {
                am.getDomenRepo().addToFavourites(d);
                listAdapter.setAtPosition(position, am.getDomenRepo().getDomen(d.getNaziv()));
            }

            @Override
            public void IzbaciIzOmiljenih(Domen d, int position) {
                return;
            }

            @Override
            public void Izbrisi(Domen d, int position) {
                am.getDomenRepo().deleteDomen(d);
                listAdapter.deleteAtPosition(position);

            }
        };

        rvIstorija.setAdapter(listAdapter);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeCallbackIstorija(listAdapter));
        itemTouchHelper.attachToRecyclerView(rvIstorija);

        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvIstorija.getContext(),
                lm.getOrientation());
        rvIstorija.addItemDecoration(dividerItemDecoration);
        rvIstorija.setLayoutManager(lm);

        List<Domen> istorija = am.getDomenRepo().getAll();

        listAdapter.setLocalDataSet(istorija);

        return root;
    }

    @Override
    public void onResume() {
        List<Domen> istorija = am.getDomenRepo().getAll();
        listAdapter.setLocalDataSet(istorija);
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}