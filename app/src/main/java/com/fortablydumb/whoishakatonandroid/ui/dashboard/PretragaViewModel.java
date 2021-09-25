package com.fortablydumb.whoishakatonandroid.ui.dashboard;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fortablydumb.whoishakatonandroid.Domen;
import com.fortablydumb.whoishakatonandroid.DomenRepo;

public class PretragaViewModel extends ViewModel {

    private MutableLiveData<Domen> domen;

    public LiveData<Domen> getDomen() {
        if(domen == null) domen = new MutableLiveData<Domen>();
        return this.domen;
    }
}