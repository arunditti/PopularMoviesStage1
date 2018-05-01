package com.arunditti.android.popularmoviesstage1.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.arunditti.android.popularmoviesstage1.R;

/**
 * Created by arunditti on 5/1/18.
 */

public class MoviePreferences {

    public static String getPreferredMovie(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String keyForMovie = context.getString(R.string.pref_sort_by_key);
        String defaultMovie = context.getString(R.string.pref_sort_by_default_value);
        return sharedPreferences.getString(keyForMovie, defaultMovie);
    }
}
