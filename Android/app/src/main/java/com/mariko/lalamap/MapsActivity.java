package com.mariko.lalamap;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.mariko.map.MapFragmentEx;
import com.mariko.map.MapStateListener;

import java.util.Timer;

public class MapsActivity extends Activity {

    private GoogleMap mMap;
    private MapRootView mapRootView;

    private Timer timer;

    private final MapData mapData = new MapData();
    private LatLng l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mapRootView = (MapRootView) findViewById(R.id.mapRootView);
        setupMap();

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
    protected void onResume() {
        super.onResume();
        setupMap();
    }

    private void setupMap() {

        if (mMap == null) {

            MapFragmentEx mapFragment = (MapFragmentEx) (getFragmentManager().findFragmentById(R.id.map));
            mMap = mapFragment.getMap();

            if (mMap != null) {

                mMap.getUiSettings().setRotateGesturesEnabled(false);
                //mMap.getUiSettings().setZoomGesturesEnabled(false);
                //mMap.getUiSettings().setScrollGesturesEnabled(false);

                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        mapRootView.start();
                    }
                });

                new MapStateListener(mapFragment, this) {

                    @Override
                    protected void doScroll(int x, int y, boolean zoom) {
                        Log.d("ABC", "X=" + x + " total=" + mapRootView.getTranslationX() + x);
                        if (zoom) {
                            mapRootView.showMapItems(false);
                        } else {
                            mapRootView.setTranslationX((int) (mapRootView.getTranslationX() + x));
                            mapRootView.setTranslationY((int) (mapRootView.getTranslationY() + y));
                        }

                    }

                    @Override
                    protected void onCameraChange(CameraPosition cameraPosition) {

                        mapData.map = mMap;
                        mapData.position = cameraPosition;
                        mapData.projection = mMap.getProjection();
                        mapRootView.onCameraChange(mapData);
                    }
                };
            }
        }
    }
}
