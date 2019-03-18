package com.hereticpurge.quickvat.appmodel;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

@Entity
public class CountryObject {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "country_name")
    private String countryName;

    @ColumnInfo(name = "code")
    private String code;

    @ColumnInfo(name = "country_code")
    private String countryCode;

    @ColumnInfo(name = "current_rates")
    List<String> rates;

    public CountryObject(int id, String countryName, String code, String countryCode, List<String> rates) {
        this.id = id;
        this.countryName = countryName;
        this.code = code;
        this.countryCode = countryCode;
        this.rates = rates;
    }

    public int getId() {
        return id;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getCode() {
        return code;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public List<String> getRates() {
        return rates;
    }
}
