package com.nateprat.university.mobileplatformdevelopment.utils;

import android.graphics.Color;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.nateprat.university.mobileplatformdevelopment.service.RedGreenInterpolationService;

public class MapUtils {

    private MapUtils() {}

    public static BitmapDescriptor getMarkerColourForMagnitude(double magnitude) {
        float[] hsv = new float[3];
        Color.colorToHSV(RedGreenInterpolationService.colourAtPoint((int)(magnitude * 10)), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }


}
