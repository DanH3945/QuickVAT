package com.hereticpurge.quickvat.apiservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

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
}
