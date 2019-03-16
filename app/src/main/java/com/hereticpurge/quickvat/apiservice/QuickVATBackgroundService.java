package com.hereticpurge.quickvat.apiservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.hereticpurge.quickvat.depinjector.ApiClientComponent;
import com.hereticpurge.quickvat.depinjector.ContextModule;
import com.hereticpurge.quickvat.depinjector.DaggerApiClientComponent;

public class QuickVATBackgroundService extends Service{

    /*
    TODO implement me
    Service should run once at startup and regularly thereafter to keep the database of rates
    up to date.
     */

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        ApiClientComponent clientComponent = DaggerApiClientComponent.builder()
                .contextModule(new ContextModule(this))
                .build();

        ApiClient apiClient = clientComponent.getApiClient();

        return null;
    }
}
