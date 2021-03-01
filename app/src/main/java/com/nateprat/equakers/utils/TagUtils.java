package com.nateprat.equakers.utils;

public class TagUtils {

    public static String getTag(Object obj) {
        return obj.getClass().getSimpleName();
    }

}
