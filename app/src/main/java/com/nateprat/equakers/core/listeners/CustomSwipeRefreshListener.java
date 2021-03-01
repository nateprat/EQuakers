package com.nateprat.equakers.core.listeners;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.net.MalformedURLException;

public abstract class CustomSwipeRefreshListener implements SwipeRefreshLayout.OnRefreshListener {

    private final SwipeRefreshLayout swipeRefreshLayout;

    protected CustomSwipeRefreshListener(SwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        if (run()) {
            onSuccess();
        } else {
            onFailure();
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    protected abstract boolean run();
    protected abstract void onSuccess();
    protected abstract void onFailure();

}
