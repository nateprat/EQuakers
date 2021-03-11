package com.nateprat.university.mobileplatformdevelopment.utils;

public class TagUtils {

    public static String getTag(Object obj) {
        return obj.getClass().getSimpleName();
    }

}
