package com.example.user.tourassistant.weather.WeekUpdate;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.tourassistant.R;

import java.util.ArrayList;

/**
 * Created by Ramzan Ullah on 10/23/2017.
 */

public class WeeklyAdapter extends RecyclerView.Adapter<WeeklyAdapter.WeeklyViewHolder> {

    private Context context;
    private ArrayList<WeeklyUpdate> weeklyUpdates;

    public WeeklyAdapter(Context context, ArrayList<WeeklyUpdate> weeklyUpdates) {
        this.context = context;
        this.weeklyUpdates = weeklyUpdates;
    }

    @Override
    public WeeklyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.weekly_row,parent,false);

        return new WeeklyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(WeeklyViewHolder holder, int position) {

        holder.dayTV.setText(weeklyUpdates.get(position).getDay());
        holder.tempMaxTV.setText(weeklyUpdates.get(position).getMaxtemp());
        holder.tempMinTV.setText(weeklyUpdates.get(position).getMintemp());
        holder.tempUnitTV.setText(weeklyUpdates.get(position).getTempUnit());
        Glide.with(holder.itemView.getContext()).load(weeklyUpdates.get(position).getConditionIcon()).into(holder.iconIV);

    }

    @Override
    public int getItemCount() {
        return weeklyUpdates.size();
    }

    public class WeeklyViewHolder extends RecyclerView.ViewHolder {

        ImageView iconIV;
        TextView tempMaxTV, tempMinTV, dayTV, tempUnitTV;


        public WeeklyViewHolder(View itemView) {
            super(itemView);

            iconIV = itemView.findViewById(R.id.day_image);
            tempMaxTV = itemView.findViewById(R.id.day_high_temp);
            tempMinTV = itemView.findViewById(R.id.day_low_temp);
            dayTV = itemView.findViewById(R.id.day);
            tempUnitTV = itemView.findViewById(R.id.day_temp_unit);


        }
    }
}
