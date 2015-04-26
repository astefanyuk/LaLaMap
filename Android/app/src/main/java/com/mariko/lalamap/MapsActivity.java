package com.mariko.lalamap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.mariko.map.MapFragmentEx;
import com.mariko.map.MapStateListener;
import com.squareup.otto.Subscribe;

import java.util.Timer;

public class MapsActivity extends Activity {

    public static class StopEvent {
        public final LatLng point;

        public StopEvent(LatLng point){
            this.point = point;
        }
    }

    public static class DestinationSelectedEvent {
        public final MapItem item;

        public DestinationSelectedEvent(MapItem item) {
            this.item = item;
        }

    }

    private MapRootView mapRootView;
    private DestinationList destinationList;

    private Timer timer;

    private final MapData mapData = new MapData();
    private boolean destinationVisible;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mapRootView = (MapRootView) findViewById(R.id.mapRootView);
        setupMap();

        destinationList = (DestinationList) findViewById(R.id.destinationList);

        findViewById(R.id.showList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDestinationVisibility();

            }
        });

        /*
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mMap.animateCamera(CameraUpdateFactory.scrollBy(100, 0));
                        mapRootView.onCameraMove(-100, 0);

                    }
                });
            }
        }, 0, 2000);
        */

    }

    @Override
    public void onBackPressed() {
        if (destinationVisible) {
            changeDestinationVisibility();
            return;
        }
        super.onBackPressed();
    }

    private void changeDestinationVisibility() {
        destinationList.setVisibility(View.VISIBLE);
        if (destinationVisible) {
            YoYo.with(Techniques.SlideOutDown).duration(300).playOn(destinationList);
        } else {
            YoYo.with(Techniques.SlideInUp).duration(300).playOn(destinationList);
        }
        destinationVisible = !destinationVisible;
    }

    @Override
    protected void onResume() {
        super.onResume();
        GApp.sInstance.getBus().register(this);
        setupMap();
    }

    @Override
    protected void onPause() {
        GApp.sInstance.getBus().unregister(this);
        super.onPause();
    }

    @Subscribe
    public void destinationSelected(DestinationSelectedEvent event) {
        if (destinationVisible) {
            changeDestinationVisibility();
        }
        mapRootView.plainTo(mapData, event.item);
    }

    @Subscribe
    public void stopEvent(StopEvent event) {
        mapData.map.animateCamera(CameraUpdateFactory.newLatLngZoom(event.point, 5));
    }

    private void setupMap() {

        if (mapData.map == null) {

            MapFragmentEx mapFragment = (MapFragmentEx) (getFragmentManager().findFragmentById(R.id.map));
            mapData.map = mapFragment.getMap();

            if (mapData.map != null) {

                mapData.map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                //mapData.map.getUiSettings().setRotateGesturesEnabled(false);
                //mapData.map.getUiSettings().setZoomGesturesEnabled(false);


                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        mapRootView.mapReady(mapData);
                    }
                });

                new MapStateListener(mapFragment, this) {

                    @Override
                    protected void doScroll(int x, int y, boolean zoom, long duration) {
                        if (zoom) {
                            mapRootView.showMapItems(false);
                        } else {
                            mapRootView.scrollCamera(x, y, duration);
                        }

                    }

                    @Override
                    protected void onCameraChange(CameraPosition cameraPosition) {

                        mapData.position = cameraPosition;
                        mapData.projection = mapData.map.getProjection();
                        mapRootView.onCameraChange(mapData);
                    }
                };
            }
        }
    }
}
