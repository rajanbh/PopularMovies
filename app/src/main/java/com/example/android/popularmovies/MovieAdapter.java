package com.example.android.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 *
 */
public class MovieAdapter extends ArrayAdapter<Movie> {

    public MovieAdapter(Context context, ArrayList<Movie> moviesList) {
        super(context,0, moviesList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridItemView = convertView;
        if (gridItemView == null){
            gridItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.grid_item_movie,parent,false
            );
        }

        Movie currentMovie = getItem(position);

        String imageUrl = Utility.buildUri(currentMovie.getImagePath());

        ImageView posterImageView = (ImageView)gridItemView.findViewById(R.id.grid_item_movie_imageview);
        Picasso.with(getContext()).load(imageUrl).into(posterImageView);

        return gridItemView;
    }

}
