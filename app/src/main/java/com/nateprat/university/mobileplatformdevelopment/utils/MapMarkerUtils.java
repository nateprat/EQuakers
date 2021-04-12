package com.nateprat.university.mobileplatformdevelopment.utils;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nateprat.university.mobileplatformdevelopment.model.EarthquakeRecord;
import com.nateprat.university.mobileplatformdevelopment.model.Location;

import static com.nateprat.university.mobileplatformdevelopment.utils.MapUtils.getMarkerColourForMagnitude;

public class MapMarkerUtils {

    private MapMarkerUtils() {}

    public static MarkerOptions createEarthquakeMarker(EarthquakeRecord earthquakeRecord) {
        Location location = earthquakeRecord.getEarthquake().getLocation();
        String title = location.getLocationString();
        LatLng latLng = location.getLatLng();
        return new MarkerOptions().position(latLng).title(title).draggable(false).icon(getMarkerColourForMagnitude(earthquakeRecord.getEarthquake().getMagnitude()));
    }


}
