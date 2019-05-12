package com.hereticpurge.quickvat.apiservice;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.JobIntentService;

import com.hereticpurge.quickvat.apiservice.apimodel.ApiCountryObject;
import com.hereticpurge.quickvat.apiservice.apimodel.ApiModel;
import com.hereticpurge.quickvat.apiservice.apimodel.ApiTaxPeriodObject;
import com.hereticpurge.quickvat.appmodel.CountryObject;
import com.hereticpurge.quickvat.database.CountryDao;
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

public class QuickVatJobIntentService extends JobIntentService {

    private static final int JOB_ID = 1;

    public static void schedule(Context context) {
        enqueueWork(context, QuickVatJobIntentService.class, JOB_ID, new Intent());
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

        // Get the Dao to work with the database.
        CountryDao countryDao = CountryDatabase.getCountryDatabase(getApplicationContext()).countryDao();

        // Clear the database to refresh.
        countryDao.clearDatabase();

        for (ApiCountryObject apiCountryObject : apiModel.getRates()) {

            // Api country object conforms to the Retrofit API model which isn't an exact match for
            // the room database model. So we have to convert from retrofit to room.
            CountryObject databaseCountryObject = new CountryObject();

            databaseCountryObject.setCountryName(apiCountryObject.getName());
            databaseCountryObject.setCode(apiCountryObject.getCode());
            databaseCountryObject.setCountryCode(apiCountryObject.getCountryCode());

            Map<String, String> rates = new HashMap<>();

            Calendar lastPeriodDate = null;

            for (ApiTaxPeriodObject apiTaxPeriodObject : apiCountryObject.getPeriods()) {

                // New calendar object for our tax period.
                Calendar currentPeriodDate = Calendar.getInstance();

                try {
                    // Dates come in the format yyyy-mm-dd as a string.  So we split the string
                    // and parse the int values to update our calendar.
                    String[] effectiveFrom = apiTaxPeriodObject.getEffectiveFrom().split("-");
                    int year = Integer.parseInt(effectiveFrom[0]);
                    int month = Integer.parseInt(effectiveFrom[1]);
                    int day = Integer.parseInt(effectiveFrom[2]);
                    currentPeriodDate.set(year, month, day, 0, 0);
                } catch (NumberFormatException ex) {
                    // If the date isn't properly formatted we catch the exception and continue
                    // to the next tax period.  If we don't know the date of the period we
                    // can't properly figure out which entries should be kept.
                    Timber.e(ex);
                    continue;
                }

                if (lastPeriodDate == null) {
                    lastPeriodDate = currentPeriodDate;
                }

                for (String rate : apiTaxPeriodObject.getRates().keySet()) {
                    try {
                        // If the current period date is after or equal to the last period we looked
                        // at then the rates are newer and we want to overwrite them.  If not we
                        // move to the next check.
                        if (lastPeriodDate.before(currentPeriodDate) || lastPeriodDate.equals(currentPeriodDate)) {
                            rates.put(rate, apiTaxPeriodObject.getRates().get(rate));
                        }

                        // If the current period date is before the last period we worked on the rates
                        // are older and we only want to store them if the key doesn't already
                        // exist (we haven't seen that tax rate before).
                        if (lastPeriodDate.after(currentPeriodDate) && !rates.containsKey(rate)) {
                            rates.put(rate, apiTaxPeriodObject.getRates().get(rate));
                        }
                    } catch (NullPointerException e) {
                        // Rates might be null so we just catch the exception and move on.
                        Timber.e(e);
                    }
                }

                if (lastPeriodDate.before(currentPeriodDate)) {
                    lastPeriodDate = currentPeriodDate;
                }

            }

            databaseCountryObject.setRates(rates);

            countryDao.insertCountryObject(databaseCountryObject);

            Timber.d("Country added to DB from service: %s -- With Id: %s", databaseCountryObject.getCountryName(), databaseCountryObject.getId());

        }

    }
}
