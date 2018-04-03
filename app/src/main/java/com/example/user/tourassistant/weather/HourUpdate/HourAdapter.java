package com.example.user.tourassistant.weather.HourUpdate;

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
 * Created by Ramzan Ullah on 10/22/2017.
 */

public class HourAdapter extends RecyclerView.Adapter<HourAdapter.HourViewHolder> {

    private Context context;
    private ArrayList<HourUpdate>hourUpdates;

    public HourAdapter(Context context, ArrayList<HourUpdate> hourUpdates) {
        this.context = context;
        this.hourUpdates = hourUpdates;
    }


    @Override
    public HourViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.hour_update_row,parent,false);
        return new HourViewHolder(v);
    }

    @Override
    public void onBindViewHolder(HourViewHolder holder, int position) {

        holder.timeTV.setText(hourUpdates.get(position).getTime());
        holder.tempTV.setText(hourUpdates.get(position).getTemp());
        Glide.with(holder.itemView.getContext()).load(hourUpdates.get(position).getConditionIcon()).into(holder.iconIV);
    }

    @Override
    public int getItemCount() {
        return hourUpdates.size();
    }

    public class HourViewHolder extends RecyclerView.ViewHolder {

        ImageView iconIV;
        TextView timeTV;
        TextView tempTV;

        public HourViewHolder(View itemView) {
            super(itemView);

            iconIV = itemView.findViewById(R.id.cond_icon);
            timeTV = itemView.findViewById(R.id.time);
            tempTV = itemView.findViewById(R.id.temp);

        }
    }
}
