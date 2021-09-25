package com.fortablydumb.whoishakatonandroid;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;

public class DomenRepo {

    private final Executor executor;


    private final DomenDao domenDao;

    public DomenRepo(DomenDao domenDao, Executor executor) {
        this.domenDao = domenDao;
        this.executor = executor;
    }

    // daje sve iz baze
    public List<Domen> getAll() {
        return domenDao.getAll();
    }

    public Domen getDomen(String naziv) {
        Domen d = domenDao.findByName(naziv);
        return d;
    }


    public void refreshDomen(Domen d) {
        domenDao.delete(d);
        domenDao.insert(d);
    }
}