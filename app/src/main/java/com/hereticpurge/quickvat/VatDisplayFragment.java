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
import android.widget.TextView;

import com.hereticpurge.quickvat.appmodel.CountryObject;
import com.hereticpurge.quickvat.appmodel.QuickVatViewModel;

import java.util.List;

import timber.log.Timber;

public class VatDisplayFragment extends Fragment implements Observer<List<CountryObject>> {

    public static final String TAG = "VatDisplayFragment";

    private List<CountryObject> mCountryList;
    private CountryObject mSelectedCountry;

    QuickVatViewModel mViewModel;

    TextView mTextView;

    public static Fragment createInstance() {
        return new VatDisplayFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Timber.d("Fragment Loading %s", TAG);
        View view = inflater.inflate(R.layout.vat_display_fragment_layout, container, false);

        mTextView = view.findViewById(R.id.vat_display_main_text);

        mViewModel = ViewModelProviders.of(this).get(QuickVatViewModel.class);

        mViewModel.getCountryObjects().observe(this, this);
        Timber.d("Added ViewModel Observer");

        return view;
    }

    @Override
    public void onChanged(@Nullable List<CountryObject> countryObjects) {
        Timber.d("Country List Updating");
        mCountryList = countryObjects;
    }

    private void updateUI() {
        Timber.d("Updating UI");
    }
}
