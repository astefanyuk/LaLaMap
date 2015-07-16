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
import com.mariko.data.MapController;
import com.mariko.data.MapItem;

import java.util.Random;

/**
 * Created by AStefaniuk on 22.04.2015.
 */
public class MarkerItem {

    protected static Random random = new Random();

    protected Marker marker;
    private MapController mapController;
    public MapItem item;
    protected GoogleMap map;

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

    protected LatLng getSourcePosition() {
        return item.getScaleX() > 0 ? this.item.pointLeftTop : this.item.pointRightBottom;
    }

    protected LatLng getTargetPosition() {
        return item.getScaleX() > 0 ? this.item.pointRightBottom : this.item.pointLeftTop;
    }

    protected double getNextDistance(boolean latitude) {
        if (latitude) {
            return random.nextDouble() * (Math.abs(getSourcePosition().latitude - getTargetPosition().latitude) / 4.0f);
        } else {
            return random.nextDouble() * Math.abs(getSourcePosition().longitude - getTargetPosition().longitude) / 3.0f;
        }
    }

    public void startAnimation() {

        stopAnimation();

        LatLng sourcePosition = getSourcePosition();
        LatLng targetPosition = getTargetPosition();

        if (sourcePosition == null || targetPosition == null) {
            return;
        }

        LatLng position = marker.getPosition();

        double endLatitude = position.latitude + (sourcePosition.latitude <= targetPosition.latitude ? 1 : -1) * getNextDistance(true);

        double endLongitude = position.longitude + (sourcePosition.longitude <= targetPosition.longitude ? 1 : -1) * getNextDistance(false);

        AnimatorPath path = new AnimatorPath();
        path.moveTo(position.latitude, position.longitude);

        path.curveTo(
                position.latitude - Math.abs(position.latitude - endLatitude) * 0.3f, position.longitude + (sourcePosition.longitude <= targetPosition.longitude ? 1 : -1) * Math.abs(position.longitude - endLongitude) * 0.3f,
                position.latitude - Math.abs(position.latitude - endLatitude) * 0.6f, position.longitude + (sourcePosition.longitude <= targetPosition.longitude ? 1 : -1) * Math.abs(position.longitude - endLongitude) * 0.6f,
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

    protected void onEnd(){
        stopAnimation();
        item.rotate();
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(item.getBitmap()));
        startAnimation();
    }

    protected boolean canMovePoint(PathPoint newLoc) {
        return newLoc.mY >= item.pointLeftTop.longitude &&
                newLoc.mY <= item.pointRightBottom.longitude;
    }

    public void setAnimatedLocation(PathPoint newLoc) {

        if (canMovePoint(newLoc)) {

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
           onEnd();
        }

        updateVisibility();
    }

    private void updateVisibility() {


        if(!item.supportsRotation){
            return;
        }

        LatLng position = getPosition();

        double v = Math.abs(item.pointLeftTop.longitude - item.pointRightBottom.longitude) * 0.15f;

        if (position.longitude < (item.pointLeftTop.longitude + v)) {
            marker.setAlpha(1 - (float) (Math.abs(item.pointLeftTop.longitude + v - position.longitude) * 1.0f / v));
        } else if (position.longitude > (item.pointRightBottom.longitude - v)) {
            marker.setAlpha(1 - (float) (Math.abs(item.pointRightBottom.longitude - v - position.longitude) * 1.0f / v));
        } else {
            marker.setAlpha(1);
        }
    }

    public LatLng getPosition(){
        return marker.getPosition();
    }

    public Marker getMarker() {
        return marker;
    }
}
