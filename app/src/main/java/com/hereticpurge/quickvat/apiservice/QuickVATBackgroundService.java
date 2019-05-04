package com.hereticpurge.quickvat.apiservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.hereticpurge.quickvat.apiservice.apimodel.ApiModel;
import com.hereticpurge.quickvat.depinjector.ApiClientComponent;
import com.hereticpurge.quickvat.depinjector.ContextModule;
import com.hereticpurge.quickvat.depinjector.DaggerApiClientComponent;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuickVATBackgroundService extends Service{

    /*
    TODO implement me
    Service should run once at startup and regularly thereafter to keep the database of rates
    up to date.
     */

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        ApiClientComponent apiClientComponent = DaggerApiClientComponent.builder()
                .contextModule(new ContextModule(getApplicationContext()))
                .build();

        ApiClient mApiClient = apiClientComponent.getApiClient();

        Call<ApiModel> call = mApiClient.getVATRates();
        call.enqueue(new Callback<ApiModel>() {
            @Override
            public void onResponse(Call<ApiModel> call, Response<ApiModel> response) {
                System.out.println(" ----------------- RESPONSE FROM SERVICE ----------------");
            }

            @Override
            public void onFailure(Call<ApiModel> call, Throwable t) {
                System.out.print("-------------- FAILURE FROM SERVICE -----------------");
            }
        });

        return super.onStartCommand(intent, flags, startId);
    }
}
