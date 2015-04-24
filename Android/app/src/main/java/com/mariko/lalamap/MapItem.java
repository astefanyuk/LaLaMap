package com.mariko.lalamap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RotateDrawable;
import android.util.TypedValue;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by AStefaniuk on 06.03.2015.
 */
public class MapItem {
    public LatLng pointLeftTop;
    public LatLng pointRightBottom;
    private transient Bitmap drawableRotated;
    private transient Bitmap drawable;
    public String key;
    public String youtubeKey;
    public transient MapItemView view;
    public LocationType locationType;
    public String icon;

    public int width;
    public int height;

    private transient float scaleX = 1.0f;


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

    public float getScaleX() {
        return scaleX;
    }

    public Bitmap getBitmap() {

        if (scaleX > 0) {
            return drawable;
        } else {
            if (drawableRotated == null) {
                Matrix matrix = new Matrix();
                matrix.preScale(-1, 1);
                drawableRotated = Bitmap.createBitmap(drawable, 0, 0, drawable.getWidth(), drawable.getHeight(), matrix, true);
            }
            return drawableRotated;
        }
    }

    public void rotate() {
        scaleX *= -1;
    }

}
