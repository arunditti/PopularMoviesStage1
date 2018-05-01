package com.arunditti.android.popularmoviesstage1.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.databinding.DataBindingUtil;
import com.arunditti.android.popularmoviesstage1.databinding.ActivityDetailBinding;

import com.arunditti.android.popularmoviesstage1.R;
import com.arunditti.android.popularmoviesstage1.model.MovieItem;
import com.squareup.picasso.Picasso;

/**
 * Created by arunditti on 4/26/18.
 */

public class DetailActivity extends AppCompatActivity {

    private MovieItem mCurrentMovieItem;

    //Declare an ActivityDetailBinding field called mDetailBinding
    private ActivityDetailBinding mDetailBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intentThatStartedThisActivity = getIntent();
        mCurrentMovieItem = intentThatStartedThisActivity.getParcelableExtra("MovieItem");

        mDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        mDetailBinding.movieTitle.setText(mCurrentMovieItem.getMovieTitle());
        mDetailBinding.movieReleaseDate.setText(mCurrentMovieItem.getmMovieReleaseDate());
        mDetailBinding.movieRating.setText(mCurrentMovieItem.getmRating());
        mDetailBinding.movieOverview.setText(mCurrentMovieItem.getmOverview());

        Picasso.with(this)
                .load(mCurrentMovieItem.getmImagePath())
        .into(mDetailBinding.movieImage);

    }
}
