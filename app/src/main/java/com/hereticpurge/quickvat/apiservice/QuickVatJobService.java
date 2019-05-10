package com.hereticpurge.quickvat.apiservice;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.JobIntentService;

import com.hereticpurge.quickvat.apiservice.apimodel.ApiCountryObject;
import com.hereticpurge.quickvat.apiservice.apimodel.ApiModel;
import com.hereticpurge.quickvat.apiservice.apimodel.ApiTaxPeriodObject;
import com.hereticpurge.quickvat.appmodel.CountryObject;
import com.hereticpurge.quickvat.database.CountryDatabase;
import com.hereticpurge.quickvat.depinjector.ApiClientComponent;
import com.hereticpurge.quickvat.depinjector.ContextModule;
import com.hereticpurge.quickvat.depinjector.DaggerApiClientComponent;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import timber.log.Timber;

public class QuickVatJobService extends JobIntentService {

    private static final int ONE_MINUTE = 1000 * 60;
    private static final int FIVE_MINUTES = ONE_MINUTE * 5; // Smallest possible period for JobScheduler;
    private static final int TWENTY_FOUR_HOURS = ONE_MINUTE * 60 * 24;

    private static final int JOB_ID = 1;

    public static void schedule(Context context) {
        enqueueWork(context, QuickVatJobService.class, JOB_ID, new Intent());
    }

    @Override
    protected void onHandleWork(@Nullable Intent intent) {
        doUpdateDatabase();
    }

    private void doUpdateDatabase() {

        ApiClientComponent apiClientComponent = DaggerApiClientComponent.builder()
                .contextModule(new ContextModule(getApplicationContext()))
                .build();

        ApiClient mApiClient = apiClientComponent.getApiClient();

        Call<ApiModel> call = mApiClient.getVATRates();

        // Not a normal retrofit call.  We want to call on the current IntentService thread
        // not the retrofit thread which will try to send the return back to the UI thread.
        // We need to use the data in this thread so we use execute() instead of enqueue() to
        // stay on thread.
        try {
            addToDatabase(call.execute().body());
        } catch (IOException e) {
            Timber.d(e);
        }
    }

    private void addToDatabase(ApiModel apiModel) {
        // apiModel.getRates().get(0).getPeriods().get(0).getRates().keySet();

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

            CountryDatabase.getCountryDatabase(getApplicationContext()).countryDao().insertCountryObject(databaseCountryObject);

            Timber.d("Country added to DB from service: %s", databaseCountryObject.getCountryName());

        }

    }
}
