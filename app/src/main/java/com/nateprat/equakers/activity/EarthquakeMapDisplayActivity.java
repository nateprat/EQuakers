package com.nateprat.equakers.activity;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nateprat.equakers.R;
import com.nateprat.equakers.core.concurrency.ThreadPools;
import com.nateprat.equakers.core.task.BottomNavBarMapTask;
import com.nateprat.equakers.model.EarthquakeRecord;
import com.nateprat.equakers.model.Location;
import com.nateprat.equakers.model.holders.EarthquakeRecordAdapter;
import com.nateprat.equakers.ui.custom.BottomNavigationBar;
import com.nateprat.equakers.utils.TagUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class EarthquakeMapDisplayActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, Activity {

    private static final String KEY_RECORDS = "key_records";

    private static final long timeout = 15L;
    private static final TimeUnit timeoutUnit = TimeUnit.SECONDS;

    //UI
    private BottomNavigationBar bottomNavigationBar;

    private RecyclerView earthquakeList;
    private EarthquakeRecordAdapter earthquakeListAdapter;
    private List<EarthquakeRecord> earthquakeRecords;
    private GoogleMap mMap;

    // UK DEFAULTS
    private static final LatLng DEFAULT_MAP_LAT_LNG = new LatLng(54.418929, -4.196777);
    private static final float DEFAULT_MAP_ZOOM = 5F;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        earthquakeRecords = (List<EarthquakeRecord>) getIntent().getSerializableExtra(KEY_RECORDS);
        earthquakeList = findViewById(R.id.earthquakeList);
        setContentView(R.layout.activity_earthquake_map_display);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        Objects.requireNonNull(mapFragment).getMapAsync(this);
        earthquakeListAdapter = new EarthquakeRecordAdapter(this);
        earthquakeListAdapter.submitList(earthquakeRecords);
        initialiseUI();
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
        mMap = googleMap;
        Future<List<MarkerOptions>> future = ThreadPools.getInstance().submitTask(this::getMarkerOptionsForRecords);
        List<MarkerOptions> markerOptions = new CopyOnWriteArrayList<>();
        try {
            markerOptions.addAll(future.get(timeout, timeoutUnit));
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
            Log.e(TagUtils.getTag(this), e.getMessage());
        }

        for (MarkerOptions markerOption : markerOptions) {
            mMap.addMarker(markerOption);
        }

        // Move to default position (Over UK)
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder().target(DEFAULT_MAP_LAT_LNG).zoom(DEFAULT_MAP_ZOOM).build()));
    }

    private List<MarkerOptions> getMarkerOptionsForRecords() {
        List<MarkerOptions> tmp = new CopyOnWriteArrayList<>();
        for (EarthquakeRecord earthquakeRecord : earthquakeRecords) {
            Location location = earthquakeRecord.getEarthquake().getLocation();
            String title = location.getLocationString();
            LatLng latLng = location.getLatLng();
            tmp.add(new MarkerOptions().position(latLng).title(title).draggable(false));
        }
        return tmp;
    }

    public static void startActivity(Context context, ArrayList<EarthquakeRecord> earthquakeRecords) {
        Intent intent = new Intent(context, EarthquakeMapDisplayActivity.class);
        intent.putExtra(KEY_RECORDS, earthquakeRecords);
        context.startActivity(intent);
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
//        marker.get
        return true;
    }

    @Override
    public void onBackPressed() {
        if (bottomNavigationBar.getBottomNavigationView().getSelectedItemId() == R.id.bottom_navigation_list) {
            super.onBackPressed();
        } else {
            bottomNavigationBar.getBottomNavigationView().setSelectedItemId(R.id.bottom_navigation_list);
        }
    }

    @Override
    public void initialiseUI() {
        initialiseBottomNavigationBar();
    }

    private void initialiseBottomNavigationBar() {
        bottomNavigationBar = new BottomNavigationBar(findViewById(R.id.earthquakeMapBottomNavigation), this);
    }

}