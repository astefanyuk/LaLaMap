package com.mariko.lalamap;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.VisibleRegion;

import java.util.ArrayList;
import java.util.List;


public class MapRootView extends RelativeLayout {

    ImageView airplane;

    private ViewGroup mapItemsView;

    private List<MapItem> items = new ArrayList<MapItem>();

    private AnimatorSet rootAnimationSet = new AnimatorSet();

    public MapRootView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.map_root, this);

        mapItemsView = (ViewGroup) findViewById(R.id.mapItemsView);

        airplane = (ImageView) findViewById(R.id.airplane);
        airplane.setVisibility(View.INVISIBLE);

        doAnimationVertical(0, 200, airplane);

        MapItem mapItem;


        addData(new MapItem(new LatLng(48.8582, 2.2945), null, getResources().getDrawable(R.drawable.tower), MapItem.LocationType.Marker, 100), "ZV9u5bPRSfo");
        addData(new MapItem(new LatLng(-1.082650, 23.049303), null, getResources().getDrawable(R.drawable.tiger), MapItem.LocationType.Marker, 100), "wDkZEUGRzMQ");

        mapItem = addData(new MapItem(new LatLng(40.439974, -20.402344), new LatLng(-37.597042, 53.0), getResources().getDrawable(R.drawable.afrika), MapItem.LocationType.FillRect, 100), "wDkZEUGRzMQ");

        mapItem = addData(new MapItem(new LatLng(-38.209739, 31.206592), new LatLng(-52.003176, 101.519094), getResources().getDrawable(R.drawable.kit), MapItem.LocationType.Area, 100), "wDkZEUGRzMQ");

        //mapItem = addData(new MapItem(new LatLng(-73.610217, -7.992628), new LatLng(-83.860957, 151.440969), getResources().getDrawable(R.drawable.pingvin), MapItem.LocationType.Area, 100), "wDkZEUGRzMQ");

        showMapItems(false);

    }


    public void start() {
        airplane.setVisibility(View.VISIBLE);
        showMapItems(true);
    }

    public void showMapItems(boolean show) {
        mapItemsView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    private MapItem addData(MapItem mapItem, String youtubeKey) {

        mapItem.youtubeKey = youtubeKey;

        MapItemView view;
        if (mapItem.locationType.equals(MapItem.LocationType.Area)) {
            view = new MapItemViewArea(getContext(), null);
        } else {
            view = new MapItemView(getContext(), null);
        }
        view.setVisibility(View.GONE);
        mapItemsView.addView(view);
        view.setMapItem(mapItem);

        items.add(mapItem);

        /*
        if(mapItem.locationType.equals(MapItem.LocationType.Area)){
            doAnimationHorizontal(0, 300, mapItem.view, 10000);
        }else{
            doAnimationVertical(0, 50, mapItem.view);
        }
        */


        /*
        imageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) getContext(), getContext().getString(R.string.google_maps_key), mapItem.key, 0, true, true);
                getContext().startActivity(intent);

            }
        });
        */

        return mapItem;
    }

    public void onCameraMove(int x, int y) {

        rootAnimationSet.cancel();
        setTranslationX(0);

        rootAnimationSet = new AnimatorSet();

        ObjectAnimator translationY = ObjectAnimator.ofFloat(this, "translationX", getTranslationX(), getTranslationX() + x);
        rootAnimationSet.play(translationY);
        rootAnimationSet.setDuration(2000);
        rootAnimationSet.start();
    }

    public void onCameraChange(GoogleMap map, CameraPosition position, Projection projection) {

        showMapItems(true);


        rootAnimationSet.cancel();
        setTranslationX(0);

        VisibleRegion visibleRegion = projection.getVisibleRegion();

        RectF rect = new RectF(0, 0, getWidth(), getHeight());

        for (MapItem item : items) {

            Point point = item.pointLeftTop == null ? null : projection.toScreenLocation(item.pointLeftTop);
            Point pointRightBottom = item.pointRightBottom == null ? null : projection.toScreenLocation(item.pointRightBottom);

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

                        item.view.setPosition((int) (rectPoint.left), (int) (rectPoint.top), (int) rectPoint.width(), (int) rectPoint.height());
                    } else {
                        item.view.setPosition((int) (rectPoint.left - item.width), (int) (rectPoint.top - item.height));
                    }

                } else if (item.locationType.equals(MapItem.LocationType.Area)) {
                    item.view.setPosition((int) (rectPoint.left), (int) (rectPoint.top), (int) rectPoint.width(), (int) rectPoint.height());
                }

                item.view.show(true);
            } else {
                item.view.show(false);
            }

        }
    }

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

}
