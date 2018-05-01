package com.arunditti.android.popularmoviesstage1.utils;

import com.arunditti.android.popularmoviesstage1.model.MovieItem;
import com.arunditti.android.popularmoviesstage1.ui.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by arunditti on 4/26/18.
 */

public class JsonUtils {
    private final String LOG_TAG = JsonUtils.class.getSimpleName();

    public static ArrayList<MovieItem> getPopularMoviesDataFromJson(MainActivity mainActivity, String PopularMoviesJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String PMD_ID = "id";
        final String PMD_LIST = "results";
        final String PMD_TITLE = "title";
        final String PMD_POSTER = "poster_path";
        final String PMD_PICTURE_PATH = "https://image.tmdb.org/t/p/";
        final String PMD_OVERVIEW = "overview";
        final String PMD_RATING = "vote_average";
        final String PMD_RELEASE_DATE = "release_date";
        final String PMD_PICTURE_SIZE    = "w185";

        JSONObject PopularMoviesJson = new JSONObject(PopularMoviesJsonStr);
        JSONArray moviesArray = PopularMoviesJson.getJSONArray(PMD_LIST);
        ArrayList<MovieItem> movieItems = new ArrayList<MovieItem>();

        movieItems.clear();
        for (int i = 0; i < moviesArray.length(); i++) {
            JSONObject popularMovies = moviesArray.getJSONObject(i);
            String movieId = popularMovies.getString(PMD_ID);
            String title = popularMovies.getString(PMD_TITLE);
            String releaseDate = popularMovies.getString(PMD_RELEASE_DATE);
            String movieOverview= popularMovies.getString(PMD_OVERVIEW);
            String rating= popularMovies.getString(PMD_RATING);
            String imagePath = PMD_PICTURE_PATH + PMD_PICTURE_SIZE + popularMovies.getString(PMD_POSTER);
            movieItems.add(new MovieItem(movieId, title, releaseDate, movieOverview, rating, imagePath));
        }

        return movieItems;

    }
}