package com.fortablydumb.whoishakatonandroid.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fortablydumb.whoishakatonandroid.AppModule;
import com.fortablydumb.whoishakatonandroid.Domen;
import com.fortablydumb.whoishakatonandroid.databinding.FragmentIstorijaBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class IstorijaFragment extends Fragment {
    private FragmentIstorijaBinding binding;
    private RecyclerView rvIstorija;
    private DomenListAdapter listAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentIstorijaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        AppModule am = AppModule.getInstance(getActivity().getApplication());

        rvIstorija = binding.rvIstorija;
        listAdapter = new DomenListAdapter(new ArrayList<>(), getActivity()) {
            @Override
            public void DodajUOmiljene(Domen d) {
                am.getDomenRepo().addToFavourites(d);
                listAdapter.setLocalDataSet(am.getDomenRepo().getAll());
            }

            @Override
            public void IzbaciIzOmiljenih(Domen d) {
                return;
            }

            @Override
            public void Izbrisi(Domen d) {
                am.getDomenRepo().deleteDomen(d);
                listAdapter.setLocalDataSet(am.getDomenRepo().getAll());
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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}