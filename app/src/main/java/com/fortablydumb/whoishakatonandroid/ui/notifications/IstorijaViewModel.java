package com.fortablydumb.whoishakatonandroid.ui.notifications;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fortablydumb.whoishakatonandroid.Domen;

import java.util.List;

public class IstorijaViewModel extends ViewModel {

    private MutableLiveData<List<Domen>> domeni;

    public MutableLiveData<List<Domen>> getDomeni() {
        if(domeni == null) domeni = new MutableLiveData<>();
        return this.domeni;
    }
}