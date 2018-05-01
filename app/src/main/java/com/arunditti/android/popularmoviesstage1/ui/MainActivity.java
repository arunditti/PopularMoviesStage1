package com.arunditti.android.popularmoviesstage1.ui;

import android.content.SharedPreferences;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arunditti.android.popularmoviesstage1.R;
import com.arunditti.android.popularmoviesstage1.model.MovieItem;
import com.arunditti.android.popularmoviesstage1.ui.MovieAdapter.MovieAdapterOnClickHandler;
import com.arunditti.android.popularmoviesstage1.utils.JsonUtils;
import com.arunditti.android.popularmoviesstage1.utils.MoviePreferences;
import com.arunditti.android.popularmoviesstage1.utils.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapterOnClickHandler,
        LoaderCallbacks<ArrayList<MovieItem>>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    //Constant to uniquely identify loader
    private static final int MOVIE_LOADER_ID = 11;

    private MovieAdapter mAdapter;
    RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private Toast mToast;
    private ProgressBar mLoadingIndicator;

    private static boolean PREFERENCES_HAVE_BEEN_UPDATED = false;

    ArrayList<MovieItem> mMovieItems = new ArrayList<MovieItem>();

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

        mAdapter = new MovieAdapter(this, MainActivity.this, mMovieItems);

        //Link the adapter to the RecyclerView
        mRecyclerView.setAdapter(mAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        int loaderId = MOVIE_LOADER_ID;

        //LoaderCallbacks<ArrayList<MovieItem>> callback = MainActivity.this;
        Bundle bundleForLoader = null;

        LoaderCallbacks<ArrayList<MovieItem>> callbacks = MainActivity.this;

        //Ensure a loader is initialized and active. If the loader doesn't already exist, one is created and starts the loader. Othe
        getSupportLoaderManager().initLoader(loaderId, bundleForLoader, callbacks);

        //Register MainActivity as an OnPreferenceChangedListener
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }


    @Override
    public Loader<ArrayList<MovieItem>> onCreateLoader(int id, final Bundle args) {
        Log.i(LOG_TAG, "onCreateLoader is called");

        return new AsyncTaskLoader<ArrayList<MovieItem>>(this) {

            ArrayList<MovieItem> mMovieData = null;

            @Override
            protected void onStartLoading() {
                if(mMovieData != null) {
                    deliverResult(mMovieData);
                } else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public ArrayList<MovieItem> loadInBackground() {

                String movieQuery = MoviePreferences.getPreferredMovie(MainActivity.this);

                URL MovieRequestUrl = NetworkUtils.buildUrl(movieQuery);

                try {
                    String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(MovieRequestUrl);
                    ArrayList<MovieItem> simpleJsonMovieData = JsonUtils.getPopularMoviesDataFromJson(MainActivity.this, jsonMovieResponse);
                    return simpleJsonMovieData;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(ArrayList<MovieItem> data) {
                mMovieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieItem>> loader, ArrayList<MovieItem> data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (data != null) {
            showMovieDataView();
            mAdapter.updateMovieList(data);
        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieItem>> loader) {

    }

    private void showMovieDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the movie data is visible */
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(PREFERENCES_HAVE_BEEN_UPDATED) {
            Log.d(LOG_TAG, "onStart: Preferences were updated");
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
            PREFERENCES_HAVE_BEEN_UPDATED = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(LOG_TAG, "Preferences are updated");
        PREFERENCES_HAVE_BEEN_UPDATED = true;

    }
}