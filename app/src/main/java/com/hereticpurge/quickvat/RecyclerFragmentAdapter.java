package com.hereticpurge.quickvat;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hereticpurge.quickvat.appmodel.CountryObject;

import java.util.List;

import timber.log.Timber;

public class RecyclerFragmentAdapter extends RecyclerView.Adapter<RecyclerFragmentAdapter.RecyclerDisplayViewHolder>
        implements Observer<List<CountryObject>> {

    private LiveData<List<CountryObject>> mLiveDataList;

    private List<CountryObject> mListCountryObjects;

    private RecyclerClickListener listener;

    RecyclerFragmentAdapter(LiveData<List<CountryObject>> listLiveData, RecyclerClickListener listener) {
        mLiveDataList = listLiveData;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerDisplayViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.vat_recycler_fragment_item, viewGroup, false);
        return new RecyclerDisplayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerDisplayViewHolder recyclerDisplayViewHolder, int i) {
        final CountryObject countryObject = mListCountryObjects.get(i);

        Timber.d("Creating ViewHolder for %s", countryObject.getCountryName());

        recyclerDisplayViewHolder.mCountryNameTextView.setText(countryObject.getCountryName());

        recyclerDisplayViewHolder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.itemSelected(countryObject);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mListCountryObjects == null || mListCountryObjects.size() < 1 ? 0 : mListCountryObjects.size();
    }

    @Override
    public void onChanged(@Nullable List<CountryObject> countryObjects) {
        this.mListCountryObjects = countryObjects;
        this.notifyDataSetChanged();
    }

    public void doSubscribe(LifecycleOwner owner) {
        // Called in the onResume() method of the parent Fragment to sub to the Live Data.
        mLiveDataList.observe(owner, this);
    }

    public void doUnsubscribe() {
        // Called in the OnPause() method of the parent Fragment to unsub the Live Data.
        mLiveDataList.removeObserver(this);
    }


    class RecyclerDisplayViewHolder extends RecyclerView.ViewHolder {

        CardView mCardView;
        TextView mCountryNameTextView;
        ImageView mCountryFlagImageView;

        public RecyclerDisplayViewHolder(@NonNull View itemView) {
            super(itemView);

            mCardView = itemView.findViewById(R.id.vat_recycler_card);
            mCountryNameTextView = itemView.findViewById(R.id.vat_recycler_card_country_name_text);
            mCountryFlagImageView = itemView.findViewById(R.id.vat_recycler_card_country_flag_image);
        }
    }

    public interface RecyclerClickListener {
        void itemSelected(CountryObject countryObject);
    }
}
