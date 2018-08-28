package com.zrz.android.olearisweather.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zrz.android.olearisweather.R;
import com.zrz.android.olearisweather.WeatherActivity;

import java.util.ArrayList;

public class RvPlacesAdapter extends RecyclerView.Adapter<RvPlacesAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Double> latitude;
    private ArrayList<Double> longitude;
    private final static String COORDINATES="coordinates";

    public RvPlacesAdapter(Context context, ArrayList<Double> latitude, ArrayList<Double> longitude){
        this.context=context;
        this.latitude=latitude;
        this.longitude=longitude;
        layoutInflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RvPlacesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = layoutInflater.inflate(R.layout.item_rv_places, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RvPlacesAdapter.ViewHolder holder, int position) {
        String address = String.valueOf(latitude.get(position))+"\n"+String.valueOf(longitude.get(position));
        holder.textView.setText(address);
        final int currentPosition=position;
        holder.textView.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   Intent intent = new Intent(context, WeatherActivity.class);
                                                   double[] coordinates={latitude.get(currentPosition), longitude.get(currentPosition)};
                                                   intent.putExtra(COORDINATES, coordinates);
                                                   context.startActivity(intent);
                                               }
                                           }
        );
    }

    @Override
    public int getItemCount() {
        return latitude.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        final TextView textView;

        ViewHolder(View view) {
            super(view);
            textView=(TextView)view.findViewById(R.id.tv_item_places);
        }
    }
}
