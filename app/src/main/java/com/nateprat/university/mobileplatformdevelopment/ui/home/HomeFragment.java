package com.nateprat.university.mobileplatformdevelopment.ui.home;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.chip.Chip;
import com.nateprat.mobileplatformdevelopment.R;
import com.nateprat.university.mobileplatformdevelopment.core.listeners.CustomSwipeRefreshListener;
import com.nateprat.university.mobileplatformdevelopment.core.publish.BGSEarthquakeFeed;
import com.nateprat.university.mobileplatformdevelopment.core.publish.EarthquakeObserver;
import com.nateprat.university.mobileplatformdevelopment.model.EarthquakeRecord;
import com.nateprat.university.mobileplatformdevelopment.model.holders.EarthquakeRecordAdapter;
import com.nateprat.university.mobileplatformdevelopment.service.EarthquakeListService;
import com.nateprat.university.mobileplatformdevelopment.ui.SortItemListDialogFragment;
import com.nateprat.university.mobileplatformdevelopment.ui.custom.sorting.EarthquakeRecordsSorting;
import com.nateprat.university.mobileplatformdevelopment.utils.DateTimeUtils;
import com.nateprat.university.mobileplatformdevelopment.utils.TagUtils;

import java.util.List;

public class HomeFragment extends Fragment {

    private Context context;
    private RecyclerView recyclerView;
    private Chip sortChip;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EarthquakeRecordAdapter earthquakeListAdapter;
    private static EarthquakeListService earthquakeListService;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        context = container.getContext();
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        swipeRefreshLayout = root.findViewById(R.id.swipe_layout);
        recyclerView = root.findViewById(R.id.mainActivityEarthquakeList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        earthquakeListAdapter = new EarthquakeRecordAdapter(context);
        earthquakeListService = new EarthquakeListService(getActivity(), earthquakeListAdapter, swipeRefreshLayout);
        recyclerView.setAdapter(earthquakeListAdapter);
        sortChip = root.findViewById(R.id.sortChip);


        initUI();
        // FIXME: 15/03/2021 | needs 100ms sleep to allow swipeListener.onRefresh() for some reason
        DateTimeUtils.sleep(150);
        earthquakeListService.refresh();
        return root;
    }

    public static EarthquakeListService getEarthquakeListService() {
        return earthquakeListService;
    }

    private void initUI() {
        initialiseSortButton();
        earthquakeListService.init();
    }

    @Override
    public void onDestroy() {
        earthquakeListService.uninit();
        super.onDestroy();
    }

    private void initialiseSortButton() {
        sortChip.setOnClickListener(item -> {
            Log.i(TagUtils.getTag(this), "Opened sorting list for HomeFragment");
            SortItemListDialogFragment.newInstance().showNow(getChildFragmentManager(), "sortList");
        });
    }

}