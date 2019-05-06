package com.hereticpurge.quickvat.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.hereticpurge.quickvat.appmodel.CountryObject;

import java.util.List;

@Dao
public interface CountryDao {

    @Query("SELECT * FROM CountryObject")
    LiveData<List<CountryObject>> getCountryObjects();

    @Query("SELECT * FROM CountryObject WHERE id = :id")
    LiveData<CountryObject> getSingleCountryObjectById(int id);

    @Query("SELECT * FROM CountryObject WHERE country_name = :name")
    LiveData<CountryObject> getSingleCountryObjectByName(String name);

    @Insert
    long insertCountryObject(CountryObject countryObject);
}
