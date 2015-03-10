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
    public Drawable image;
    public String key;
    public String youtubeKey;
    public View view;
    public LocationType locationType;

    public static enum LocationType{
        Point /*Like Marker*/,
        Rect /* Fill all Rect*/,
        Area /* Contains in Rect*/
    }
}
