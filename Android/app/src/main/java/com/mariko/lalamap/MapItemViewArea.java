package com.mariko.lalamap;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;

import com.mariko.animation.AnimatorPath;
import com.mariko.animation.PathEvaluator;
import com.mariko.animation.PathPoint;

/**
 * Created by AStefaniuk on 09.04.2015.
 */
public class MapItemViewArea extends MapItemView {

    private boolean rotateIncrease;
    private int width;
    private int height;

    private float scaleX;

    public MapItemViewArea(Context context, AttributeSet attrs) {
        super(context, attrs);

        //setBackgroundColor(Color.RED);
        scaleX = view.getScaleX();
    }

    @Override
    protected void doAnimation() {
        doAnimation(0, 0);
    }

    private float getViewHeight() {
        return Math.abs(view.getScaleY()) * item.height;
    }

    private float getViewWidth() {
        return Math.abs(scaleX) * item.width;
    }

    private void doAnimation(int x, int y) {

        cancelAnimationSet();

        if (width <= 0 || height <= 0) {
            return;
        }

        int maxX = random.nextInt(Math.min((int) (GApp.sInstance.DPI * 300), (int) (width / 3.0)));
        int yShift = Math.abs((int) (view.getPivotY() * scaleX));
        int maxY = (int) (random.nextFloat() * (height - getViewHeight() - yShift));

        int[] yPoints = new int[]{
                yShift + (int) (maxY * 0.5f),
                yShift + (int) (maxY * 0.6f),
                yShift + (int) (maxY * 0.8f)};


        // Set up the path we're animating along
        AnimatorPath path = new AnimatorPath();
        path.moveTo(x, y);

        if (scaleX < 0) {
            path.curveTo(
                    x, yPoints[0],
                    x - (int) (maxX * 0.6f), yPoints[1],
                    x - maxX, yPoints[2]);
        } else {
            path.curveTo(
                    x + (int) (maxX * 0.4f), yPoints[0],
                    x + (int) (maxX * 0.6f), yPoints[1],
                    x + maxX, yPoints[2]);
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
    public void setPosition(MapData mapData, int left, int top, int width, int height) {
        super.setPosition(mapData, left, top, width, height);

        if (width > 0 && height > 0 && (this.width != width || this.height != height)) {
            this.width = width;
            this.height = height;

            float zoom = mapData.position.zoom;
            int maxHeight = Math.min(Math.max(item.height, (int) (200 * GApp.sInstance.DPI)), (int) (height * 0.7f));
            zoom = Math.min((maxHeight * 1.0f / item.height), zoom);
            scaleX = zoom * (scaleX > 0 ? 1 : -1);
            view.setScaleX(scaleX);
            view.setScaleY(zoom);

            if (set != null) {
                cancelAnimationSet();
                doAnimation((int) view.getTranslationX(), (int) view.getTranslationY());
            }
        }
    }

    public void setAnimatedLocation(PathPoint newLoc) {

        if (width <= 0 || height <= 0) {
            return;
        }

        float nextX = newLoc.mX + (scaleX > 0 ? 1 : -1);

        if (nextX < 0 || (scaleX < 0 && nextX < (Math.abs(scaleX) * view.getPivotX())) || (nextX + getViewWidth() + 1) >= width) {
            cancelAnimationSet();

            //rotate image
            scaleX *= -1;
            ObjectAnimator animator = ObjectAnimator.ofFloat(view, "scaleX", scaleX);
            animator.setDuration(500);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    doAnimation((int) Math.min(getWidth() - getViewWidth() - 1, Math.max(1, (int) view.getTranslationX())), (int) view.getTranslationY());
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    view.setScaleX(scaleX);
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.start();
        } else {

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
        }
    }
}
