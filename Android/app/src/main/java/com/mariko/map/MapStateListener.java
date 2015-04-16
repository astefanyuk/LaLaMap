package com.mariko.map;

import android.app.Activity;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;

public abstract class MapStateListener {

    private GoogleMap mMap;


    public MapStateListener(MapFragmentEx touchableMapFragment, Activity activity) {
        this.mMap = touchableMapFragment.getMap();

        this.mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

                MapStateListener.this.onCameraChange(mMap.getCameraPosition());

            }
        });

        GestureDetectorCompat gestureDetector = new GestureDetectorCompat(activity, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                doScroll(-(int) distanceX, -(int) distanceY, false, 0);
                return false;

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                final float d = 6;
                int scrollX = (int) (velocityX / d);
                int scrollY = (int) (velocityY / d);
                mMap.animateCamera(CameraUpdateFactory.scrollBy(scrollX, scrollY));
                doScroll((int) (scrollX * 0.9f), (int) (scrollY * 0.9f), false, 500);

                return true;
            }
        });
        touchableMapFragment.setGestureDetector(gestureDetector);
    }

    protected abstract void doScroll(int x, int y, boolean zoom, long duration);

    protected abstract void onCameraChange(CameraPosition cameraPosition);

}
