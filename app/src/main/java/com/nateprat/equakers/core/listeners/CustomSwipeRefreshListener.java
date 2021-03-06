package com.nateprat.equakers.core.listeners;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nateprat.equakers.core.concurrency.ThreadPools;

import java.net.MalformedURLException;

public abstract class CustomSwipeRefreshListener implements SwipeRefreshLayout.OnRefreshListener {

    private final SwipeRefreshLayout swipeRefreshLayout;

    protected CustomSwipeRefreshListener(SwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    @Override
    public void onRefresh() {
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
