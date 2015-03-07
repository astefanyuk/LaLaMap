package com.mariko.lalamap;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
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
    ImageView kit;

    private List<MapItem> items = new ArrayList<MapItem>();

    public MapRootView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.map_root, this);

        airplane = (ImageView) findViewById(R.id.airplane);
        kit = (ImageView) findViewById(R.id.kit);

        doAnimation(0, 200, airplane);

        doAnimation(10, 50, kit);

        addData(new LatLng(48.8582, 2.2945), getResources().getDrawable(R.drawable.tower), "ZV9u5bPRSfo");
        addData(new LatLng(-1.082650, 23.049303), getResources().getDrawable(R.drawable.tiger), "wDkZEUGRzMQ");
    }

    private void addData(LatLng latLng, Drawable drawable, String key) {

        final MapItem mapItem = new MapItem();
        mapItem.image = drawable;
        mapItem.point = latLng;
        mapItem.key = key;

        ImageView imageView = new ImageView(getContext(), null);
        imageView.setImageDrawable(mapItem.image);
        mapItem.view = imageView;

        imageView.setVisibility(View.GONE);

        items.add(mapItem);

        addView(imageView);

        imageView.getLayoutParams().width = 100;
        imageView.getLayoutParams().height = (int) (imageView.getLayoutParams().width * 1.5);

        doAnimation(0, 50, imageView);

        imageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) getContext(), getContext().getString(R.string.google_maps_key), mapItem.key, 0, true, true);
                getContext().startActivity(intent);

            }
        });
    }

    public void onCameraChange(GoogleMap map, CameraPosition position, Projection projection) {
        VisibleRegion visibleRegion = projection.getVisibleRegion();
        for (MapItem item : items) {
            if (!visibleRegion.latLngBounds.contains(item.point)) {
                item.view.setVisibility(View.INVISIBLE);
            } else {

                Point point = projection.toScreenLocation(item.point);

                ((LayoutParams) item.view.getLayoutParams()).leftMargin = point.x - item.view.getWidth();
                ((LayoutParams) item.view.getLayoutParams()).topMargin = point.y - item.view.getHeight();
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
