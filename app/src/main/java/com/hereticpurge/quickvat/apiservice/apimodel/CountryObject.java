package com.hereticpurge.quickvat.apiservice.apimodel;

public class CountryObject {

    // Object representing a single country with it's individual taxes and effective start
    // dates as TaxPeriodObjects in a list.

    private String name;
    private String code;
    private String country_code;

    private TaxPeriodObject[] periods;
}
