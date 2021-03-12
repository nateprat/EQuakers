package com.nateprat.university.mobileplatformdevelopment.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.nateprat.mobileplatformdevelopment.R;
import com.nateprat.university.mobileplatformdevelopment.core.publish.BGSEarthquakeFeed;
import com.nateprat.university.mobileplatformdevelopment.ui.custom.sorting.EarthquakeRecordsSorting;
import com.nateprat.university.mobileplatformdevelopment.ui.home.HomeFragment;

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
        HomeFragment.getSwipeListener().onRefresh();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        final ImageView image;
        final TextView description;
        final Switch sortSwitch;
        final ImageButton reverseToggle;

        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            // TODO: Customize the item layout
            super(inflater.inflate(R.layout.fragment_item_list_dialog_list_dialog_item, parent, false));
            description = itemView.findViewById(R.id.sortItemText);
            sortSwitch = itemView.findViewById(R.id.sortSwitch);
            image = itemView.findViewById(R.id.sortImage);
            reverseToggle = itemView.findViewById(R.id.imageButton);
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
            int drawableId = getResources().getIdentifier(EarthquakeRecordsSorting.getSorterMap().get(position).getIconPath(), "drawable", getContext().getPackageName());
            holder.image.setImageResource(drawableId);
            holder.description.setText(EarthquakeRecordsSorting.getSorterMap().get(position).getKey());
            holder.reverseToggle.setOnClickListener(item -> {
                EarthquakeRecordsSorting.getSorterMap().get(position).switchOrder();
                if (EarthquakeRecordsSorting.getSorterMap().get(position).isAscending()) {
                    holder.reverseToggle.setImageDrawable(getResources().getDrawable(android.R.drawable.arrow_up_float));
                } else {
                    holder.reverseToggle.setImageDrawable(getResources().getDrawable(android.R.drawable.arrow_down_float));
                }
            });
            if (EarthquakeRecordsSorting.getSorterMap().get(position).isAscending()) {
                holder.reverseToggle.setImageDrawable(getResources().getDrawable(android.R.drawable.arrow_up_float));
            } else {
                holder.reverseToggle.setImageDrawable(getResources().getDrawable(android.R.drawable.arrow_down_float));
            }

            holder.sortSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                EarthquakeRecordsSorting.getSorterMap().get(position).setEnabled(isChecked);
                if (isChecked) {
                    holder.reverseToggle.setVisibility(View.VISIBLE);
                } else {
                    holder.reverseToggle.setVisibility(View.INVISIBLE);
                }
            });
            holder.sortSwitch.setChecked(EarthquakeRecordsSorting.getSorterMap().get(position).isEnabled());
        }

        @Override
        public int getItemCount() {
            return EarthquakeRecordsSorting.getSorterMap().size();
        }

    }

}