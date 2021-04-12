package com.nateprat.university.mobileplatformdevelopment.ui.daterange.section;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nateprat.university.mobileplatformdevelopment.core.publish.EarthquakeObserver;
import com.nateprat.university.mobileplatformdevelopment.model.EarthquakeRecord;
import com.nateprat.university.mobileplatformdevelopment.model.Location;
import com.nateprat.university.mobileplatformdevelopment.utils.MapUtils;

import java.util.concurrent.locks.ReentrantLock;

import static com.nateprat.university.mobileplatformdevelopment.utils.MapUtils.DEFAULT_MAP_LAT_LNG;
import static com.nateprat.university.mobileplatformdevelopment.utils.MapUtils.DEFAULT_MAP_ZOOM;
import static com.nateprat.university.mobileplatformdevelopment.utils.MapUtils.getMarkerColourForMagnitude;

public class MapDateRangeSection extends DateRangeSection implements OnMapReadyCallback {

    private Activity activity;
    private MapView mapView;
    private GoogleMap gMap;
    private EarthquakeObserver observer;
    private ReentrantLock gMapLock = new ReentrantLock();

    public MapDateRangeSection(Context context, CardView cardView, TextView nameTextView, Activity activity, MapView mapView, EarthquakeObserver observer) {
        super(context, cardView, nameTextView);
        this.activity = activity;
        this.mapView = mapView;
        this.mapView.getMapAsync(this);
        this.observer = observer;
        this.gMapLock.lock();
    }

    @Override
    public void setCurrentEarthquakeRecord(EarthquakeRecord currentEarthquakeRecord) {
        super.setCurrentEarthquakeRecord(currentEarthquakeRecord);
        if (gMap == null) {
            return;
        }
//        ThreadPools.getInstance().submitTask(() -> {
        try {
            for (EarthquakeRecord earthquakeRecord : observer.getRecords()) {
                Location location = earthquakeRecord.getEarthquake().getLocation();
                final String title = location.getLocationString();
                final LatLng latLng = location.getLatLng();
                BitmapDescriptor iconColour;
                if (earthquakeRecord.equals(currentEarthquakeRecord)) {
                    iconColour = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET);
                } else {
                    iconColour = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
                }
                activity.runOnUiThread(() -> gMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(title)
                        .draggable(false)
                        .icon(iconColour)));
            }
            gMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder().target(DEFAULT_MAP_LAT_LNG).zoom(DEFAULT_MAP_ZOOM).build()));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        });
    }

    public MapView getMapView() {
        return mapView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        MapUtils.moveMapToDefault(gMap);
        gMapLock.unlock();
    }
}
