package com.mariko.lalamap;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.RectF;
import android.util.Log;
import android.view.animation.AnimationSet;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
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
    private MapItem item;
    private GoogleMap map;

    private float scaleX = 1.0f;
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
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(item.drawable));
        markerOptions.position(item.pointLeftTop);
        marker = map.addMarker(markerOptions);


        markerOptions = new MarkerOptions();
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(item.drawable));
        markerOptions.position(item.pointLeftTop);
        map.addMarker(markerOptions);

        markerOptions = new MarkerOptions();
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(item.drawable));
        markerOptions.position(item.pointRightBottom);
        map.addMarker(markerOptions);

        doAnimation();

    }

    protected void cancelAnimationSet() {
        if (set != null) {
            set.removeAllListeners();
            set.cancel();
            set = null;
        }
    }

    private void doAnimation() {

        cancelAnimationSet();

        LatLng position = marker.getPosition();

        double distanceLatitude = Math.abs(item.pointLeftTop.latitude - item.pointRightBottom.latitude);
        double distanceLongitude = Math.abs(item.pointLeftTop.longitude - item.pointRightBottom.longitude);

        double maxLongitude = random.nextDouble() * (distanceLongitude / 3.0f);

        double latitudeShift = Math.abs(item.pointLeftTop.latitude - position.latitude);

        double maxLatitude = random.nextFloat() * (distanceLatitude - latitudeShift);

        double longitude = position.longitude;
        double latitude = position.latitude;

        double[] latitudePoints = new double[]{
                (position.latitude - latitudeShift - (maxLatitude * 0.5f)),
                (position.latitude - latitudeShift - (maxLatitude * 0.6f)),
                (position.latitude - latitudeShift - (maxLatitude * 0.8f))};


        // Set up the path we're animating along
        AnimatorPath path = new AnimatorPath();
        path.moveTo(latitude, longitude);

        if (scaleX < 0) {
            path.curveTo(
                    latitudePoints[0], longitude,
                    latitudePoints[1], longitude - (int) (maxLongitude * 0.6f),
                    latitudePoints[2], longitude - maxLongitude);
        } else {
            path.curveTo(
                    latitudePoints[0], longitude + (int) (maxLongitude * 0.4f),
                    latitudePoints[1], longitude + (int) (maxLongitude * 0.6f),
                    latitudePoints[2], longitude + maxLongitude);
        }


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
                doAnimation();
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


        double nextX = newLoc.mX + (scaleX > 0 ? 1 : -1);

        if (false) {
//                (nextX < 0 || (scaleX < 0 && nextX < (Math.abs(scaleX) * view.getPivotX())) || (nextX + getViewWidth() + 1) >= width) {
            cancelAnimationSet();

            /*

            //rotate image
            scaleX *= -1;
            ObjectAnimator animator = ObjectAnimator.ofFloat(view, "scaleX", scaleX);
            animator.setDuration(500);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    doAnimation((int) Math.min(getWidth() - getViewWidth() - 1, Math.max(1, (int) view.getTranslationX())), (int) view.getTranslationY());
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    view.setScaleX(scaleX);
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.start();
            */
        } else {

            //if(newLoc.mX >= item.pointRightBottom.latitude && newLoc.mX <= item.pointLeftTop.latitude)
            {
                marker.setPosition(new LatLng(newLoc.mX, newLoc.mY));
            }


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
        }
    }
}
