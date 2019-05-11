package com.hereticpurge.quickvat;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

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

            // In release mode check for updates to the database.
            // If that app runs in debug onCreateOptionsMenu() will make a manual database update
            // button available to avoid hitting the REST API constantly with restarts.
            doDatabaseUpdate();
        }

        if (savedInstanceState == null) {
            loadFragment(VatDisplayFragment.createInstance(), false, null);
        }

    }

    private void loadFragment(Fragment fragment, boolean addToBackStack, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment_container, fragment);

        if (addToBackStack) fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
    }

    private void doDatabaseUpdate() {
        // Start the service running to update the local database with the latest VAT rates
        // provided by jsonvat.com
        if (isNetworkConnected()) {
            QuickVatJobIntentService.schedule(getApplicationContext());
            Timber.d("Internet Connected.  Performing DB update.");
        }
    }

    private boolean isNetworkConnected() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            return cm.getActiveNetworkInfo() != null;
        } catch (NullPointerException e) {
            Timber.e(e);
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.overflow_menu, menu);

        // In debug mode the database doesn't automatically refresh on app start to avoid
        // flooding the REST service.  Instead this makes the Debug Refresh button in the
        // overflow menu visible to allow manual database updates.
        if (BuildConfig.DEBUG) {
            menu.findItem(R.id.overflow_menu_debug_refresh).setVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.overflow_menu_about:
                // new AboutDialogFragment().show(getSupportFragmentManager(), null);
                break;

            case R.id.overflow_menu_preferences:
                loadFragment(PreferenceFragment.createInstance(), true, PreferenceFragment.TAG);
                break;

            case R.id.overflow_menu_debug_refresh:
                doDatabaseUpdate();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
