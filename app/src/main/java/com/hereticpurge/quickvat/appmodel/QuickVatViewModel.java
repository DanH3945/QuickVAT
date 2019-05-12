package com.hereticpurge.quickvat.appmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.hereticpurge.quickvat.database.CountryDatabase;

import java.util.List;

public class QuickVatViewModel extends AndroidViewModel {

    private final MutableLiveData<CountryObject> selectedItem = new MutableLiveData<>();

    private CountryDatabase mCountryDatabase;

    public QuickVatViewModel(@NonNull Application application) {
        super(application);

        mCountryDatabase = CountryDatabase.getCountryDatabase(this.getApplication());
    }

    public LiveData<List<CountryObject>> getCountryObjects() {
        return mCountryDatabase.countryDao().getCountryObjects();
    }

    public LiveData<CountryObject> getSingleCountryObjectById(int id) {
        return mCountryDatabase.countryDao().getSingleCountryObjectById(id);
    }

    public LiveData<CountryObject> getSingleCountryObjectByName(String name) {
        return mCountryDatabase.countryDao().getSingleCountryObjectByName(name);
    }

    public void clearDatabase() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mCountryDatabase.countryDao().clearDatabase();
            }
        }).start();
    }

    public void setSelectedItem(CountryObject countryObject) {
        selectedItem.setValue(countryObject);
    }

    public LiveData<CountryObject> getSelected() {
        return selectedItem;
    }

}
