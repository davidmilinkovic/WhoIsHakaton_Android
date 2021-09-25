package com.fortablydumb.whoishakatonandroid.ui.notifications;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.fortablydumb.whoishakatonandroid.Domen;
import com.fortablydumb.whoishakatonandroid.R;

import java.util.List;

public abstract class DomenListAdapter extends RecyclerView.Adapter<DomenListAdapter.ViewHolder> {

    private final Context context;
    private List<Domen> localDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtDomen;
        private final TextView txtPoslednjaPretraga;

        public ImageView getImgFav() {
            return imgFav;
        }

        private final ImageView imgFav;

        public ViewHolder(View view) {
            super(view);
            txtDomen = (TextView) view.findViewById(R.id.txtDomen);
            txtPoslednjaPretraga = (TextView) view.findViewById(R.id.txtPoslednjaPretraga);
            imgFav = (ImageView)view.findViewById(R.id.imgFav);
        }

        public TextView getTxtDomen() {
            return txtDomen;
        }

        public TextView getTxtPoslednjaPretraga() {
            return txtPoslednjaPretraga;
        }
    }

    public DomenListAdapter(List<Domen> dataSet, Context context) {
        localDataSet = dataSet;
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.domen_list_item, viewGroup, false);

        return new ViewHolder(view);
    }

    public void setLocalDataSet(List<Domen> domeni) {
        localDataSet = domeni;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Domen d = localDataSet.get(position);
        viewHolder.getTxtDomen().setText(d.getNaziv());
        if(d.getPoslednjiPutPretrazivan() != null) {
            viewHolder.getTxtPoslednjaPretraga().setText(d.getPoslednjiPutPretrazivan().toString());
        }
        viewHolder.getImgFav().setVisibility(d.getOmiljeni() ? View.VISIBLE : View.GONE);
    }

    public void toastItem(int position) {
        Toast.makeText(context, localDataSet.get(position).getNaziv(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public void DodajUOmiljeneAt(int position) {
        DodajUOmiljene(localDataSet.get(position));
    }
    public void IzbaciIzOmiljenihAt(int position) {
        IzbaciIzOmiljenih(localDataSet.get(position));
    }

    public void IzbrisiAt(int position) {
        Izbrisi(localDataSet.get(position));
    }

    public abstract void DodajUOmiljene(Domen d);
    public abstract void IzbaciIzOmiljenih(Domen d);
    public abstract void Izbrisi(Domen d);
}
