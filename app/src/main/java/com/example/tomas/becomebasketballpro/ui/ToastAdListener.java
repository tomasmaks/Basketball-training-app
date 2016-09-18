package com.example.tomas.becomebasketballpro.ui;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;

/**
 * Created by Tomas on 16/09/2016.
 */
public class ToastAdListener extends AdListener {

    private Context mContext;
    private String mErrorReason;

    public ToastAdListener(Context context) {
        this.mContext = context;
    }

    @Override
    public void onAdClosed() {
        super.onAdClosed();

        Toast.makeText(mContext, "onAdClosed() is called", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdFailedToLoad(int errorCode) {
        super.onAdFailedToLoad(errorCode);

        mErrorReason = "";
        switch (errorCode) {
            case AdRequest.ERROR_CODE_INTERNAL_ERROR:
                mErrorReason = "Internal error";
                break;
            case AdRequest.ERROR_CODE_INVALID_REQUEST:
                mErrorReason = "Invalid request";
                break;
            case AdRequest.ERROR_CODE_NETWORK_ERROR:
                mErrorReason = "Network error";
                break;
            case AdRequest.ERROR_CODE_NO_FILL:
                mErrorReason = "No fill";
                break;
        }
        Toast.makeText(mContext,
                "onAdFailedToLoad() is called.\nReason: " + getErrorReason(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdLeftApplication() {
        super.onAdLeftApplication();

        Toast.makeText(mContext, "onAdLeftApplication() is called", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdOpened() {
        super.onAdOpened();

        Toast.makeText(mContext, "onAdOpened() is called", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdLoaded() {
        super.onAdLoaded();

        Toast.makeText(mContext, "onAdLoaded() is called", Toast.LENGTH_SHORT).show();
    }

    public String getErrorReason() {
        return mErrorReason == null ? "" : mErrorReason;
    }
}
