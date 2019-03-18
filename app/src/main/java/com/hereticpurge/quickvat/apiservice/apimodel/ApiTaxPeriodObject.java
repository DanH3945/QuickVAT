package com.hereticpurge.quickvat.apiservice.apimodel;

import java.util.Map;

public class ApiTaxPeriodObject {

    // Object representing tax data from a single country and effective date.  Several of these
    // may be used per ApiCountryObject depending on how often the taxes have changed per country.

    private String effective_from;
    private Map<String, String> rates;

    public String getEffectiveFrom() {
        return effective_from;
    }

    public Map<String, String> getRates() {
        return rates;
    }
}
