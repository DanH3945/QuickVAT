package com.hereticpurge.quickvat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import timber.log.Timber;

public class PreferenceFragment extends Fragment {

    public static final String TAG = "PreferenceFragment";

    public static Fragment createInstance() {
        return new PreferenceFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Timber.d("Fragment Loading %s", TAG);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
