package com.mariko.lalamap;

import android.animation.AnimatorSet;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by AStefaniuk on 09.04.2015.
 */
public class MapItemView extends FrameLayout {
    private MapItem item;
    protected View view;
    protected AnimatorSet set;

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
        }
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

    protected void doAnimation() {

    }

    public void show(boolean show) {
        setVisibility(show ? View.VISIBLE : View.INVISIBLE);

        if (!show) {
            if (set != null) {
                set.cancel();
            }
            set = null;
        } else {
            if (set == null) {
                doAnimation();
            }
        }
    }

    protected void cancelAnimationSet() {
        if (set != null) {
            set.removeAllListeners();
            set.cancel();
            set = null;
        }
    }
}
