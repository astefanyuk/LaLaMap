package com.mariko.lalamap;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.android.youtube.player.YouTubeStandalonePlayer;

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

        doAnimation(0, 200, airplane);

        MapItem mapItem;

        addData(new LatLng(48.8582, 2.2945), null, getResources().getDrawable(R.drawable.tower), "ZV9u5bPRSfo");
        addData(new LatLng(-1.082650, 23.049303), null, getResources().getDrawable(R.drawable.tiger), "wDkZEUGRzMQ");

        mapItem = addData(new LatLng(40.439974, -20.402344), new LatLng(-37.597042, 53.0), getResources().getDrawable(R.drawable.afrika), "wDkZEUGRzMQ");
    }

    public void showMapItems(boolean show){
        mapItemsView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    private MapItem addData(LatLng latLngLeftTop, LatLng latLngRightBottom, Drawable drawable, String key) {

        final MapItem mapItem = new MapItem();
        mapItem.image = drawable;
        mapItem.pointLeftTop = latLngLeftTop;
        mapItem.pointRightBottom = latLngRightBottom;
        mapItem.key = key;

        View imageView = new View(getContext(), null);
        imageView.setBackgroundDrawable(mapItem.image);
        mapItem.view = imageView;

        imageView.setVisibility(View.GONE);

        items.add(mapItem);

        mapItemsView.addView(imageView);

        int width  = 100;

        imageView.getLayoutParams().width = width;
        imageView.getLayoutParams().height = (int) (drawable.getIntrinsicHeight() * width * 1.0f / drawable.getIntrinsicWidth());


        doAnimation(0, 50, imageView);

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

    public void onCameraMove(int x, int y){

        rootAnimationSet.cancel();
        setTranslationX(0);

        rootAnimationSet = new AnimatorSet();

        ObjectAnimator translationY = ObjectAnimator.ofFloat(this, "translationX", getTranslationX(), getTranslationX() + x);
        rootAnimationSet.play(translationY);
        rootAnimationSet.setDuration(1000);
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

            item.view.setVisibility(View.INVISIBLE);

            if(point == null && pointRightBottom == null){
                continue;
            }

            RectF rectPoint = new RectF();
            if(point != null && pointRightBottom != null){
                rectPoint.left = point.x;
                rectPoint.top = point.y;

                rectPoint.right = pointRightBottom.x;
                rectPoint.bottom = pointRightBottom.y;
            }else{
                Point p = point != null ? point : pointRightBottom;

                rectPoint.left = p.x;
                rectPoint.top = p.y;

                rectPoint.right = p.x;
                rectPoint.bottom = p.y;

            }

            if(rect.intersects(rectPoint.left, rectPoint.top, rectPoint.right, rectPoint.bottom)){

                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) item.view.getLayoutParams();

                if(item.pointLeftTop != null && item.pointRightBottom != null){

                    layoutParams.leftMargin = (int)(rectPoint.left);
                    layoutParams.topMargin = (int)(rectPoint.top);
                    layoutParams.width  = (int)rectPoint.width();
                    layoutParams.height  = (int)rectPoint.height();
                }else{
                    layoutParams.leftMargin = (int)(rectPoint.left - item.view.getWidth());
                    layoutParams.topMargin = (int)(rectPoint.top - item.view.getHeight());
                }

                item.view.requestLayout();
                item.view.setVisibility(View.VISIBLE);
            }

        }
    }

    private void doAnimation(final int x, final int y, final View view) {
        final AnimatorSet set = new AnimatorSet();
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", y, x);
        ObjectAnimator translationY2 = ObjectAnimator.ofFloat(view, "translationY", x, y);
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

}
