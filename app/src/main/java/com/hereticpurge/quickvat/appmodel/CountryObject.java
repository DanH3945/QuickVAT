package com.hereticpurge.quickvat.appmodel;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

@Entity
@TypeConverters(CountryObject.class)
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
    Map<String, String> rates;

    public CountryObject() {

    }

    public CountryObject(int id, String countryName, String code, String countryCode, Map<String, String> rates) {
        this.id = id;
        this.countryName = countryName;
        this.code = code;
        this.countryCode = countryCode;
        this.rates = rates;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Map<String, String> getRates() {
        return rates;
    }

    public void setRates(Map<String, String> rates) {
        this.rates = rates;
    }

    @TypeConverter
    public String mapToStringConverter(Map<String, String> rateMap) {
        StringBuilder sb = new StringBuilder();

        for (String key : rateMap.keySet()) {
            sb.append("#");
            sb.append(key);
            sb.append(",");
            sb.append(rateMap.get(key));
        }
        return sb.toString();
    }

    @TypeConverter
    public Map<String, String> stringToMapConverter(String rateString) {
        Map<String, String> rateMap = new HashMap<>();

        String[] rates = rateString.split("#");

        for (String rate : rates) {
            String[] temp = rate.split(",");
            if (temp.length > 1) {
                rateMap.put(temp[0], temp[1]);
            }
        }
        return rateMap;
    }
}
