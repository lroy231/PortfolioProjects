package com.example.hw05;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ForecastRecyclerAdapter extends RecyclerView.Adapter<ForecastRecyclerAdapter.ForecastViewHolder> {
    ArrayList<ForeCast> foreCasters;
    Context context;

    public ForecastRecyclerAdapter(ArrayList<ForeCast> data, Context context){
        this.foreCasters = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_layout, parent, false);

        ForecastViewHolder forecastViewHolder = new ForecastViewHolder(view);

        return forecastViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {
        ForeCast foreCast = foreCasters.get(position);
        holder.date.setText(foreCast.getDate());
        holder.temp.setText(foreCast.getTemp()+"F");
        holder.tempMa.setText("Max:"+foreCast.getTempMax()+"F");
        holder.tempMi.setText("Min:"+foreCast.getTempMin()+"F");
        holder.humidity.setText(foreCast.getHumid()+"%");
        holder.typeDay.setText(foreCast.getWeather().getDescription());
        holder.icon = foreCast.getWeather().getIcon();
        Picasso.with(context).load("https://openweathermap.org/img/wn/"+holder.icon+".png").into(holder.iconView);

    }

    @Override
    public int getItemCount() {
        return this.foreCasters.size();
    }

    public static class ForecastViewHolder extends RecyclerView.ViewHolder{
        TextView date;
        TextView temp;
        TextView tempMa;
        TextView tempMi;
        TextView humidity;
        TextView typeDay;
        ImageView iconView;
        String icon;
        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            temp = itemView.findViewById(R.id.temperatures);
            tempMa = itemView.findViewById(R.id.tMax);
            tempMi = itemView.findViewById(R.id.tMin);
            humidity = itemView.findViewById(R.id.humVal);
            typeDay = itemView.findViewById(R.id.type);
            iconView = itemView.findViewById(R.id.iconView);
        }
    }
}
