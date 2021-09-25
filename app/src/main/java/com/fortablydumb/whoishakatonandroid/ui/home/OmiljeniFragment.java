package com.fortablydumb.whoishakatonandroid.ui.home;

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

import com.fortablydumb.whoishakatonandroid.AppModule;
import com.fortablydumb.whoishakatonandroid.Domen;
import com.fortablydumb.whoishakatonandroid.databinding.FragmentIstorijaBinding;
import com.fortablydumb.whoishakatonandroid.databinding.FragmentOmiljeniBinding;
import com.fortablydumb.whoishakatonandroid.ui.notifications.DomenListAdapter;
import com.fortablydumb.whoishakatonandroid.ui.notifications.SwipeCallbackIstorija;

import java.util.ArrayList;
import java.util.List;

public class OmiljeniFragment extends Fragment {

    private OmiljeniViewModel homeViewModel;
    private FragmentOmiljeniBinding binding;
    private RecyclerView rvOmiljeni;
    private DomenListAdapter listAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOmiljeniBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        AppModule am = AppModule.getInstance(getActivity().getApplication());

        rvOmiljeni = binding.rvOmiljeni;
        listAdapter = new DomenListAdapter(new ArrayList<>(), getActivity()) {
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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}