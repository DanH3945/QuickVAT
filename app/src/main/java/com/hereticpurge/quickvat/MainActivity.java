package com.hereticpurge.quickvat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.hereticpurge.quickvat.apiservice.QuickVatJobIntentService;
import com.hereticpurge.quickvat.timberlogging.TimberDebugTree;
import com.hereticpurge.quickvat.timberlogging.TimberReleaseTree;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private final String FIRST_RUN_PREF_STRING = "FirstRun";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TIMBER MUST BE THE FIRST THING LOADED TO HANDLE DEBUGGING CORRECTLY
        // NOTHING GOES ABOVE THIS COMMENT
        if (BuildConfig.DEBUG) {
            // Timber debug tree
            Timber.plant(new TimberDebugTree());
            Timber.d("Loaded Debug Tree");
        } else {
            // Timber Release Tree
            Timber.plant(new TimberReleaseTree());
            Timber.d("Loaded Release Tree");
        }
        // END OF REQUIRED TIMBER LOAD


        // Start the service running to update the local database with the latest VAT rates
        // provided by jsonvat.com
        QuickVatJobIntentService.schedule(getApplicationContext());

    }

    private void loadFragment(Fragment fragment, boolean addToBackStack, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment_container, fragment);

        if (addToBackStack) fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
    }
}
