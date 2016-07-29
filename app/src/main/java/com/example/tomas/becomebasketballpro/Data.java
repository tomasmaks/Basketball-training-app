package com.example.tomas.becomebasketballpro;

import android.graphics.Movie;
import android.os.Parcel;
import android.os.Parcelable;



/**
 * Created by Tomas on 29/07/2016.
 */
public class Data implements Parcelable {

    public static final String LOG_TAG = Data.class.getSimpleName();
    public static final float POSTER_ASPECT_RATIO = 1.5f;


    private long mId;

    private String mTitle;

    private String mPoster;

    private String mOverview;

    private String mUserRating;

    private String mReleaseDate;

    private String mBackdrop;

    // Only for createFromParcel
    private Data() {
    }



    public Data(long id, String title, String poster, String overview, String userRating,
                 String releaseDate, String backdrop) {
        mId = id;
        mTitle = title;
        mPoster = poster;
        mOverview = overview;
        mUserRating = userRating;
        mReleaseDate = releaseDate;
        mBackdrop = backdrop;
    }


    public static final Parcelable.Creator<Data> CREATOR = new Creator<Data>() {
        public Data createFromParcel(Parcel source) {
            Data data = new Data();
            data.mId = source.readLong();
            data.mTitle = source.readString();
            data.mPoster = source.readString();
            data.mOverview = source.readString();
            data.mUserRating = source.readString();
            data.mReleaseDate = source.readString();
            data.mBackdrop = source.readString();
            return data;
        }

        public Data[] newArray(int size) {
            return new Data[size];
        }
    };


    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(mId);
        parcel.writeString(mTitle);
        parcel.writeString(mPoster);
        parcel.writeString(mOverview);
        parcel.writeString(mUserRating);
        parcel.writeString(mReleaseDate);
        parcel.writeString(mBackdrop);
    }




}
