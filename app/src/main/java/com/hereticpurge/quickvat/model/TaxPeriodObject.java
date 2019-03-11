package com.hereticpurge.quickvat.model;

import java.util.Date;
import java.util.Map;

public class TaxPeriodObject {

    // Object representing tax data from a single country and effective date.  Several of these
    // may be used per CountryObject depending on how often the taxes have changed per country.

    private Date mEffectiveStart;

    private Map<String, String> mRates;

    public void addRate(String name, String rate) {
        this.mRates.put(name, rate);
    }

    public Map getRates() {
        return this.mRates;
    }

    public void setEffectiveStart(Date date) {
        this.mEffectiveStart = date;
    }

    public Date getEffectiveStart() {
        return this.mEffectiveStart;
    }
}
