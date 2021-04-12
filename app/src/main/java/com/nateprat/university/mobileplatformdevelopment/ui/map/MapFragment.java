package com.nateprat.university.mobileplatformdevelopment.ui.map;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nateprat.mobileplatformdevelopment.R;
import com.nateprat.university.mobileplatformdevelopment.activity.EarthquakeRecordScrollingActivity;
import com.nateprat.university.mobileplatformdevelopment.core.publish.BGSEarthquakeFeed;
import com.nateprat.university.mobileplatformdevelopment.model.EarthquakeRecord;
import com.nateprat.university.mobileplatformdevelopment.model.holders.EarthquakeRecordAdapter;
import com.nateprat.university.mobileplatformdevelopment.service.EarthquakeListService;
import com.nateprat.university.mobileplatformdevelopment.utils.MapMarkerUtils;
import com.nateprat.university.mobileplatformdevelopment.utils.MapUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private MapView mapView;
    private GoogleMap gMap;
    private EarthquakeListService earthquakeListService;
    private Marker markerDoubleClick;

    private final Map<EarthquakeRecord, MarkerOptions> markerRecordMap = new ConcurrentHashMap<>();

    // Defaults


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(this);

        RecyclerView recyclerView = v.findViewById(R.id.earthquakeMapList);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            EarthquakeRecordAdapter earthquakeListAdapter = new EarthquakeRecordAdapter(getContext(), record -> view -> {
                gMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder().target(record.getEarthquake().getLocation().getLatLng()).zoom(10F).build()));
                return true;
            });
            SwipeRefreshLayout swipeRefreshLayout = v.findViewById(R.id.swipe_layout_map);
            earthquakeListService = new EarthquakeListService(getActivity(), earthquakeListAdapter, swipeRefreshLayout);
            recyclerView.setAdapter(earthquakeListAdapter);
            earthquakeListService.init();
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
        if (earthquakeListService != null) {
            earthquakeListService.refresh();
        }
        gMap = googleMap;
        gMap.getUiSettings().setMyLocationButtonEnabled(true);

        updateMarkerRecordMap();
        for (MarkerOptions value : markerRecordMap.values()) {
            gMap.addMarker(value);
        }

        gMap.setOnMarkerClickListener(this);
        gMap.setOnMapLongClickListener(view -> {
            if (markerDoubleClick != null) {
                markerDoubleClick.hideInfoWindow();
                markerDoubleClick = null;
            }
            MapUtils.moveMapToDefault(gMap);
        });

        MapUtils.moveMapToDefault(gMap);
    }


    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
        if (markerDoubleClick != null) {
            markerDoubleClick.showInfoWindow();
        }
    }


    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
        if (earthquakeListService != null) {
            earthquakeListService.uninit();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void updateMarkerRecordMap() {
        for (EarthquakeRecord earthquakeRecord : BGSEarthquakeFeed.getInstance().getRecords()) {
            markerRecordMap.put(earthquakeRecord, MapMarkerUtils.createEarthquakeMarker(earthquakeRecord));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
        public boolean onMarkerClick(Marker marker) {
        if (markerDoubleClick == null || !markerDoubleClick.getPosition().equals(marker.getPosition())) {
            markerDoubleClick = marker;
            marker.showInfoWindow();
        } else {
            markerRecordMap.entrySet().stream()
                    .filter(entry -> entry.getValue().getPosition().equals(marker.getPosition()))
                    .findFirst().ifPresent(entry -> EarthquakeRecordScrollingActivity.startActivity(getContext(), entry.getKey()));
        }
        return true;
    }

}