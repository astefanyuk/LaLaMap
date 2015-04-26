package com.mariko.lalamap;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;


public class MapRootView extends RelativeLayout {

    private MapMainItem airplane;

    private ViewGroup mapItemsView;

    private GoogleMap map;

    private AnimatorSet rootAnimationSet = new AnimatorSet();

    private MarkerItemList items = new MarkerItemList();

    public MapRootView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.map_root, this);

        mapItemsView = (ViewGroup) findViewById(R.id.mapItemsView);

        showMapItems(false);


    }


    public void plainTo(MapData mapData, MapItem item) {

        airplane.stopAnimation();
        airplane.destinationMarkerItem = items.findByMapItem(item);

        airplane.startAnimation();

    }

    public void mapReady(MapData mapData) {

        initMainItem(mapData);

        for(MapItem item : GApp.sInstance.getMapController().items){

            MarkerItem markerItem = new MarkerItem();

            markerItem.setItem(GApp.sInstance.getMapController(), item, mapData.map);

            items.add(markerItem);
        }

        start();
    }

    private void initMainItem(MapData mapData) {
        MapItem item = new MapItem();
        item.icon = "airplane";
        item.locationType = MapItem.LocationType.Area;
        item.width = 200;

        item.supportsRotation = false;

        item.pointLeftTop = new LatLng(0,0);

        item.init();

        airplane = new MapMainItem();
        airplane.setItem(GApp.sInstance.getMapController(), item, mapData.map);
    }

    public void start() {
       // airplane.setVisibility(View.VISIBLE);
        showMapItems(true);
    }

    public void showMapItems(boolean show) {
        mapItemsView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }


    public void scrollCamera(int x, int y, long duration) {

        rootAnimationSet.cancel();

        if (duration > 0) {

            ObjectAnimator moveX = ObjectAnimator.ofFloat(this, "translationX", getTranslationX(), getTranslationX() + x);
            ObjectAnimator moveY = ObjectAnimator.ofFloat(this, "translationY", getTranslationY(), getTranslationY() + y);

            moveX.setInterpolator(new DecelerateInterpolator());
            moveY.setInterpolator(new DecelerateInterpolator());

            rootAnimationSet = new AnimatorSet();
            rootAnimationSet.setDuration(duration);
            rootAnimationSet.playTogether(moveX, moveY);
            rootAnimationSet.start();
        } else {
            setTranslationX(getTranslationX() + x);
            setTranslationY(getTranslationY() + y);
        }
    }

    public void onCameraChange(MapData mapData) {

        map = mapData.map;

        showMapItems(true);

        rootAnimationSet.cancel();
        setTranslationX(0);
        setTranslationY(0);

        //VisibleRegion visibleRegion = projection.getVisibleRegion();

        RectF rect = new RectF(0, 0, getWidth(), getHeight());
        /*

        for (MapItem item : GApp.sInstance.getMapController().items) {

            Point point = item.pointLeftTop == null ? null : mapData.projection.toScreenLocation(item.pointLeftTop);
            Point pointRightBottom = item.pointRightBottom == null ? null : mapData.projection.toScreenLocation(item.pointRightBottom);

            if (point == null && pointRightBottom == null) {
                item.view.show(false);
                continue;
            }

            RectF rectPoint = new RectF();
            if (point != null && pointRightBottom != null) {
                rectPoint.left = point.x;
                rectPoint.top = point.y;

                rectPoint.right = pointRightBottom.x;
                rectPoint.bottom = pointRightBottom.y;
            } else {

                Point p = point != null ? point : pointRightBottom;

                rectPoint.left = p.x;
                rectPoint.top = p.y;

                rectPoint.right = p.x;
                rectPoint.bottom = p.y;
            }

            if (rect.intersects(rectPoint.left, rectPoint.top, rectPoint.right, rectPoint.bottom)) {

                if (item.locationType.equals(MapItem.LocationType.Marker) || item.locationType.equals(MapItem.LocationType.FillRect)) {

                    if (item.pointLeftTop != null && item.pointRightBottom != null) {

                        item.view.setPosition(mapData, (int) (rectPoint.left), (int) (rectPoint.top), (int) rectPoint.width(), (int) rectPoint.height());
                    } else {
                        item.view.setPosition(mapData, (int) (rectPoint.left - item.width), (int) (rectPoint.top - item.height));
                    }

                } else if (item.locationType.equals(MapItem.LocationType.Area)) {
                    item.view.setPosition(mapData, (int) (rectPoint.left), (int) (rectPoint.top), (int) rectPoint.width(), (int) rectPoint.height());
                }

                item.view.show(true);
            } else {
                item.view.show(false);
            }

        }
        */
    }

    /*
    private void doAnimationVertical(final int y1, final int y2, final View view) {
        final AnimatorSet set = new AnimatorSet();
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", y2, y1);
        ObjectAnimator translationY2 = ObjectAnimator.ofFloat(view, "translationY", y1, y2);
        set.play(translationY2).after(translationY);
        set.setDuration(5000);
        set.start();

        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                set.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void doAnimationHorizontal(final int x1, final int x2, final View view, long duration) {
        final AnimatorSet set = new AnimatorSet();
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationX", x2, x1);
        ObjectAnimator translationY2 = ObjectAnimator.ofFloat(view, "translationX", x1, x2);
        set.play(translationY2).after(translationY);
        set.setDuration(duration);
        set.start();

        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                set.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
    */

}
