package com.fortablydumb.whoishakatonandroid.ui.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class IstorijaViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public IstorijaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}