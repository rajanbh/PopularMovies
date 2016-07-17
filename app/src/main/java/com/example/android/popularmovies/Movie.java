package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by rajan on 7/14/2016.
 */
public class Movie implements Parcelable{

    private String originalTitle;
    private String imagePath;
    private String moviePlot;
    private String releaseDate;
    private String userRating;

    public Movie(){}

    public Movie(String originalTitle, String imagePath, String moviePlot, String releaseDate, String userRating) {
        this.originalTitle = originalTitle;
        this.imagePath = imagePath;
        this.moviePlot = moviePlot;
        this.releaseDate = releaseDate;
        this.userRating = userRating;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getMoviePlot() {
        return moviePlot;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getUserRating() {
        return userRating;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "originalTitle='" + originalTitle + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", moviePlot='" + moviePlot + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", userRating='" + userRating + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(originalTitle);
        parcel.writeString(imagePath);
        parcel.writeString(moviePlot);
        parcel.writeString(userRating);
        parcel.writeString(releaseDate);
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){

        @Override
        public Object createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Object[] newArray(int i) {
            return new Movie[i];
        }
    };

    private Movie(Parcel in){
        originalTitle = in.readString();
        imagePath = in.readString();
        moviePlot = in.readString();
        userRating = in.readString();
        releaseDate = in.readString();
    }
}
