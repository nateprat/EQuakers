package com.nateprat.equakers.model.holders;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.nateprat.equakers.R;
import com.nateprat.equakers.activity.EarthquakeRecordScrollingActivity;
import com.nateprat.equakers.model.EarthquakeRecord;
import com.nateprat.equakers.utils.TagUtils;

public class EarthquakeRecordAdapter extends ListAdapter<EarthquakeRecord, EarthquakeRecordViewHolder> {

    private final Context context;

    public EarthquakeRecordAdapter(Context context) {
        super(new DiffUtil.ItemCallback<EarthquakeRecord>() {
            @Override
            public boolean areItemsTheSame(@NonNull EarthquakeRecord oldItem, @NonNull EarthquakeRecord newItem) {
                return oldItem == newItem;
            }

            @Override
            public boolean areContentsTheSame(@NonNull EarthquakeRecord oldItem, @NonNull EarthquakeRecord newItem) {
                return oldItem.equals(newItem);
            }
        });
        this.context = context;
    }

    @NonNull
    @Override
    public EarthquakeRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.earthquake_record, parent, false);
        return new EarthquakeRecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EarthquakeRecordViewHolder holder, int position) {
        holder.bind(getItem(position));
        holder.itemView.setOnClickListener(v -> {
            EarthquakeRecordScrollingActivity.startActivity(context,  getItem(position));
            Log.i(TagUtils.getTag(this), "Clicked on " + holder.getItemId());
        });
    }
}
