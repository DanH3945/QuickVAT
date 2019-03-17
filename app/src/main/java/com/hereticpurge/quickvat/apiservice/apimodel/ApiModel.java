package com.hereticpurge.quickvat.apiservice.apimodel;

public class ApiModel {

    // Model representing the full data pulled from the ApiClient retrofit call.
    // All the data comes at once in a single json return.  (no choice here since that's
    // how the api is setup).

    private String details;
    private String version;

    private CountryObject[] rates;

}
