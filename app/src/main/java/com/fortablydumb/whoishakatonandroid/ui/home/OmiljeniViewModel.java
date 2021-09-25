package com.fortablydumb.whoishakatonandroid.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OmiljeniViewModel extends ViewModel {

    private MutableLiveData<String> mText;


    public OmiljeniViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}