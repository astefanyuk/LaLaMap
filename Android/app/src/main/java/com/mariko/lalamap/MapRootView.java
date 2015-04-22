package com.mariko.lalamap;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mariko.animation.PathPoint;


public class MapRootView extends RelativeLayout {

    private MapMainItem airplane;

    private ViewGroup mapItemsView;

    private GoogleMap map;

    private AnimatorSet rootAnimationSet = new AnimatorSet();

    private Marker marker;

    public MapRootView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.map_root, this);

        mapItemsView = (ViewGroup) findViewById(R.id.mapItemsView);

        airplane = (MapMainItem) findViewById(R.id.airplane);

        /*
        for (MapItem mapItem : GApp.sInstance.getMapController().items) {

            MapItemView view;
            if (mapItem.locationType.equals(MapItem.LocationType.Area)) {
                view = new MapItemViewArea(getContext(), null){
                    @Override
                    public void setAnimatedLocation(PathPoint newLoc) {
                        super.setAnimatedLocation(newLoc);

                        if(map != null){

                            LatLng p = map.getProjection().fromScreenLocation(new Point((int)(newLoc.mX + 1200), (int)(newLoc.mY + 900)));

                            if(marker == null){
                                MarkerOptions markerOptions = new MarkerOptions();
                                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.kit);
                                markerOptions.icon(bitmapDescriptor);
                                markerOptions.position(p);
                                marker = map.addMarker(markerOptions);
                            }

                            marker.setPosition(p);
                            marker.setRotation(getRotation());

                        }

                    }
                };
            } else {
                view = new MapItemView(getContext(), null);
            }
            view.setVisibility(View.GONE);
            mapItemsView.addView(view);
            view.setMapItem(mapItem);
        }
        */

        showMapItems(false);


    }


    public void plainTo(MapData mapData, MapItem item) {

        Point point = mapData.map.getProjection().toScreenLocation(item.pointLeftTop);

        airplane.cancelAnimationSet();
        airplane.setPosition(mapData, 0, 0, point.x, point.y);
        airplane.show(true);

        /*
       // showMapItems(false);

        Point pointStart = new Point((int)airplane.getTranslationX(), (int)airplane.getTranslationY());


        //mapData.map.animateCamera(CameraUpdateFactory.newLatLngZoom(item.pointLeftTop, 10));
        */
    }

    public void start() {
        airplane.setVisibility(View.VISIBLE);
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
