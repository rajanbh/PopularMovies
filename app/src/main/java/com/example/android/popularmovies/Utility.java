package com.example.android.popularmovies;


import android.net.Uri;

public class Utility {

    public static String buildUri(String imagePath){
        final String BASE_URL = "http://image.tmdb.org/t/p/";
        final String IMAGE_SIZE = "w185/";
        String finalUrl = BASE_URL+IMAGE_SIZE+imagePath;
        Uri builtUri = Uri.parse(BASE_URL).buildUpon().
                appendPath(IMAGE_SIZE).appendPath(imagePath).build();

        return finalUrl;
    }
}
