package com.mariko.lalamap;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by AStefaniuk on 09.04.2015.
 */
public class MapItemView extends FrameLayout {
    private MapItem item;
    private View view;

    public MapItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = new View(context, attrs);
        addView(view);
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

    public MapItem getMapItem() {
        return item;
    }
}
