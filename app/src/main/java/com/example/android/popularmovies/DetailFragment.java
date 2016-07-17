package com.example.android.popularmovies;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {


    public DetailFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_detail, container, false);
        Intent intent = getActivity().getIntent();
        if (intent!=null){
            Movie movie = (Movie) intent.getParcelableExtra("selectedMovie");

            String releaseDate = movie.getReleaseDate();

            String releaseYear="";
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = format.parse(releaseDate);
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
                releaseYear = yearFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ((TextView)rootview.findViewById(R.id.movie_detail_title_textview)).setText(movie.getOriginalTitle());

            ImageView imageView = (ImageView) rootview.findViewById(R.id.movie_detail_imageview);
            String imageUrl = Utility.buildUri(movie.getImagePath());
            Picasso.with(getContext()).load(imageUrl)
                    .placeholder(R.drawable.movie_icon)
                    .error(R.drawable.movie_icon)
                    .into(imageView);

            ((TextView)rootview.findViewById(R.id.movie_detail_plot_textview)).setText(movie.getMoviePlot());

            ((TextView)rootview.findViewById(R.id.movie_detail_user_rating_textview)).setText(movie.getUserRating()+"/10");
            ((TextView)rootview.findViewById(R.id.movie_detail_year_text_view)).setText(releaseYear);
        }
        return rootview;
    }
}
