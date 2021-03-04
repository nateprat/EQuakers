package com.nateprat.equakers.core.task;

import android.content.Context;
import android.content.Intent;

import com.nateprat.equakers.activity.MainActivity;

public class BottomNavBarListTask extends Task {

    public BottomNavBarListTask(Context context) {
        super(() -> {
            if (!(context instanceof MainActivity)) {
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        });
    }

}
