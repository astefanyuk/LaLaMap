package com.mariko.lalamap;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.mariko.map.MapFragmentEx;
import com.mariko.map.MapStateListener;

import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends Activity {

    private GoogleMap mMap;
    private MapRootView mapRootView;

    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mapRootView = (MapRootView) findViewById(R.id.mapRootView);
        setUpMapIfNeeded();

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
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {

        if (mMap == null) {

            MapFragmentEx touchableMapFragment = (MapFragmentEx) (getFragmentManager().findFragmentById(R.id.map));
            mMap = touchableMapFragment.getMap();

            if (mMap != null) {

                new MapStateListener(touchableMapFragment, this) {
                    @Override
                    public void onMapTouched() {
                        mapRootView.showMapItems(false);
                    }
                };

                setUpMap();
            }
        }
    }

    private void setUpMap() {

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                mapRootView.onCameraChange(mMap, cameraPosition, mMap.getProjection());
            }
        });
    }
}
