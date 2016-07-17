package com.example.android.popularmovies;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {

    private final String LOG_TAG = MovieFragment.class.getSimpleName();
    private ArrayList<Movie> movieList;
    MovieAdapter mAdapter;

    public MovieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.moviefragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateMovies() {

        FetchData weatherTask = new FetchData();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOrder = preferences.getString(getString(R.string.pref_movie_sort_order_key),
                getString(R.string.pref_movie_sort_order_popular));
        weatherTask.execute(sortOrder);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_main,container,false);

        mAdapter = new MovieAdapter(getActivity(),new ArrayList<Movie>());

        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_movie);
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Movie movie = movieList.get(position);
                Intent intent = new Intent(getActivity(),DetailActivity.class).
                        putExtra("selectedMovie", (Parcelable) movie);
                startActivity(intent);
            }
        });
        return rootView;
    }

    public class FetchData extends AsyncTask<String, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String movieJsonStr = null;
            try {
                //construct url for themoviedb query
                final String BASE_URL = "http://api.themoviedb.org/3/movie/";
                final String MOVIE_TYPE = params[0];
                final String API_KEY = "api_key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon().
                        appendPath(MOVIE_TYPE).appendQueryParameter(API_KEY, BuildConfig.API_KEY).build();

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, builtUri.toString());
                //create request and open connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                //Read the input stream into a string.
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieJsonStr = buffer.toString();

                try {
                    return getMovieDataFromJson(movieJsonStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error" + e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> result) {
            if (result != null && !result.isEmpty()) {
                movieList = result;
                mAdapter.clear();
                for (Movie m :result){
                    mAdapter.add(m);
                }

            }
        }

        public ArrayList<Movie> getMovieDataFromJson(String movieJsonStr) throws JSONException {
            final String M_RESULTS = "results";
            final String M_TITLE = "original_title";
            final String M_IMAGE_PATH = "poster_path";
            final String M_PLOT = "overview";
            final String M_USER_RATING = "vote_average";
            final String M_RELEASE_DATE = "release_date";

            JSONObject movieJsonObj = new JSONObject(movieJsonStr);
            JSONArray movieArray = movieJsonObj.getJSONArray(M_RESULTS);

            ArrayList<Movie> movieList = new ArrayList<>();
            for (int i = 0; i < movieArray.length(); i++) {
                String originalTitle;
                String imagePath;
                String moviePlot;
                String releaseDate;
                String userRating;

                JSONObject singleMovieObj = movieArray.getJSONObject(i);
                originalTitle = singleMovieObj.getString(M_TITLE);
                imagePath = singleMovieObj.getString(M_IMAGE_PATH);
                moviePlot = singleMovieObj.getString(M_PLOT);
                releaseDate = singleMovieObj.getString(M_RELEASE_DATE);
                userRating = singleMovieObj.getString(M_USER_RATING);

                movieList.add(new Movie(originalTitle,imagePath,moviePlot,releaseDate,userRating));
            }

            return movieList;

        }
    }

}
