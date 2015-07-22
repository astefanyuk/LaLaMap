package com.mariko.lalamap;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;


public class ViewAnimated extends RelativeLayout {

    private View mainView;
    private GestureDetector gestureDetector;

    private Integer animationWidth;
    private AnimatorSet animatorSet;

    public static interface Listener {
        void show(boolean show);
    }

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public ViewAnimated(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(getContext()).inflate(R.layout.animated_view, this);

        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {

                if (getTranslationX() < 0) {
                    if (listener != null) {
                        listener.show(true);
                    }
                }

                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

                if (Math.abs(distanceX) >= Math.abs(distanceY)) {
                    if (listener != null) {
                        listener.show(distanceX <= 0);
                    }
                }

                return false;
            }
        });
    }

    public int getShadowWidth() {
        return (int) (GApp.sInstance.DPI * 20);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (mainView != null && gestureDetector.onTouchEvent(ev)) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void addMainView(View mainView) {

        this.mainView = mainView;

        ((ViewGroup) findViewById(R.id.viewRoot)).addView(mainView, 0);
    }

    public void show(int x) {
        show(x, true);
    }

    public void show(int x, boolean animated) {

        if (!animated) {

            if (animatorSet != null) {
                animatorSet.cancel();
            }

            animatorSet = null;
            this.animationWidth = x;

            setTranslationX(x);

            return;
        }

        if (animatorSet != null && animatorSet.isRunning()) {
            return;
        }

        if (animationWidth == null || x != animationWidth) {

            this.animationWidth = x;

            if (animatorSet != null) {
                animatorSet.cancel();
            }

            ValueAnimator translationHeight = ObjectAnimator.ofFloat(this, "translationX", getTranslationX(), x);

            animatorSet = new AnimatorSet();
            translationHeight.setDuration(300).setInterpolator(new AccelerateDecelerateInterpolator());
            animatorSet.play(translationHeight);
            animatorSet.start();
        }
    }
}
