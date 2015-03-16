package com.mariko.lalamap;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by AStefaniuk on 06.03.2015.
 */
public class MapItem {
    public LatLng pointLeftTop;
    public LatLng pointRightBottom;
    public Drawable drawable;
    public String key;
    public String youtubeKey;
    public View view;
    public LocationType locationType;

    public int width;
    public int height;

    public static enum LocationType{
        Marker /*Like Google Map Marker*/,
        FillRect /* Fill all Rect*/,
        Area /* Contains in Rect*/
    }

    public MapItem(){

    }

    public MapItem(LatLng pointLeftTop, LatLng pointRightBottom, Drawable drawable, MapItem.LocationType locationType, int width){
        this.pointLeftTop = pointLeftTop;
        this.pointRightBottom = pointRightBottom;
        this.drawable = drawable;
        this.locationType = locationType;

        setDrawableWidth(width);
    }

    public void setDrawableWidth(int width){
        this.width = width;
        this.height =  (int) (drawable.getIntrinsicHeight() * width * 1.0f / drawable.getIntrinsicWidth());
    }
}
