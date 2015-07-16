package com.mariko.lalamap;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

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
    public LocationType locationType;
    public String icon;

    public int width;
    public int height;

    private transient float scaleX = 1.0f;
    public transient boolean supportsRotation = true;

    public List<MediaItem> images = new ArrayList<>();

    public static enum LocationType {
        Marker /*Like Google Map Marker*/,
        FillRect /* Fill all Rect*/,
        Area /* Contains in Rect*/
    }

    public MapItem() {

    }

    public void init(Bitmap bitmap) {
        this.drawable = bitmap;
        this.height = (int) (drawable.getHeight() * width * 1.0f / drawable.getWidth());
        this.drawable = Bitmap.createScaledBitmap(drawable, this.width, this.height, false);
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
