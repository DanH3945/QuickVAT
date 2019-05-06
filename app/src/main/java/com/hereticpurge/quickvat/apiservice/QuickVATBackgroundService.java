package com.hereticpurge.quickvat.apiservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.hereticpurge.quickvat.apiservice.apimodel.ApiCountryObject;
import com.hereticpurge.quickvat.apiservice.apimodel.ApiModel;
import com.hereticpurge.quickvat.apiservice.apimodel.ApiTaxPeriodObject;
import com.hereticpurge.quickvat.appmodel.CountryObject;
import com.hereticpurge.quickvat.database.CountryDatabase;
import com.hereticpurge.quickvat.depinjector.ApiClientComponent;
import com.hereticpurge.quickvat.depinjector.ContextModule;
import com.hereticpurge.quickvat.depinjector.DaggerApiClientComponent;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class QuickVATBackgroundService extends Service{

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
        // apiModel.getRates().get(0).getPeriods().get(0).getRates().keySet();
        // Todo this call needs to be made async.  Executor?

        for (ApiCountryObject apiCountryObject : apiModel.getRates()) {
            CountryObject databaseCountryObject = new CountryObject();

            databaseCountryObject.setCountryName(apiCountryObject.getName());
            databaseCountryObject.setCode(apiCountryObject.getCode());
            databaseCountryObject.setCountryCode(apiCountryObject.getCountryCode());

            Map<String, String> rates = new HashMap<>();

            Calendar mostRecentPeriod = null;

            for (ApiTaxPeriodObject apiTaxPeriodObject : apiCountryObject.getPeriods()) {

                Calendar currentCalendar = Calendar.getInstance();

                try {
                    String[] effectiveFrom = apiTaxPeriodObject.getEffectiveFrom().split("-");
                    int year = Integer.parseInt(effectiveFrom[0]);
                    int month = Integer.parseInt(effectiveFrom[1]);
                    int day = Integer.parseInt(effectiveFrom[2]);
                    currentCalendar.set(year, month, day, 0, 0);
                } catch (NumberFormatException ex) {
                    // If the date isn't properly formatted we catch the exception and continue
                    // to the next tax period.  If we don't know the date of the period we
                    // can't properly figure out which entries should be kept.
                    Timber.e(ex);
                    continue;
                }

                if (mostRecentPeriod == null) {
                    mostRecentPeriod = currentCalendar;
                }

                for (String rate : apiTaxPeriodObject.getRates().keySet()) {
                    try {
                        if (mostRecentPeriod.before(currentCalendar) || mostRecentPeriod.equals(currentCalendar)) {
                            rates.put(rate, apiTaxPeriodObject.getRates().get(rate));
                        }

                        if (mostRecentPeriod.after(currentCalendar) && !rates.containsKey(rate)) {
                            rates.put(rate, apiTaxPeriodObject.getRates().get(rate));
                        }
                    } catch (NullPointerException e) {
                        // Rates might be null so we just catch the exception and move on.
                        Timber.e(e);
                    }
                }

                if (mostRecentPeriod.before(currentCalendar)) {
                    mostRecentPeriod = currentCalendar;
                }

            }

            databaseCountryObject.setRates(rates);

            CountryDatabase.getCountryDatabase(getBaseContext()).countryDao().insertCountryObject(databaseCountryObject);

        }

    }
}
