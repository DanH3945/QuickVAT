package com.hereticpurge.quickvat.apiservice.apimodel;

import java.util.Arrays;
import java.util.List;

public class ApiModel {

    // Model representing the full data pulled from the ApiClient retrofit call.
    // All the data comes at once in a single json return.  (no choice here since that's
    // how the api is setup).

    private String details;
    private String version;

    private ApiCountryObject[] rates;

    public String getDetails() {
        return details;
    }

    public String getVersion() {
        return version;
    }

    public List<ApiCountryObject> getRates() {
        return Arrays.asList(rates);
    }
}
