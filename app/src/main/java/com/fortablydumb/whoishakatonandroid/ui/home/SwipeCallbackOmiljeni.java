package com.fortablydumb.whoishakatonandroid.ui.home;

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
import com.fortablydumb.whoishakatonandroid.ui.notifications.DomenListAdapter;

public class SwipeCallbackOmiljeni extends ItemTouchHelper.SimpleCallback {
    private DomenListAdapter mAdapter;

    private ColorDrawable background;

    public SwipeCallbackOmiljeni(DomenListAdapter adapter) {
        super(0, ItemTouchHelper.LEFT);
        mAdapter = adapter;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX,
                dY, actionState, isCurrentlyActive);
        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;
        if (dX > 0) { // Swiping to the right
            background = new ColorDrawable(Color.GREEN);
            background.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + ((int) dX) + backgroundCornerOffset,
                    itemView.getBottom());

        } else if (dX < 0) { // Swiping to the left
            background = new ColorDrawable(Color.RED);
            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else { // view is unSwiped
            background = new ColorDrawable(Color.TRANSPARENT);
            background.setBounds(0, 0, 0, 0);
        }
        background.draw(c);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        if(direction == ItemTouchHelper.LEFT) {
            mAdapter.IzbaciIzOmiljenihAt(position);
        }
    }

}
