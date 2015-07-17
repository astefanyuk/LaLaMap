package com.mariko.lalamap;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by AStefaniuk on 17.07.2015.
 */
public class ViewFling extends View {

    private final GestureDetector detector;

    public ViewFling(Context context, AttributeSet attrs) {
        super(context, attrs);

        detector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                Log.d("ABC", "Fling");
                return true;
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (this.detector != null) {
            detector.onTouchEvent(ev);
            super.dispatchTouchEvent(ev);
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);
        //
        if (this.detector != null) {
            return this.detector.onTouchEvent(ev);
        }
        return false;
    }
}
