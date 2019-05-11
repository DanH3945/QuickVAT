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

public class VatDisplayFragment extends Fragment implements Observer<CountryObject> {

    public static final String TAG = "VatDisplayFragment";

    QuickVatViewModel mViewModel;

    TextView mTextView;

    public static Fragment createInstance() {
        return new VatDisplayFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vat_display_fragment_layout, container, false);

        mTextView = view.findViewById(R.id.vat_display_main_text);

        mViewModel = ViewModelProviders.of(this).get(QuickVatViewModel.class);

        mViewModel.getSingleCountryObjectById(1).observe(this, this);

        return view;
    }

    @Override
    public void onChanged(@Nullable CountryObject countryObject) {
        mTextView.setText(countryObject.getCountryName());
    }
}
