package com.arunditti.android.popularmoviesstage1.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by arunditti on 4/26/18.
 */

public class MovieItem  implements Parcelable {

    public String movieId;
    public String mMovieTitle;
    public String mMovieReleaseDate;
    public String mOverview;
    public String mRating;
    public String mImagePath;

    public MovieItem(String id, String title, String releaseDate, String movieOverview, String rating, String imagePath) {
        this.movieId = id;
        this.mMovieTitle = title;
        this.mMovieReleaseDate = releaseDate;
        this.mOverview = movieOverview;
        this.mImagePath = imagePath;
        this.mRating = rating;
    }

    public MovieItem(Parcel in) {
        movieId = in.readString();
        mMovieTitle = in.readString();
        mMovieReleaseDate = in.readString();
        mOverview = in.readString();
        mImagePath = in.readString();
        mRating = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return movieId + mMovieTitle + mMovieReleaseDate + mOverview + mImagePath + mRating;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(movieId);
        dest.writeString(mMovieTitle);
        dest.writeString(mMovieReleaseDate);
        dest.writeString(mOverview);
        dest.writeString(mImagePath);
        dest.writeString(mRating);
    }

    public static final Parcelable.Creator<MovieItem> CREATOR = new Parcelable.Creator<MovieItem>() {
        @Override
        public MovieItem createFromParcel(Parcel parcel) {
            return new MovieItem(parcel);
        }

        @Override
        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }
    };

    public String getItemId() {
        return movieId;
    }

    public String getMovieTitle() {
        return mMovieTitle;
    }

    public String getmMovieReleaseDate() {
        return mMovieReleaseDate;
    }

    public String getmOverview() {
        return mOverview;
    }

    public String getmImagePath() {
        return mImagePath;
    }

    public String getmRating() {
        return mRating;
    }
}
