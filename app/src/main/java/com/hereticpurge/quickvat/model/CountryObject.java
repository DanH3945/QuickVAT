package com.hereticpurge.quickvat.model;

import java.util.List;

public class CountryObject {

    // Object representing a single country with it's individual taxes and effective start
    // dates as TaxPeriodObjects in a list.

    private String mCountryName;
    private String mCode;
    private String mCountryCode;

    private List<TaxPeriodObject> mTaxPeriods;
}
