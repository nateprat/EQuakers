package com.nateprat.equakers.service;

import android.graphics.Color;
import android.util.Log;

import com.nateprat.equakers.utils.TagUtils;

import org.apache.commons.lang3.Range;

public class RedGreenInterpolationService {

    private static final double MAX_VALUE = 100;
    private static final double MIN_VALUE = 0;
    private static final Range<Double> range = Range.between(MIN_VALUE, MAX_VALUE);

    private RedGreenInterpolationService() { }

    public static int colourAtPoint(int point) {
        int color = Color.MAGENTA;
        if (range.contains((double)point)) {
            int red = (int)(point / 100.0 * 255);
            int green = 255 - red;
            int blue = 0;

            color = Color.rgb(red, green, blue);
        } else {
            Log.w(TagUtils.getTag(RedGreenInterpolationService.class), "Point not between range 1-100, " + point);
        }
        return color;
    }

}