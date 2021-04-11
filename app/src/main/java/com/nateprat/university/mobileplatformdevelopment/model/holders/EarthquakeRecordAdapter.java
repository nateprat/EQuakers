package com.nateprat.university.mobileplatformdevelopment.model.holders;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.nateprat.mobileplatformdevelopment.R;
import com.nateprat.university.mobileplatformdevelopment.activity.EarthquakeRecordScrollingActivity;
import com.nateprat.university.mobileplatformdevelopment.model.EarthquakeRecord;
import com.nateprat.university.mobileplatformdevelopment.utils.TagUtils;

import java.util.function.Function;

public class EarthquakeRecordAdapter extends ListAdapter<EarthquakeRecord, EarthquakeRecordViewHolder> {

    private final Context context;
    private final Function<EarthquakeRecord, View.OnLongClickListener> onLongClickListenerFunction;

    public EarthquakeRecordAdapter(Context context) {
        this(context, null);
    }

    public EarthquakeRecordAdapter(Context context, Function<EarthquakeRecord, View.OnLongClickListener> onLongClickListenerFunction) {
        super(new DiffUtil.ItemCallback<EarthquakeRecord>() {
            @Override
            public boolean areItemsTheSame(@NonNull EarthquakeRecord oldItem, @NonNull EarthquakeRecord newItem) {
                return oldItem == newItem;
//                return false;
            }

            @Override
            public boolean areContentsTheSame(@NonNull EarthquakeRecord oldItem, @NonNull EarthquakeRecord newItem) {
                return oldItem.equals(newItem);
//                return false;
            }
        });
        this.context = context;
        this.onLongClickListenerFunction = onLongClickListenerFunction;
    }

    @NonNull
    @Override
    public EarthquakeRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.earthquake_record, parent, false);
        return new EarthquakeRecordViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull EarthquakeRecordViewHolder holder, int position) {
        holder.bind(getItem(position));
        holder.itemView.setOnClickListener(v -> {
            EarthquakeRecordScrollingActivity.startActivity(context,  getItem(position));
            Log.i(TagUtils.getTag(this), "Clicked on " + holder.getItemId());
        });
        if (onLongClickListenerFunction != null) {
            holder.itemView.setOnLongClickListener(onLongClickListenerFunction.apply(getItem(position)));
        }
    }
}
