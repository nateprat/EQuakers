package com.nateprat.university.mobileplatformdevelopment.ui.map;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nateprat.mobileplatformdevelopment.R;
import com.nateprat.university.mobileplatformdevelopment.core.publish.BGSEarthquakeFeed;
import com.nateprat.university.mobileplatformdevelopment.model.EarthquakeRecord;
import com.nateprat.university.mobileplatformdevelopment.model.Location;
import com.nateprat.university.mobileplatformdevelopment.service.RedGreenInterpolationService;
import com.nateprat.university.mobileplatformdevelopment.ui.home.HomeFragment;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.nateprat.university.mobileplatformdevelopment.utils.MapUtils.DEFAULT_MAP_LAT_LNG;
import static com.nateprat.university.mobileplatformdevelopment.utils.MapUtils.DEFAULT_MAP_ZOOM;
import static com.nateprat.university.mobileplatformdevelopment.utils.MapUtils.getMarkerColourForMagnitude;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private MapView mapView;
    private GoogleMap gMap;
    private RecyclerView recyclerView;

    private final Map<MarkerOptions, EarthquakeRecord> markerRecordMap = new ConcurrentHashMap<>();

    // Defaults


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(this);

        try {
            recyclerView = v.findViewById(R.id.earthquakeMapList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return v;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.getUiSettings().setMyLocationButtonEnabled(false);
//        gMap.setMyLocationEnabled(true);

        updateMarkerRecordMap();
        for (MarkerOptions value : markerRecordMap.keySet()) {
            gMap.addMarker(value);
        }

        gMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder().target(DEFAULT_MAP_LAT_LNG).zoom(DEFAULT_MAP_ZOOM).build()));
    }


    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void updateMarkerRecordMap() {
        for (EarthquakeRecord earthquakeRecord : BGSEarthquakeFeed.getInstance().getRecords()) {
            Location location = earthquakeRecord.getEarthquake().getLocation();
            String title = location.getLocationString();
            LatLng latLng = location.getLatLng();
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(title).draggable(false).icon(getMarkerColourForMagnitude(earthquakeRecord.getEarthquake().getMagnitude()));
            markerRecordMap.put(markerOptions, earthquakeRecord);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.getId();
        return true;
    }

}