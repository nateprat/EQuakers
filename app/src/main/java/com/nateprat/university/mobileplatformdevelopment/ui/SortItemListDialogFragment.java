package com.nateprat.university.mobileplatformdevelopment.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.nateprat.mobileplatformdevelopment.R;
import com.nateprat.university.mobileplatformdevelopment.ui.custom.sorting.EarthquakeRecordSorter;
import com.nateprat.university.mobileplatformdevelopment.ui.custom.sorting.EarthquakeRecordsSorting;
import com.nateprat.university.mobileplatformdevelopment.ui.home.HomeFragment;
import com.nateprat.university.mobileplatformdevelopment.utils.TagUtils;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     SortItemListDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class SortItemListDialogFragment extends BottomSheetDialogFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_SORT_MAP = "sort_map";

    // TODO: Customize parameters
    public static SortItemListDialogFragment newInstance() {
        final SortItemListDialogFragment fragment = new SortItemListDialogFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item_list_dialog_list_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new SortItemAdapter());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        HomeFragment.getEarthquakeListService().refresh();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        final TextView description;
        final Switch sortEnabledSwitch;
        final ToggleButton sortOrderButton;

        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.fragment_item_list_dialog_list_dialog_item, parent, false));
            description = itemView.findViewById(R.id.sortItemText);
            sortEnabledSwitch = itemView.findViewById(R.id.sortSwitch);
            sortOrderButton = itemView.findViewById(R.id.toggleButton);
        }

    }

    private class SortItemAdapter extends RecyclerView.Adapter<ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.description.setText(EarthquakeRecordsSorting.getSorterMap().get(position).getKey());
            holder.sortOrderButton.setOnCheckedChangeListener(sortOrderListener(position));
            holder.sortOrderButton.setChecked(EarthquakeRecordsSorting.getSorterMap().get(position).isAscending());
            holder.sortOrderButton.setVisibility(View.INVISIBLE);
            holder.sortEnabledSwitch.setOnCheckedChangeListener(sortEnabledListener(holder.sortOrderButton, position));
            holder.sortEnabledSwitch.setChecked(EarthquakeRecordsSorting.getSorterMap().get(position).isEnabled());
        }

        private CompoundButton.OnCheckedChangeListener sortOrderListener(int position) {
            return (buttonView, isChecked) -> EarthquakeRecordsSorting.getSorterMap().get(position).setAscending(isChecked);
        }

        private CompoundButton.OnCheckedChangeListener sortEnabledListener(ToggleButton sortOrderButton, int position) {
            return (buttonView, isChecked) -> {
                EarthquakeRecordsSorting.getSorterMap().get(position).setEnabled(isChecked);
                sortOrderButton.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
            };
        }

        @Override
        public int getItemCount() {
            return EarthquakeRecordsSorting.getSorterMap().size();
        }

    }

}