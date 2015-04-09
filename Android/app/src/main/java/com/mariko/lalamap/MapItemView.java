package com.mariko.lalamap;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.mariko.animation.AnimatorPath;
import com.mariko.animation.PathEvaluator;
import com.mariko.animation.PathPoint;

/**
 * Created by AStefaniuk on 09.04.2015.
 */
public class MapItemView extends FrameLayout {
    private MapItem item;
    private View view;
    private boolean rotateIncrease;

    public MapItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = new View(context, attrs);
        addView(view);

        //setBackgroundColor(Color.RED);
    }

    public MapItem getMapItem() {
        return item;
    }

    public void setMapItem(MapItem item) {
        this.item = item;
        if (this.item != null) {

            if (item.locationType.equals(MapItem.LocationType.FillRect)) {
                this.view.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                this.view.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;

            } else {
                this.view.getLayoutParams().width = this.item.width;
                this.view.getLayoutParams().height = this.item.height;
            }

            this.view.setBackgroundDrawable(this.item.drawable);
            this.item.view = this;

            if (item.locationType.equals(MapItem.LocationType.Area)) {
                areaAnimation(0, 0);
            }
        }
    }

    private void areaAnimation(final int x, final int y) {
        // Set up the path we're animating along
        AnimatorPath path = new AnimatorPath();
        path.moveTo(x, y);
        path.curveTo(x + 100, 100, x + 200, 200, x + 300, 100);
        //path.lineTo(300,100);
        //path.curveTo(300, 90, 600, 100, 700, 200);

        //path.curveTo(100, 0, 300, 900, 400, 500);
        //path.curveTo(500, 400, 400, 400, 300, 100);

        // Set up the animation
        final ObjectAnimator anim = ObjectAnimator.ofObject(this, "BezierLocation",
                new PathEvaluator(), path.getPoints().toArray());


        final AnimatorSet set = new AnimatorSet();
        set.play(anim);
        set.setDuration(10000);

        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                areaAnimation(x + 300, 100);
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

    public void setBezierLocation(PathPoint newLoc) {
        view.setTranslationX(newLoc.mX);
        view.setTranslationY(newLoc.mY);

        int min = -15;
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

    public void setPosition(int left, int top, int width, int height) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) item.view.getLayoutParams();

        layoutParams.leftMargin = left;
        layoutParams.topMargin = top;
        layoutParams.width = width;
        layoutParams.height = height;

        requestLayout();
    }

    public void setPosition(int left, int top) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) item.view.getLayoutParams();

        layoutParams.leftMargin = left;
        layoutParams.topMargin = top;

        requestLayout();
    }
}
