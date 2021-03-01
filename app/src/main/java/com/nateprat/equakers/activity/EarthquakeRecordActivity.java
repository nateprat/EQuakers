package com.nateprat.equakers.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nateprat.equakers.R;
import com.nateprat.equakers.model.EarthquakeRecord;
import com.nateprat.equakers.ui.custom.MagnitudeCircle;
import com.nateprat.equakers.utils.TagUtils;

import java.util.Date;

public class EarthquakeRecordActivity extends AppCompatActivity {


    // keys
    private static final String KEY_LOCATION = "key_location";
    private static final String KEY_MAGNITUDE = "key_magnitude";
    private static final String KEY_DEPTH = "key_depth";
    private static final String KEY_URL = "key_url";
    private static final String KEY_DATE = "key_date";
    private static final String KEY_LONG = "key_long";
    private static final String KEY_LAT = "key_lat";

    // maps
    private static final int MAP_ZOOM_LEVEL = 8;
    private static final int MAP_ANIMATION_LENGTH_MS = 200;

    // label views
    private TextView depthLabel;
    private TextView urlLabel;
    private TextView latLabel;
    private TextView longLabel;

    // value views
    private TextView locationValue;
    private TextView dateValue;
    private TextView depthValue;
    private Button urlButton;
    private TextView latValue;
    private TextView longValue;
    private SupportMapFragment mapFragment;
    private MagnitudeCircle magnitudeValue;

    private CardView cardView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake_details);
        depthLabel = findViewById(R.id.depthLabel);
        latLabel = findViewById(R.id.latLabel);
        longLabel = findViewById(R.id.longLabel);
        locationValue = findViewById(R.id.locationValue);
        dateValue = findViewById(R.id.dateValue);
        depthValue = findViewById(R.id.depthValue);
        latValue = findViewById(R.id.latValue);
        longValue = findViewById(R.id.longValue);
        urlButton = findViewById(R.id.urlButton);
        urlButton.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getIntent().getStringExtra(KEY_URL)));
            startActivity(browserIntent);
        });
        magnitudeValue = new MagnitudeCircle(findViewById(R.id.magnitudeValue));

        cardView = findViewById(R.id.locationCardView);



        setText();
        setMapLocation();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setMapLocation() {
        LatLng placeLocation = new LatLng(getIntent().getDoubleExtra(KEY_LAT, 0), getIntent().getDoubleExtra(KEY_LONG, 0));
        Log.i(TagUtils.getTag(this), "Setting map location for " + placeLocation.toString());
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(googleMap -> {
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            try {
                googleMap.setMyLocationEnabled(false);
            } catch (SecurityException se) {
                Log.e(TagUtils.getTag(this), se.getMessage());
            }

            //Edit the following as per you needs
            googleMap.setTrafficEnabled(false);
            googleMap.setIndoorEnabled(false);
            googleMap.setBuildingsEnabled(false);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            //


            Marker placeMarker = googleMap.addMarker(new MarkerOptions().position(placeLocation)
                    .title(locationValue.getText().toString()));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(placeLocation, MAP_ZOOM_LEVEL), MAP_ANIMATION_LENGTH_MS, null);
        });

    }

    @SuppressLint("SetTextI18n")
    private void setText() {
        locationValue.setText(getIntent().getStringExtra(KEY_LOCATION));
        magnitudeValue.setMagnitude(getIntent().getDoubleExtra(KEY_MAGNITUDE, 0));
        depthValue.setText(Double.toString(getIntent().getDoubleExtra(KEY_DEPTH, 0)));
        dateValue.setText(((Date)getIntent().getSerializableExtra(KEY_DATE)).toString());
        longValue.setText(Double.toString(getIntent().getDoubleExtra(KEY_LONG, 0)));
        latValue.setText(Double.toString(getIntent().getDoubleExtra(KEY_LAT, 0)));
    }

    public static void startActivity(Context context, EarthquakeRecord earthquakeRecord) {
        Intent intent = new Intent(context, EarthquakeRecordActivity.class);
        intent.putExtra(KEY_LOCATION, earthquakeRecord.getEarthquake().getLocation().getLocationString());
        intent.putExtra(KEY_MAGNITUDE, earthquakeRecord.getEarthquake().getMagnitude());
        intent.putExtra(KEY_DEPTH, earthquakeRecord.getEarthquake().getDepth());
        intent.putExtra(KEY_URL, earthquakeRecord.getUrl().toString());
        intent.putExtra(KEY_DATE, earthquakeRecord.getEarthquake().getDate());
        intent.putExtra(KEY_LONG, earthquakeRecord.getEarthquake().getLocation().getLatLng().longitude);
        intent.putExtra(KEY_LAT, earthquakeRecord.getEarthquake().getLocation().getLatLng().latitude);
        context.startActivity(intent);
    }




}
