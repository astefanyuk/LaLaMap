package com.mariko.lalamap;

/**
 * Created by AStefaniuk on 21.04.2015.
 */
public class MapMainItem extends MarkerItem {

    public MarkerItem destinationMarkerItem;

    @Override
    protected void onEnd() {
        stopAnimation();

        //destinationMarkerItem.stopAnimation();
        destinationMarkerItem = null;
        GApp.sInstance.getBus().post(new MapsActivity.StopEvent(this.getPosition()));
    }

    @Override
    public void startAnimation() {

        stopAnimation();

        item.pointRightBottom = null;

        if(destinationMarkerItem != null){
            item.pointRightBottom = destinationMarkerItem.getPosition();
        }

        super.startAnimation();
    }
}
