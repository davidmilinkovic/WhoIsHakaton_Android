package com.fortablydumb.whoishakatonandroid.ui.notifications;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.fortablydumb.whoishakatonandroid.R;

public class SwipeCallbackIstorija extends ItemTouchHelper.SimpleCallback {
    private DomenListAdapter mAdapter;

    private Drawable icon;
    private final ColorDrawable background;

    public SwipeCallbackIstorija(DomenListAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mAdapter = adapter;
        icon = ContextCompat.getDrawable(mAdapter.getContext(),
                R.drawable.ic_twotone_favorite_24);
        background = new ColorDrawable(Color.RED);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        if(direction == ItemTouchHelper.LEFT) {
            mAdapter.DodajUOmiljeneAt(position);
        }
        else {
            mAdapter.IzbrisiAt(position);
        }
    }

}
