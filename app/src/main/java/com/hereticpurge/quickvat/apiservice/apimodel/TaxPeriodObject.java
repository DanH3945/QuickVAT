package com.hereticpurge.quickvat.apiservice.apimodel;

import java.util.Date;
import java.util.Map;

public class TaxPeriodObject {

    // Object representing tax data from a single country and effective date.  Several of these
    // may be used per CountryObject depending on how often the taxes have changed per country.

    private String effective_from;
    private RatesObject rates;
}
