package com.nateprat.university.mobileplatformdevelopment.core.listeners;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
@RunWith(MockitoJUnitRunner.class)
public class CustomSwipeRefreshListenerTest {

    @Mock
    private SwipeRefreshLayout swipeRefreshLayout;

    private CustomSwipeRefreshListener underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new CustomSwipeRefreshListener(swipeRefreshLayout) {
            @Override
            protected boolean run() {
                return false;
            }

            @Override
            protected void onSuccess() {

            }

            @Override
            protected void onFailure() {

            }
        };
    }

    @Test
    public void onRefresh() {
        underTest.onRefresh();
        verify(swipeRefreshLayout).setRefreshing(eq(true));
        verify(swipeRefreshLayout).setRefreshing(eq(false));
    }

}