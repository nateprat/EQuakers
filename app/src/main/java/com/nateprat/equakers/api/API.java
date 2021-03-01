package com.nateprat.equakers.api;

import com.nateprat.equakers.core.concurrency.ThreadPools;

public abstract class API<T> {

    public abstract T call(Object... args);


}
