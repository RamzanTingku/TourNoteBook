package com.example.user.tourassistant.weather;

/**
 * Created by user on 23/10/2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.user.tourassistant.R;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.example.user.tourassistant.R.*;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {
    Context context;
    private List<Movie> moviesList;
    View mView;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title, year ;
        public ImageView genre;

        public MyViewHolder(View view) {
            super(view);
            mView = view;
            title = (TextView) view.findViewById(id.title);
            genre = (ImageView) view.findViewById(id.genre);
            year = (TextView) view.findViewById(id.year);
        }
    }


    public MoviesAdapter(List<Movie> moviesList,Context context) {
        this.moviesList = moviesList;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(layout.movie_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Movie movie = moviesList.get(position);
        holder.title.setText(movie.getTitle());
        holder.year.setText(movie.getYear());
        Glide.with(context).load("http:"+movie.getGenre()).into(holder.genre);
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }




}