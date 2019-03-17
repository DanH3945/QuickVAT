package com.hereticpurge.quickvat.apiservice.apimodel;

import java.util.Arrays;
import java.util.List;

public class CountryObject {

    // Object representing a single country with it's individual taxes and effective start
    // dates as TaxPeriodObjects in a list.

    private String name;
    private String code;
    private String country_code;

    private TaxPeriodObject[] periods;

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getCountryCode() {
        return country_code;
    }

    public List<TaxPeriodObject> getPeriods() {
        return Arrays.asList(periods);
    }
}
