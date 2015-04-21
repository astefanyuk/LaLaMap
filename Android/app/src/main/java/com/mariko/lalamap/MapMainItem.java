package com.mariko.lalamap;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by AStefaniuk on 21.04.2015.
 */
public class MapMainItem extends MapItemViewArea {
    public MapMainItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        MapItem mapItem = new MapItem();
        mapItem.icon = "airplane";
        mapItem.locationType = MapItem.LocationType.Area;
        mapItem.width = 200;

        mapItem.init();

        setMapItem(mapItem);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);


    }
}
