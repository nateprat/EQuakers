package com.nateprat.equakers.ui.custom;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nateprat.equakers.R;
import com.nateprat.equakers.core.task.Task;
import com.nateprat.equakers.utils.TagUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BottomNavigationBar {

    private Map<Integer, Task> itemTasks;
    private BottomNavigationView bottomNavigationView;

    public BottomNavigationBar(@NonNull BottomNavigationView bottomNavigationView) {
        this.itemTasks = new ConcurrentHashMap<>();
        this.bottomNavigationView = bottomNavigationView;
        this.bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Task task = itemTasks.get(item.getItemId());
            if (task != null) {
                task.run();
                return true;
            } else {
                Log.e(TagUtils.getTag(this), "ERROR: could not find task for item: " + item.getTitle());
                return false;
            }
        });
    }

    public Task getTaskForItemId(Integer itemId) {
        return itemTasks.get(itemId);
    }

    public BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }

    public void addItemRunnable(Integer itemId, Task task) {
        addItemRunnable(itemId, task, false);
    }

    public void addItemRunnable(Integer itemId, Task task, boolean replace) {
        if (!itemTasks.containsKey(itemId) || replace) {
            itemTasks.put(itemId, task);
        } else {
            Log.e(TagUtils.getTag(this), "Error adding task, task already exists for itemId: " + itemId);
        }
    }

}
