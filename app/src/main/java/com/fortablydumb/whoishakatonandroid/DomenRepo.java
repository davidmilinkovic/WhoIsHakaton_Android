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
        domenDao.update(d);
    }

    public void deleteDomen(Domen d) {
        domenDao.delete(d);
    }

    public void addToFavourites(Domen d) {
        domenDao.addToFavourites(d.getNaziv());
    }

    public void removeFromFavourites(Domen d) {
        domenDao.removeFromFavourites(d.getNaziv());
    }

    public void insertDomen(Domen d) {
        domenDao.insert(d);
    }
}