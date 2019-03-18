package com.hereticpurge.quickvat.apiservice.apimodel;

import java.util.Arrays;
import java.util.List;

public class ApiCountryObject {

    // Object representing a single country with it's individual taxes and effective start
    // dates as TaxPeriodObjects in a list.

    private String name;
    private String code;
    private String country_code;

    private ApiTaxPeriodObject[] periods;

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getCountryCode() {
        return country_code;
    }

    public List<ApiTaxPeriodObject> getPeriods() {
        return Arrays.asList(periods);
    }
}
