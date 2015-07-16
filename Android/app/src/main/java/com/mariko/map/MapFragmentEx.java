package com.mariko.map;

import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.maps.MapFragment;

public class MapFragmentEx extends MapFragment {

    private View mOriginalContentView;
    private GestureDetectorCompat mDetector;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {

        mOriginalContentView = super.onCreateView(inflater, parent, savedInstanceState);

        ViewGroup touchView = new FrameLayout(getActivity()) {

            @Override
            public boolean dispatchTouchEvent(MotionEvent event) {

                if (mDetector != null) {
                    mDetector.onTouchEvent(event);
                }

                return super.dispatchTouchEvent(event);
            }

        };
        touchView.addView(mOriginalContentView);

        return touchView;
    }

    public void setGestureDetector(GestureDetectorCompat gestureDetector) {
        mDetector = gestureDetector;
    }

    @Override
    public View getView() {
        return mOriginalContentView;
    }
}
