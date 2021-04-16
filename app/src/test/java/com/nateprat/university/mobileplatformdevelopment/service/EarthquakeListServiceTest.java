package com.nateprat.university.mobileplatformdevelopment.service;

import android.app.Activity;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nateprat.university.mobileplatformdevelopment.core.publish.BGSEarthquakeFeed;
import com.nateprat.university.mobileplatformdevelopment.model.holders.EarthquakeRecordAdapter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EarthquakeListServiceTest {

    @Mock
    private Activity activity;
    @Mock
    private EarthquakeRecordAdapter adapter;
    @Mock
    private SwipeRefreshLayout swipeRefreshLayout;

    private SwipeRefreshLayout.OnRefreshListener listener;

    private EarthquakeListService underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new EarthquakeListService(activity, adapter, swipeRefreshLayout);
        doAnswer(invoc -> {
            listener = (SwipeRefreshLayout.OnRefreshListener) invoc.getArguments()[0];
            return listener;
        }).when(swipeRefreshLayout).setOnRefreshListener(any(SwipeRefreshLayout.OnRefreshListener.class));
    }

    @After
    public void tearDown() throws Exception {
        listener = null;
    }

    @Test
    public void refresh() {
        underTest.init();
        underTest.refresh();
    }

    @Test
    public void init() {
        assertEquals(0, BGSEarthquakeFeed.getInstance().countObservers());
        assertNull(listener);
        underTest.init();
        assertNotNull(listener);
        assertEquals(1, BGSEarthquakeFeed.getInstance().countObservers());
    }

    @Test
    public void uninit() {
        underTest.init();
        assertNotNull(listener);
        assertEquals(1, BGSEarthquakeFeed.getInstance().countObservers());
        underTest.uninit();
        assertNull(listener);
        assertEquals(0, BGSEarthquakeFeed.getInstance().countObservers());
    }
}