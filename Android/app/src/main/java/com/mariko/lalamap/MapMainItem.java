package com.mariko.lalamap;

import android.animation.ObjectAnimator;
import android.graphics.Point;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.mariko.animation.AnimatorPath;
import com.mariko.animation.PathEvaluator;
import com.mariko.animation.PathPoint;

/**
 * Created by AStefaniuk on 21.04.2015.
 */
public class MapMainItem extends MarkerItem {

    private MarkerItem destinationMarkerItem;

    @Override
    protected void onEnd() {
        stopAnimation();

        destinationMarkerItem.stopAnimation();
        LatLng target = destinationMarkerItem.getPosition();

        GApp.sInstance.getBus().post(new MapsActivity.StopEvent(this.getPosition()));

        AnimatorPath path = new AnimatorPath();
        path.moveTo(getMarker().getPosition().latitude, getMarker().getPosition().longitude);
        path.lineTo(target.latitude, target.longitude - 5);

        ObjectAnimator anim = ObjectAnimator.ofObject(this, "AnimatedLocationLiner",
                new PathEvaluator(), path.getPoints().toArray());

        anim.setDuration(1000);
        anim.start();
    }

    public void setAnimatedLocationLiner(PathPoint newLoc) {
        marker.setPosition(new LatLng(newLoc.mX, newLoc.mY));
    }

    @Override
    protected LatLng getSourcePosition() {
        return getPosition();
    }

    @Override
    protected LatLng getTargetPosition() {
        if (destinationMarkerItem != null) {
            return destinationMarkerItem.getPosition();
        }
        return null;
    }

    @Override
    public void startAnimation() {

        stopAnimation();

        item.pointRightBottom = null;

        if(destinationMarkerItem != null){
            item.pointRightBottom = destinationMarkerItem.getPosition();
        }

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(getMarker().getPosition(), map.getMinZoomLevel()));


        super.startAnimation();
    }

    @Override
    protected double getNextDistance(boolean latitude) {
        return random.nextDouble() * 30;
    }

    @Override
    protected boolean canMovePoint(PathPoint newLoc) {
        LatLng target = getTargetPosition();
        return Math.abs(newLoc.mX - target.latitude) > 10f || Math.abs(newLoc.mY - target.longitude) > 10f;
    }

    public MarkerItem getDestinationMarkerItem() {
        return destinationMarkerItem;
    }

    public void setDestinationMarkerItem(MarkerItem value) {

        if (this.destinationMarkerItem != null && this.destinationMarkerItem != value) {
            this.destinationMarkerItem.startAnimation();
        }

        this.destinationMarkerItem = value;

        if (this.destinationMarkerItem != null) {
            this.destinationMarkerItem.startAnimation();
        }
    }
}
