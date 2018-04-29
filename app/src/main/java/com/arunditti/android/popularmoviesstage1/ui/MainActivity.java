package com.arunditti.android.popularmoviesstage1.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arunditti.android.popularmoviesstage1.R;
import com.arunditti.android.popularmoviesstage1.model.MovieItem;
import com.arunditti.android.popularmoviesstage1.utils.JsonUtils;
import com.arunditti.android.popularmoviesstage1.utils.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private MovieAdapter mAdapter;
    RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private Toast mToast;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        //Set the RecyclerView to its corresponding view
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_list);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        //Create a fake list of movies
        ArrayList<MovieItem> movieItems = new ArrayList<MovieItem>();


        mAdapter = new MovieAdapter(this, MainActivity.this, movieItems);

        //Link the adapter to the RecyclerView
        mRecyclerView.setAdapter(mAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
    }

    public void updateMovieList() {
        FetchPopularMoviesTask movieTask = new FetchPopularMoviesTask();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String sort_by = prefs.getString(getString(R.string.pref_sort_by_key),
                getString(R.string.pref_sort_by_default));
        movieTask.execute(sort_by);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovieList();
    }

    private void showWeatherDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(MovieItem movieClicked) {

        Intent intentToStartDetailActivity = new Intent(MainActivity.this, DetailActivity.class);
        intentToStartDetailActivity.putExtra("MovieItem", movieClicked);
        startActivity(intentToStartDetailActivity);
    }


    public class FetchPopularMoviesTask extends AsyncTask<String, Void, ArrayList<MovieItem>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<MovieItem> doInBackground(String... params) {

            URL weatherRequestUrl = NetworkUtils.buildUrl();

            try {
                String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);
                ArrayList<MovieItem> simpleJsonWeatherData = JsonUtils.getPopularMoviesDataFromJson(MainActivity.this, jsonWeatherResponse);
                return simpleJsonWeatherData;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<MovieItem> result) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (result != null) {
                showWeatherDataView();
                mAdapter.updateMovieList(result);
            } else {
                showErrorMessage();
            }
        }
    }
}