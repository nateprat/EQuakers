package com.nateprat.university.mobileplatformdevelopment.core.listeners;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nateprat.university.mobileplatformdevelopment.core.concurrency.ThreadPools;

public abstract class CustomSwipeRefreshListener implements SwipeRefreshLayout.OnRefreshListener {

    private final SwipeRefreshLayout swipeRefreshLayout;

    protected CustomSwipeRefreshListener(SwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    @Override
    public synchronized void onRefresh() {
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
