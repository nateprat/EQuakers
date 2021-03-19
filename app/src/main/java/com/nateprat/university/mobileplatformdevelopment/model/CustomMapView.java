package com.nateprat.university.mobileplatformdevelopment.model;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nateprat.university.mobileplatformdevelopment.utils.MapUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CustomMapView implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private MapView mapView;
    private GoogleMap googleMap;
    private List<MarkerOptions> markerOptionList;

    public CustomMapView(MapView mapView) {
        this.mapView = mapView;
        this.markerOptionList = new ArrayList<>();
    }

    public void setMarkers(List<MarkerOptions> markerOptionList) {
        this.markerOptionList = markerOptionList;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    public void updateMarkersOnMap() {
        if (googleMap != null) {
            for (MarkerOptions markerOption : markerOptionList) {
                googleMap.addMarker(markerOption);
            }
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder().target(MapUtils.DEFAULT_MAP_LAT_LNG).zoom(MapUtils.DEFAULT_MAP_ZOOM).build()));
        }
    }


}
