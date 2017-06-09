package com.abroscreative.weatherinformant.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abroscreative.weatherinformant.model.ForecastDayItem;
import com.abroscreative.weatherinformant.R;

import java.util.List;

/**
 * Created by David on 6/6/2017.
 */

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastDayItemViewHolder> {

    List<ForecastDayItem> mForecastDayItemList;
    Context mContext;

    public ForecastAdapter(Context context, List<ForecastDayItem> forecastDayItemList) {
        mForecastDayItemList = forecastDayItemList;
        mContext = context;
    }

    @Override
    public ForecastDayItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedItemView  = LayoutInflater.from(mContext).inflate(R.layout.item_forecast_day, parent, false);
        ForecastDayItemViewHolder holder = new ForecastDayItemViewHolder(inflatedItemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ForecastDayItemViewHolder holder, int position) {
        ForecastDayItem currentItem = mForecastDayItemList.get(position);
        holder.tempMaxTv.setText(currentItem.getTempMax()+"\u2103");
        holder.tempMinTv.setText(currentItem.getTempMin()+"\u2103");
        holder.humidityTv.setText(currentItem.getHumidity()+"%");
    }

    @Override
    public int getItemCount() {
        return mForecastDayItemList.size();
    }

    static class ForecastDayItemViewHolder extends RecyclerView.ViewHolder{

        TextView tempMaxTv;
        TextView tempMinTv;
        TextView humidityTv;

        public ForecastDayItemViewHolder(View itemView) {
            super(itemView);
            tempMaxTv = (TextView) itemView.findViewById(R.id.tvTempMax);
            tempMinTv = (TextView) itemView.findViewById(R.id.tvTempMin);
            humidityTv = (TextView) itemView.findViewById(R.id.tvHumidity);
        }
    }
}
