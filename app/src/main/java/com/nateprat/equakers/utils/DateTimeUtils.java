package com.nateprat.equakers.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

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
