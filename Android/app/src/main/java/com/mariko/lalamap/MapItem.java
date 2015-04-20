package com.mariko.lalamap;

import android.graphics.drawable.Drawable;
import android.util.TypedValue;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by AStefaniuk on 06.03.2015.
 */
public class MapItem {
    public LatLng pointLeftTop;
    public LatLng pointRightBottom;
    public transient Drawable drawable;
    public String key;
    public String youtubeKey;
    public transient MapItemView view;
    public LocationType locationType;
    public String icon;

    public int width;
    public int height;


    public static enum LocationType {
        Marker /*Like Google Map Marker*/,
        FillRect /* Fill all Rect*/,
        Area /* Contains in Rect*/
    }

    public MapItem() {

    }


    public void init() {
        int iconResourceId = GApp.sInstance.getResources().getIdentifier(icon, "drawable", GApp.sInstance.getPackageName());
        this.drawable = GApp.sInstance.getResources().getDrawable(iconResourceId);

        setDrawableWidth(width);
    }

    public void setDrawableWidth(int width) {
        this.width = width;
        this.height = (int) (drawable.getIntrinsicHeight() * width * 1.0f / drawable.getIntrinsicWidth());
    }
}
