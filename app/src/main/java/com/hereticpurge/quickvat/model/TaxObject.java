package com.hereticpurge.quickvat.model;

import java.util.Date;
import java.util.Map;

public class TaxObject {

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
