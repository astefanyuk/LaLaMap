package com.mariko.lalamap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by AStefaniuk on 06.03.2015.
 */
public class MapItem {
    public LatLng pointLeftTop;
    public LatLng pointRightBottom;
    public transient Bitmap drawable;
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
        this.drawable = BitmapFactory.decodeResource(GApp.sInstance.getResources(), iconResourceId);
        this.height = (int) (drawable.getHeight() * width * 1.0f / drawable.getWidth());
    }


}
