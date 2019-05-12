package com.hereticpurge.quickvat;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hereticpurge.quickvat.appmodel.CountryObject;
import com.hereticpurge.quickvat.appmodel.QuickVatViewModel;

import timber.log.Timber;

public class VatDisplayFragment extends Fragment implements Observer<CountryObject> {

    public static final String TAG = "VatDisplayFragment";

    private CountryObject mSelectedCountry;

    QuickVatViewModel mViewModel;

    Button mCountrySelectButton;

    public static Fragment createInstance() {
        return new VatDisplayFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(QuickVatViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Timber.d("Fragment Loading %s", TAG);
        View view = inflater.inflate(R.layout.vat_display_fragment_layout, container, false);

        mCountrySelectButton = view.findViewById(R.id.vat_display_country_button);
        mCountrySelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timber.d("Button Clicked");
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.getSelected().observe(this, this);
        Timber.d("Added ViewModel Observer");
    }

    @Override
    public void onPause() {
        mViewModel.getSelected().removeObserver(this);
        Timber.d("Removed ViewModel Observer");
        super.onPause();
    }

    @Override
    public void onChanged(@Nullable CountryObject countryObject) {
        Timber.d("Country Object Updating in %s", TAG);
        mSelectedCountry = countryObject;
        updateUI();
    }

    private void updateUI() {
        if (mSelectedCountry != null) {
            mCountrySelectButton.setText(mSelectedCountry.getCountryName());
            Timber.d("Updating UI with non-null value");
        } else {
            Timber.d("Updating UI with null value");
            mCountrySelectButton.setText(R.string.vat_display_country_button_string);
        }
    }
}
