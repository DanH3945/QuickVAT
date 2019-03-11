package com.hereticpurge.quickvat.apiservice;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiClient {

    // Simple retrofit call to get the full tax details from the api

    @GET(ApiReference.ApiUrl)
    Call<ApiModel> getVATRates();
}
