package com.example.user.tourassistant;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Ramzan Ullah on 10/24/2017.
 */

public class TopPlaceAdapter extends RecyclerView.Adapter<TopPlaceAdapter.TopPlaceViewHolder> {

    private Context context;
    private ArrayList<TopPlace>topPlaces;

    public TopPlaceAdapter(Context context, ArrayList<TopPlace> topPlaces) {
        this.context = context;
        this.topPlaces = topPlaces;
    }

    @Override
    public TopPlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.top_place_row,parent,false);
        return new TopPlaceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TopPlaceViewHolder holder, int position) {

        holder.placeTV.setText(topPlaces.get(position).getPlaceName());
        Glide.with(holder.itemView.getContext()).load(topPlaces.get(position).getPlaceImg()).asBitmap()
                .error(R.drawable.coxbazer).centerCrop().into(holder.imageIV);

    }

    @Override
    public int getItemCount() {
        return topPlaces.size();
    }

    public class TopPlaceViewHolder extends RecyclerView.ViewHolder {

        ImageView imageIV;
        TextView placeTV;


        public TopPlaceViewHolder(View itemView) {
            super(itemView);

            imageIV = itemView.findViewById(R.id.top_place_iv);
            placeTV = itemView.findViewById(R.id.top_place_title);

        }
    }
}
