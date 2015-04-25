package com.mariko.lalamap;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mariko.animation.AnimatorPath;
import com.mariko.animation.PathEvaluator;
import com.mariko.animation.PathPoint;

import java.util.Random;

/**
 * Created by AStefaniuk on 22.04.2015.
 */
public class MarkerItem {

    protected static Random random = new Random();

    private Marker marker;
    private MapController mapController;
    public MapItem item;
    private GoogleMap map;

    protected AnimatorSet set;
    private boolean rotateIncrease;

    public void setItem(MapController mapController, MapItem item, GoogleMap map) {
        this.item = item;
        this.mapController = mapController;
        this.map = map;

        if (marker != null) {
            marker.remove();
        }

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(item.getBitmap()));
        markerOptions.position(item.pointLeftTop);
        marker = map.addMarker(markerOptions);

        startAnimation();
    }

    protected void stopAnimation() {
        if (set != null) {
            set.removeAllListeners();
            set.cancel();
            set = null;
        }
    }

    public void startAnimation() {

        stopAnimation();

        if(item.pointLeftTop == null || item.pointRightBottom == null){
            return;
        }

        LatLng position = marker.getPosition();

        double latitudeDistance = random.nextDouble() * (Math.abs(item.pointLeftTop.latitude - item.pointRightBottom.latitude) / 4.0f);

        double endLatitude = position.latitude - latitudeDistance;

        if (endLatitude < item.pointRightBottom.latitude) {
            endLatitude = position.latitude + latitudeDistance;
        }

        double distanceLongitude = Math.abs(item.pointLeftTop.longitude - item.pointRightBottom.longitude);
        double endLongitude = position.longitude + (item.getScaleX() > 0 ? 1 : -1) * random.nextDouble() * (distanceLongitude / 3.0f);

        AnimatorPath path = new AnimatorPath();
        path.moveTo(position.latitude, position.longitude);

        path.curveTo(
                position.latitude - Math.abs(position.latitude - endLatitude) * 0.3f, position.longitude + (item.getScaleX() > 0 ? 1 : -1) * Math.abs(position.longitude - endLongitude) * 0.3f,
                position.latitude - Math.abs(position.latitude - endLatitude) * 0.6f, position.longitude + (item.getScaleX() > 0 ? 1 : -1) * Math.abs(position.longitude - endLongitude) * 0.6f,
                endLatitude, endLongitude);

        // Set up the animation
        ObjectAnimator anim = ObjectAnimator.ofObject(this, "AnimatedLocation",
                new PathEvaluator(), path.getPoints().toArray());


        set = new AnimatorSet();
        set.play(anim);
        set.setDuration(2000);

        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                startAnimation();
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

    public void setAnimatedLocation(PathPoint newLoc) {

        if (newLoc.mY >= item.pointLeftTop.longitude &&
                newLoc.mY <= item.pointRightBottom.longitude) {

            marker.setPosition(new LatLng(newLoc.mX, newLoc.mY));

            int min = -10;
            int max = 15;
            float rotation = marker.getRotation();

            if (rotateIncrease) {
                rotation += 0.1;
            } else {
                rotation -= 0.1;
            }

            if (rotation >= max || rotation <= min) {
                rotateIncrease = !rotateIncrease;
            }

            marker.setRotation(rotation);

        } else {
            stopAnimation();
            item.rotate();
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(item.getBitmap()));
            startAnimation();
        }

        LatLng position = marker.getPosition();

        double v = Math.abs(item.pointLeftTop.longitude - item.pointRightBottom.longitude) * 0.15f;

        if (position.longitude < (item.pointLeftTop.longitude + v)) {
            marker.setAlpha(1 - (float) (Math.abs(item.pointLeftTop.longitude + v - position.longitude) * 1.0f / v));
        } else if (position.longitude > (item.pointRightBottom.longitude - v)) {
            marker.setAlpha(1 - (float) (Math.abs(item.pointRightBottom.longitude - v - position.longitude) * 1.0f / v));
        } else {
            marker.setAlpha(1);
        }
    }
}
