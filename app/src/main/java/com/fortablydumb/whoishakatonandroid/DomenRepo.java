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

    // daje sve iz istorije
    public List<Domen> getAll() {
        return domenDao.getAll();
    }

    // daje omiljene
    public List<Domen> getFavourites() {
        return domenDao.getFavourites();
    }

    // daje iz kesa po nazivu
    public Domen getDomen(String naziv) {
        return domenDao.findByName(naziv);
    }

    // preuzima sa servera i upisuje umesto kesirane verzija
    public void refreshDomen(Domen d) {
        domenDao.delete(d);
        domenDao.insert(d);
    }
}