package com.mariko.map;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

public class MapFragmentEx extends MapFragment {

    private View mOriginalContentView;
    private OnTouchListener onTouchListener;

    public interface OnTouchListener {
        public void onTouch();
        public void onRelease();
    }

    public void setTouchListener(OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {

        mOriginalContentView = super.onCreateView(inflater, parent, savedInstanceState);

        ViewGroup touchView = new FrameLayout(getActivity()){
            @Override
            public boolean dispatchTouchEvent(MotionEvent event) {


                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        onTouchListener.onTouch();
                        break;
                    case MotionEvent.ACTION_UP:
                        onTouchListener.onRelease();
                        break;
                }

                return super.dispatchTouchEvent(event);
            }
        };
        touchView.addView(mOriginalContentView);

        return touchView;
    }

    @Override
    public View getView() {
        return mOriginalContentView;
    }
}
