package com.mariko.lalamap;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.gson.Gson;
import com.mariko.map.MapFragmentEx;
import com.mariko.map.MapStateListener;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;

public class MapsActivity extends Activity {

    private GoogleMap mMap;
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
        setupMap();
    }

    private void setupMap() {

        if (mMap == null) {

            MapFragmentEx mapFragment = (MapFragmentEx) (getFragmentManager().findFragmentById(R.id.map));
            mMap = mapFragment.getMap();

            if (mMap != null) {

                mMap.getUiSettings().setRotateGesturesEnabled(false);
                mMap.getUiSettings().setZoomGesturesEnabled(false);

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
