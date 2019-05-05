package com.hereticpurge.quickvat.apiservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.hereticpurge.quickvat.apiservice.apimodel.ApiModel;
import com.hereticpurge.quickvat.depinjector.ApiClientComponent;
import com.hereticpurge.quickvat.depinjector.ContextModule;
import com.hereticpurge.quickvat.depinjector.DaggerApiClientComponent;

import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

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
                addToDatabase(response.body());
            }

            @Override
            public void onFailure(Call<ApiModel> call, Throwable t) {
                Timber.e(t);
            }
        });

        return super.onStartCommand(intent, flags, startId);
    }

    private void addToDatabase(ApiModel apiModel) {
        // Test Code
        Set<String> keys = apiModel.getRates().get(0).getPeriods().get(0).getRates().keySet();

        for (String key : keys) {
            String rate = apiModel.getRates().get(0).getPeriods().get(0).getRates().get(key);

            Timber.d("Rate level : " + key + " --------- " + "Rate : " + rate);
        }
    }
}
