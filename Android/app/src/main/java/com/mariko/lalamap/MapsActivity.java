package com.mariko.lalamap;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.mariko.map.MapFragmentEx;
import com.mariko.map.MapStateListener;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;

public class MapsActivity extends Activity {

    private MapRootView mapRootView;
    private DestinationList destinationList;

    private Timer timer;

    private final MapData mapData = new MapData();
    private boolean destinationVisible;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mapRootView = (MapRootView) findViewById(R.id.mapRootView);
        setupMap();

        destinationList = (DestinationList) findViewById(R.id.destinationList);

        findViewById(R.id.showList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDestinationVisibility();

            }
        });

        /*
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mMap.animateCamera(CameraUpdateFactory.scrollBy(100, 0));
                        mapRootView.onCameraMove(-100, 0);

                    }
                });
            }
        }, 0, 2000);
        */

    }

    @Override
    public void onBackPressed() {
        if (destinationVisible) {
            changeDestinationVisibility();
            return;
        }
        super.onBackPressed();
    }

    private void changeDestinationVisibility() {
        destinationList.setVisibility(View.VISIBLE);
        if (destinationVisible) {
            YoYo.with(Techniques.SlideOutDown).duration(300).playOn(destinationList);
        } else {
            YoYo.with(Techniques.SlideInUp).duration(300).playOn(destinationList);
        }
        destinationVisible = !destinationVisible;
    }

    @Override
    protected void onResume() {
        super.onResume();
        GApp.sInstance.getBus().register(this);
        setupMap();
    }

    @Override
    protected void onPause() {
        GApp.sInstance.getBus().unregister(this);
        super.onPause();
    }

    @Subscribe
    public void destinationSelected(DestinationList.DestinationSelectedEvent event) {
        if (destinationVisible) {
            changeDestinationVisibility();
        }
        mapRootView.plainTo(mapData, event.item);
    }

    private void aaa() {

        MarkerOptions markerOptions = new MarkerOptions();

        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.tiger);

        markerOptions.icon(bitmapDescriptor);

        markerOptions.position(new LatLng(50.398194, 30.488018));

        final Marker marker = mapData.map.addMarker(markerOptions);

        //Make the marker bounce
        final Handler handler = new Handler();

        final long startTime = SystemClock.uptimeMillis();
        final long duration = 20000;

        Projection proj = mapData.map.getProjection();
        final LatLng markerLatLng = marker.getPosition();
        Point startPoint = proj.toScreenLocation(markerLatLng);
        startPoint.offset(0, -200);
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);

        final Interpolator interpolator = new BounceInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - startTime;
                float t = interpolator.getInterpolation((float) elapsed / duration);
                double lng = t * markerLatLng.longitude + (1 - t) * startLatLng.longitude;
                double lat = t * markerLatLng.latitude + (1 - t) * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                marker.setRotation(marker.getRotation() + 1);

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    private void setupMap() {

        if (mapData.map == null) {

            MapFragmentEx mapFragment = (MapFragmentEx) (getFragmentManager().findFragmentById(R.id.map));
            mapData.map = mapFragment.getMap();

            if (mapData.map != null) {

                mapData.map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                mapData.map.getUiSettings().setRotateGesturesEnabled(false);
                mapData.map.getUiSettings().setZoomGesturesEnabled(false);

                //aaa();

                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        mapRootView.start();
                    }
                });

                new MapStateListener(mapFragment, this) {

                    @Override
                    protected void doScroll(int x, int y, boolean zoom, long duration) {
                        if (zoom) {
                            mapRootView.showMapItems(false);
                        } else {
                            mapRootView.scrollCamera(x, y, duration);
                        }

                    }

                    @Override
                    protected void onCameraChange(CameraPosition cameraPosition) {

                        mapData.position = cameraPosition;
                        mapData.projection = mapData.map.getProjection();
                        mapRootView.onCameraChange(mapData);
                    }
                };
            }
        }
    }
}
