package com.hereticpurge.quickvat.appmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.hereticpurge.quickvat.database.CountryDatabase;

import java.util.List;

public class QuickVatViewModel extends AndroidViewModel {

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

    public long insertCountryObject(CountryObject countryObject) {
        return mCountryDatabase.countryDao().insertCountryObject(countryObject);
    }

}
