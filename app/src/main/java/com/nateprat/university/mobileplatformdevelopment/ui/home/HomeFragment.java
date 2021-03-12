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
import com.nateprat.university.mobileplatformdevelopment.api.BritishGeologicalSurveyEarthquakeAPI;
import com.nateprat.university.mobileplatformdevelopment.core.listeners.CustomSwipeRefreshListener;
import com.nateprat.university.mobileplatformdevelopment.core.publish.BGSEarthquakeFeed;
import com.nateprat.university.mobileplatformdevelopment.core.publish.EarthquakeObserver;
import com.nateprat.university.mobileplatformdevelopment.model.EarthquakeRecord;
import com.nateprat.university.mobileplatformdevelopment.model.holders.EarthquakeRecordAdapter;
import com.nateprat.university.mobileplatformdevelopment.ui.SortItemListDialogFragment;
import com.nateprat.university.mobileplatformdevelopment.ui.custom.sorting.EarthquakeRecordsSorting;
import com.nateprat.university.mobileplatformdevelopment.utils.TagUtils;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class HomeFragment extends Fragment {

    private Context context;
    private RecyclerView recyclerView;
    private Chip sortChip;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EarthquakeRecordAdapter earthquakeListAdapter;
    private EarthquakeObserver earthquakeObserver = new EarthquakeObserver();
    private static SwipeRefreshLayout.OnRefreshListener swipeListener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        context = container.getContext();
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        swipeRefreshLayout = root.findViewById(R.id.swipe_layout);
        recyclerView = root.findViewById(R.id.mainActivityEarthquakeList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        earthquakeListAdapter = new EarthquakeRecordAdapter(context);
        recyclerView.setAdapter(earthquakeListAdapter);
        sortChip = root.findViewById(R.id.sortChip);
        BGSEarthquakeFeed.getInstance().addObserver(earthquakeObserver);


        initUI();
        return root;
    }

    public static SwipeRefreshLayout.OnRefreshListener getSwipeListener() {
        return swipeListener;
    }

    private void initUI() {
        initialiseEarthquakeSwipeLayout();
        initialiseSortButton();
    }

    private List<EarthquakeRecord> retrieveRecords() {
        return BritishGeologicalSurveyEarthquakeAPI.getInstance().call();
    }

    private void initialiseEarthquakeSwipeLayout() {
        swipeListener = new CustomSwipeRefreshListener(swipeRefreshLayout) {
            @Override
            protected boolean run() {
                earthquakeObserver.requestUpdate();
                return true;
            }

            @Override
            protected void onSuccess() {
                Log.i(TagUtils.getTag(this), "Mapping earthquake list (size=" + earthquakeObserver.getRecords().size() + ") to scroller view.");
                getActivity().runOnUiThread(() -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        EarthquakeRecordsSorting.getWorkingComparator().ifPresent(earthquakeObserver.getRecords()::sort);
                    }
                    earthquakeListAdapter.submitList(earthquakeObserver.getRecords());
                });
            }

            @Override
            protected void onFailure() {
                Toast.makeText(getContext(), "Failed to update earthquake list", Toast.LENGTH_SHORT);
            }
        };
        swipeRefreshLayout.setOnRefreshListener(swipeListener);
        swipeListener.onRefresh();
    }

    private void initialiseSortButton() {
        sortChip.setOnClickListener(item -> {
            Log.i(TagUtils.getTag(this), "Opened sorting list for HomeFragment");
            SortItemListDialogFragment.newInstance().showNow(getChildFragmentManager(), "sortList");
        });
    }

}