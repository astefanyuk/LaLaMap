package com.mariko.map;

import android.app.Activity;
import android.graphics.Point;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.Timer;
import java.util.TimerTask;

public abstract class MapStateListener {

    private GoogleMap mMap;
    private Activity mActivity;
    private Timer timer;
    private boolean releasePressed;
    private LatLng mapLeftTop;

    private float zoom;
    private boolean zoomChanged;

    public MapStateListener(MapFragmentEx touchableMapFragment, Activity activity) {
        this.mMap = touchableMapFragment.getMap();
        this.mActivity = activity;

        this.mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

                stopScroll();

                mapLeftTop = null;
                zoomChanged = false;
                zoom = cameraPosition.zoom;

                MapStateListener.this.onCameraChange(mMap.getCameraPosition());
            }
        });

        touchableMapFragment.setTouchListener(new MapFragmentEx.OnTouchListener() {
            @Override
            public void onTouch() {
                startScroll();
            }

            @Override
            public void onRelease() {
                releasePressed = true;
            }
        });
    }

    private void startScroll() {
        if (timer == null || releasePressed) {

            stopScroll();

            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (mActivity.isFinishing()) {
                        stopScroll();
                        return;
                    }
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            LatLng location = mMap.getProjection().fromScreenLocation(new Point(0, 0));

                            if (mapLeftTop == null || !location.equals(mapLeftTop)) {

                                if (mapLeftTop != null) {
                                    Point p = mMap.getProjection().toScreenLocation(mapLeftTop);
                                    doScroll(p.x, p.y, zoomChanged || (zoom != mMap.getCameraPosition().zoom));
                                }

                                mapLeftTop = location;


                            }
                        }
                    });
                }
            }, 0, 10);
        }
    }

    private void stopScroll() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    protected abstract void doScroll(int x, int y, boolean zoom);

    protected abstract void onCameraChange(CameraPosition cameraPosition);

}
