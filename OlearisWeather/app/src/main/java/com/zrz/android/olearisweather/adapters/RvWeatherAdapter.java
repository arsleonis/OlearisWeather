package com.zrz.android.olearisweather.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zrz.android.olearisweather.R;
import com.zrz.android.olearisweather.Retrofit.ApiInterface;

import java.util.ArrayList;

public class RvWeatherAdapter extends RecyclerView.Adapter<RvWeatherAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private ArrayList<String> dates;
    private ArrayList<String> currentWeather;


    public RvWeatherAdapter(Context context, ArrayList<String> dates, ArrayList<String> currentWeather){
        this.dates=dates;
        this.currentWeather=currentWeather;
        layoutInflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RvWeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = layoutInflater.inflate(R.layout.item_rv_weather, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RvWeatherAdapter.ViewHolder holder, int position) {
        holder.textViewDate.setText(dates.get(position));
        holder.textViewWeather.setText(currentWeather.get(position));
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        final LinearLayout linearLayout;
        final TextView textViewDate, textViewWeather;

        ViewHolder(View view) {
            super(view);
            linearLayout=(LinearLayout) view.findViewById(R.id.ll_item_weather);
            textViewDate=(TextView)view.findViewById(R.id.tv_item_weather_date);
            textViewWeather=(TextView) view.findViewById(R.id.tv_item_weather_weather);
        }
    }
}
