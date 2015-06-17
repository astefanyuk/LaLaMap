package com.mariko.lalamap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.badoo.mobile.util.WeakHandler;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.mariko.data.Service;
import com.mariko.map.MapFragmentEx;
import com.mariko.map.MapStateListener;
import com.squareup.otto.Subscribe;

import java.nio.channels.GatheringByteChannel;
import java.util.Timer;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MapsActivity extends Activity {

    public static class StopEvent {
        public final LatLng point;

        public StopEvent(LatLng point){
            this.point = point;
        }
    }

    public static class ShowBrowserEvent {
        public final boolean show;

        public ShowBrowserEvent(boolean show) {
            this.show = show;
        }
    }

    public static class DestinationSelectedEvent {
        public final MapItem item;

        public DestinationSelectedEvent(MapItem item) {
            this.item = item;
        }

    }

    private WeakHandler handler = new WeakHandler();
    private final Runnable changeBrowserVisibilityRunnable = new Runnable() {
        @Override
        public void run() {
            if(!mapBrowserVisible){
                changeMapBrowserVisibility();
            }
        }
    };
    private MapRootView mapRootView;

    private final MapData mapData = new MapData();
    private boolean mapBrowserVisible;

    private MarkerBrowser markerBrowser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mapRootView = (MapRootView) findViewById(R.id.mapRootView);
        setupMap();

        findViewById(R.id.showList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GApp.sInstance.getBus().post(new ShowBrowserEvent(true));

            }
        });

        markerBrowser = (MarkerBrowser) findViewById(R.id.markerBrowser);
    }

    @Override
    public void onBackPressed() {

        if (mapBrowserVisible) {
            changeMapBrowserVisibility();
            return;
        }

        super.onBackPressed();
    }


    private void changeMapBrowserVisibility() {
        handler.removeCallbacks(changeBrowserVisibilityRunnable);
        markerBrowser.setVisibility(View.VISIBLE);
        if (mapBrowserVisible) {
            YoYo.with(Techniques.SlideOutLeft).duration(300).playOn(markerBrowser);
        } else {
            YoYo.with(Techniques.SlideInLeft).duration(300).playOn(markerBrowser);
        }
        mapBrowserVisible = !mapBrowserVisible;
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
        handler.removeCallbacks(changeBrowserVisibilityRunnable);
        super.onPause();
    }

    @Subscribe
    public void destinationSelected(DestinationSelectedEvent event) {

        if (mapBrowserVisible) {
            changeMapBrowserVisibility();
        }

        markerBrowser.load(event.item);

        mapRootView.plainTo(mapData, event.item);
    }


    @Subscribe
    public void stopEvent(StopEvent event) {
        mapRootView.plainDone(mapData);

        handler.removeCallbacks(changeBrowserVisibilityRunnable);
        if (!mapBrowserVisible) {
            handler.postDelayed(changeBrowserVisibilityRunnable, 4000);
        }

    }

    @Subscribe
    public void showBrowserEvent(ShowBrowserEvent event) {
        if (event.show == mapBrowserVisible) {
            return;
        }
        changeMapBrowserVisibility();
    }

    private void setupMap() {

        if (mapData.map == null) {

            MapFragmentEx mapFragment = (MapFragmentEx) (getFragmentManager().findFragmentById(R.id.map));
            mapData.map = mapFragment.getMap();

            if (mapData.map != null) {

                mapData.map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                mapData.map.getUiSettings().setRotateGesturesEnabled(false);
                //mapData.map.getUiSettings().setZoomGesturesEnabled(false);


                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        new Service().getMapItemList().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<MapItemList>() {
                            @Override
                            public void call(MapItemList mapItems) {
                                GApp.sInstance.getMapController().setItems(mapItems);
                                mapRootView.mapReady(mapData);
                            }
                        });

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
