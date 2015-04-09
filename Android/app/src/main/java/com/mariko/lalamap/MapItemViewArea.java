package com.mariko.lalamap;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Size;

import com.mariko.animation.AnimatorPath;
import com.mariko.animation.PathEvaluator;
import com.mariko.animation.PathPoint;

import java.lang.reflect.Method;

/**
 * Created by AStefaniuk on 09.04.2015.
 */
public class MapItemViewArea extends MapItemView {

    private boolean rotateIncrease;
    private int width;
    private int height;

    public MapItemViewArea(Context context, AttributeSet attrs) {
        super(context, attrs);

        //setBackgroundColor(Color.RED);
    }

    @Override
    protected void doAnimation() {
        doAnimation(0, 0);
    }

    private void doAnimation(int x, int y) {

        cancelAnimationSet();

        if (width <= 0 || height <= 0) {
            return;
        }

        int maxX = (int) (width / 3.0f);
        int maxY = (int) (height / 3.0f);

        /*
        AnimatorPath path = new AnimatorPath();
        path.moveTo(300, 100);
        path.curveTo(
                300, 50,
                200, 60,
                0, 20);
                */


        // Set up the path we're animating along
        AnimatorPath path = new AnimatorPath();
        path.moveTo(x, y);

        if (view.getScaleX() < 0) {
            path.curveTo(
                    x, (int) (maxY * 0.5f),
                    x - (int) (maxX * 0.6f), (int) (maxY * 0.6f),
                    x - maxX, (int) (maxY * 0.8f));
        } else {
            path.curveTo(
                    x + (int) (maxX * 0.4f), (int) (maxY * 0.5f),
                    x + (int) (maxX * 0.6f), (int) (maxY * 0.6f),
                    x + maxX, (int) (maxY * 0.8f));
        }


        // Set up the animation
        ObjectAnimator anim = ObjectAnimator.ofObject(this, "AnimatedLocation",
                new PathEvaluator(), path.getPoints().toArray());


        set = new AnimatorSet();
        set.play(anim);
        set.setDuration(2000);

        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                doAnimation((int) view.getTranslationX(), (int) view.getTranslationY());
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


        set.start();
    }

    @Override
    public void setPosition(int left, int top, int width, int height) {
        super.setPosition(left, top, width, height);

        if (width > 0 && height > 0 && (this.width != width || this.height != height)) {
            this.width = width;
            this.height = height;

            /*
            if (set != null) {
                cancelAnimationSet();
                doAnimation((int)view.getTranslationX(), (int)view.getTranslationY());
            }
            */
        }
    }

    public void setAnimatedLocation(PathPoint newLoc) {
        view.setTranslationX(newLoc.mX);
        view.setTranslationY(newLoc.mY);

        int min = -10;
        int max = 15;
        float rotation = view.getRotation();

        if (rotateIncrease) {
            rotation += 0.1;
        } else {
            rotation -= 0.1;
        }

        if (rotation >= max || rotation <= min) {
            rotateIncrease = !rotateIncrease;
        }

        view.setRotation(rotation);

        if (newLoc.mX < 0 || (newLoc.mX + view.getWidth()) > getWidth()) {
            view.setScaleX(-1 * view.getScaleX());
            doAnimation((int) Math.min(getWidth() - view.getWidth() - 1, Math.max(1, (int) view.getTranslationX())), (int) view.getTranslationY());
        }
    }
}
