package com.mariko.lalamap;

import android.graphics.Point;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.mariko.animation.PathPoint;

/**
 * Created by AStefaniuk on 21.04.2015.
 */
public class MapMainItem extends MarkerItem {

    public MarkerItem destinationMarkerItem;

    @Override
    protected void onEnd() {
        stopAnimation();

        destinationMarkerItem.stopAnimation();
        destinationMarkerItem = null;
        GApp.sInstance.getBus().post(new MapsActivity.StopEvent(this.getPosition()));
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
    protected boolean canMovePoint(PathPoint newLoc) {
        LatLng target = getTargetPosition();
        return Math.abs(newLoc.mX - target.latitude) > 2f || Math.abs(newLoc.mY - target.longitude) > 2f;
    }
}
