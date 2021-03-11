package com.nateprat.university.mobileplatformdevelopment.ui.map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MapViewFragment extends ViewModel {

    private MutableLiveData<String> mText;

    public MapViewFragment() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}