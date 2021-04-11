package com.nateprat.university.mobileplatformdevelopment.core.listeners;

import android.util.Log;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nateprat.university.mobileplatformdevelopment.core.concurrency.ThreadPools;
import com.nateprat.university.mobileplatformdevelopment.utils.TagUtils;

public abstract class CustomSwipeRefreshListener implements SwipeRefreshLayout.OnRefreshListener {

    private final SwipeRefreshLayout swipeRefreshLayout;

    protected CustomSwipeRefreshListener(SwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public synchronized void onRefresh() {
        Log.d(TagUtils.getTag(this), "onRefresh() called for CustomSwipeRefreshListener");
        swipeRefreshLayout.setRefreshing(true);
        ThreadPools.getInstance().submitTask(() -> {
            if (run()) {
                onSuccess();
            } else {
                onFailure();
            }
        });
        swipeRefreshLayout.setRefreshing(false);
    }

    protected abstract boolean run();
    protected abstract void onSuccess();
    protected abstract void onFailure();

}
