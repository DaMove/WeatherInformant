package com.abroscreative.weatherinformant.ui;


import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abroscreative.weatherinformant.R;
import com.abroscreative.weatherinformant.databinding.FragmentCurrentWeatherBinding;
import com.abroscreative.weatherinformant.model.CurrentWeatherItem;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentWeatherFragment extends Fragment {

    private static final String KEY_CURRENT = "key_current";
    CurrentWeatherItem mCurrentWeatherItem;
    FragmentCurrentWeatherBinding mFragmentBinding;

    public CurrentWeatherFragment() {
        // Required empty public constructor
    }

    public static CurrentWeatherFragment newInstance(CurrentWeatherItem currentWeatherItem) {

        Bundle args = new Bundle();

        CurrentWeatherFragment fragment = new CurrentWeatherFragment();
        args.putParcelable(KEY_CURRENT,currentWeatherItem);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_current_weather, container, false);
        View rootView = mFragmentBinding.getRoot();
        mCurrentWeatherItem = getArguments().getParcelable(KEY_CURRENT);

        //Binding data to widgets
        mFragmentBinding.humidityValue.setText(mCurrentWeatherItem.getHumidity()+"");
        mFragmentBinding.locationLabel.setText(mCurrentWeatherItem.getLocation());
        mFragmentBinding.pressureValue.setText(mCurrentWeatherItem.getPressure()+"");
        mFragmentBinding.temperatureValue.setText(mCurrentWeatherItem.getTemperature()+"");
        mFragmentBinding.descriptionValue.setText(mCurrentWeatherItem.getDescription());

        String iconId = mCurrentWeatherItem.getIcon()+".png";
        Uri iconImageUri = Uri.parse("http://openweathermap.org/img/w/"+iconId);
        Picasso.with(getActivity()).load(iconImageUri).into(mFragmentBinding.iconImageView);
        return  rootView;
    }

}
