package com.hereticpurge.quickvat;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hereticpurge.quickvat.appmodel.CountryObject;
import com.hereticpurge.quickvat.appmodel.QuickVatViewModel;

import timber.log.Timber;

public class RecyclerFragment extends Fragment {

    public static final String TAG = "Recycler Fragment";

    RecyclerView mRecyclerView;

    RecyclerFragmentAdapter mRecyclerFragmentAdapter;

    public static Fragment createInstance() {
        return new RecyclerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Timber.d("Fragment Loading %s", TAG);

        View view = inflater.inflate(R.layout.vat_recycler_fragment_layout, container, false);

        final QuickVatViewModel viewModel = ViewModelProviders.of(getActivity()).get(QuickVatViewModel.class);

        mRecyclerView = view.findViewById(R.id.vat_recycler_view);
        mRecyclerFragmentAdapter = new RecyclerFragmentAdapter(viewModel.getCountryObjects(), new RecyclerFragmentAdapter.RecyclerClickListener() {
            @Override
            public void itemSelected(CountryObject countryObject) {
                viewModel.setSelectedItem(countryObject);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        mRecyclerView.setAdapter(mRecyclerFragmentAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onResume() {
        mRecyclerFragmentAdapter.doSubscribe(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        mRecyclerFragmentAdapter.doUnsubscribe();
        super.onPause();
    }
}
