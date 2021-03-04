package com.nateprat.equakers.utils;

import android.os.Build;

public class BuildUtils {

    public static boolean supportsAPI(int version) {
        return Build.VERSION.SDK_INT >= version;
    }

}
