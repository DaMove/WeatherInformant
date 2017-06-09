package com.abroscreative.weatherinformant.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abroscreative.weatherinformant.model.ForecastDayItem;
import com.abroscreative.weatherinformant.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastFragment extends Fragment {

    private static final String KEY_FORECAST_LIST = "key_forecast_list";
    List<ForecastDayItem> mForecastDayItems;
    ForecastAdapter mAdapter;
    RecyclerView mRecyclerView;

    public static ForecastFragment newInstance(ArrayList<ForecastDayItem> forecastDayItems) {

        Bundle args = new Bundle();

        ForecastFragment fragment = new ForecastFragment();
        args.putParcelableArrayList(KEY_FORECAST_LIST, forecastDayItems);
        fragment.setArguments(args);
        return fragment;
    }

    public ForecastFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_forecast, container, false);
        mForecastDayItems = getArguments().getParcelableArrayList(KEY_FORECAST_LIST);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mAdapter = new ForecastAdapter(getActivity(), mForecastDayItems);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        return rootView;
    }

}
