package com.nateprat.equakers.api;

import android.util.Log;

import com.google.gson.internal.$Gson$Preconditions;
import com.nateprat.equakers.core.concurrency.ThreadPools;
import com.nateprat.equakers.service.RssBGSEarthquakeCallable;
import com.nateprat.equakers.utils.TagUtils;

import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class API<T> {

    private final Callable<T> callable;
    private final T returnIfError;

    protected API(Callable<T> callable, T returnIfError) {
        this.callable = callable;
        this.returnIfError = returnIfError;
    }

    protected API(Callable<T> callable) {
        this(callable, null);
    }

    protected Callable<T> getCallable() {
        return callable;
    }

    public T call() {
        Log.i(TagUtils.getTag(this), "Calling " + callable.getClass().getSimpleName());
        try {
            return ThreadPools.getInstance().submitTask(callable,  15L, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            Log.e(BritishGeologicalSurveyEarthquakeAPI.class.getSimpleName(), e.getMessage());
            return returnIfError;
        }
    }

}
