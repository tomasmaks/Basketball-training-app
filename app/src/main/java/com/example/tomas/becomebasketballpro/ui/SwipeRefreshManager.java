package com.example.tomas.becomebasketballpro.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

//public class SwipeRefreshManager implements SwipeRefreshLayout.OnRefreshListener{
//    private final static int MINIMUM_SPINNER_VISIBLE_PERIOD_MS = 500;
//    private SwipeRefreshLayout mRefreshLayout;
//    private RefreshHandler mRefreshHandler;
//    private boolean mRefreshing;
//
//    public SwipeRefreshManager(SwipeRefreshLayout refreshLayout, RefreshHandler refreshHandler) {
//        mRefreshLayout = refreshLayout;
//        mRefreshHandler = refreshHandler;
//
//        mRefreshLayout.setOnRefreshListener(this);
//        mRefreshLayout.setEnabled(true);
//        ensureSpinnerIsVisibleOnFirstUse();
//        start();
//    }
//
//    public void start() {
//        mRefreshing = true;
//        updateRefreshSpinner();
//    }
//
//    public void stop() {
//        mRefreshing = false;
//        updateRefreshSpinner();
//    }
//
//    public void enable() {
//        mRefreshLayout.setEnabled(true);
//    }
//
//    public void disable() {
//        mRefreshLayout.setEnabled(false);
//    }
//
//    private void updateRefreshSpinner() {
//        if (mRefreshLayout == null) {
//            return;
//        }
//
//        mRefreshLayout.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mRefreshLayout.setRefreshing(mRefreshing);
//            }
//        }, MINIMUM_SPINNER_VISIBLE_PERIOD_MS);
//    }
//
//    @Override
//    public void onRefresh() {
//        mRefreshHandler.onRefresh();
//    }
//
//    private void ensureSpinnerIsVisibleOnFirstUse() {
//        // See Android Issue #77712
//        // https://code.google.com/p/android/issues/detail?id=77712
//        mRefreshLayout.measure(View.MEASURED_SIZE_MASK,View.MEASURED_HEIGHT_STATE_SHIFT);
//    }
//}