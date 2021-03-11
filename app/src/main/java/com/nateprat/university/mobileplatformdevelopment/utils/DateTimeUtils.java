package com.nateprat.university.mobileplatformdevelopment.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {

    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy/MM/dd");

    private DateTimeUtils() throws IllegalAccessException {
        throw new IllegalAccessException("DateTimeUtils should not be constructed.");
    }

    public static Date parse(String localDateTimeString, SimpleDateFormat simpleDateFormat) throws ParseException {
        return simpleDateFormat.parse(localDateTimeString);
    }

}
