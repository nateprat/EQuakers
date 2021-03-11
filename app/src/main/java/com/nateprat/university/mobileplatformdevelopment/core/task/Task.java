package com.nateprat.university.mobileplatformdevelopment.core.task;

import android.util.Log;

import com.nateprat.university.mobileplatformdevelopment.core.concurrency.ThreadPools;
import com.nateprat.university.mobileplatformdevelopment.utils.TagUtils;

public abstract class Task {

    private final Runnable task;
    private final boolean async;

    protected Task(Runnable task) {
        this(task, false);
    }

    public Task(Runnable task, boolean async) {
        this.task = task;
        this.async = async;
    }

    public void run() {
        Log.d(TagUtils.getTag(this), "Running task: " + getClass().getSimpleName());
        if (async) {
            ThreadPools.getInstance().submitTask(task);
        } else {
            task.run();
        }
    }

}
