package com.mariko.lalamap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.badoo.mobile.util.WeakHandler;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.mariko.data.Service;
import com.mariko.map.MapFragmentEx;
import com.mariko.map.MapStateListener;
import com.squareup.otto.Subscribe;

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
            if(!markerBrowserVisible){
                changeBrowserVisibility();
            }
        }
    };
    private MapRootView mapRootView;

    private final MapData mapData = new MapData();
    private boolean destinationListVisible;
    private boolean markerBrowserVisible;

    //private MarkerBrowser markerBrowser;
    private DestinationList destinationList;
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

        destinationList = (DestinationList) findViewById(R.id.destinationList);

        //markerBrowser = (MarkerBrowser) findViewById(R.id.markerBrowser);
        markerBrowser = (MarkerBrowser) findViewById(R.id.markerBrowser);
    }

    @Override
    public void onBackPressed() {

        if (destinationListVisible) {
            changeDestinationListVisibility();
            return;
        }

        if(markerBrowserVisible){
            changeBrowserVisibility();
            return;
        }

        super.onBackPressed();
    }


    private void changeDestinationListVisibility() {
        destinationList.setVisibility(View.VISIBLE);
        if (destinationListVisible) {
            YoYo.with(Techniques.SlideOutLeft).duration(300).playOn(destinationList);
        } else {
            YoYo.with(Techniques.ZoomInUp).duration(300).playOn(destinationList);
        }
        destinationListVisible = !destinationListVisible;
    }

    private void changeBrowserVisibility() {
        handler.removeCallbacks(changeBrowserVisibilityRunnable);
        markerBrowser.setVisibility(View.VISIBLE);
        if (markerBrowserVisible) {
            YoYo.with(Techniques.SlideOutLeft).duration(300).playOn(markerBrowser);
        } else {
            YoYo.with(Techniques.ZoomInUp).duration(300).playOn(markerBrowser);
        }
        markerBrowserVisible = !markerBrowserVisible;
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

        if (destinationListVisible) {
            changeDestinationListVisibility();
        }

        markerBrowser.load(event.item);

        mapRootView.plainTo(mapData, event.item);
    }


    @Subscribe
    public void stopEvent(StopEvent event) {
        mapRootView.plainDone(mapData);

        handler.removeCallbacks(changeBrowserVisibilityRunnable);
        if (!destinationListVisible) {
            handler.postDelayed(changeBrowserVisibilityRunnable, 4000);
        }

    }

    @Subscribe
    public void showBrowserEvent(ShowBrowserEvent event) {
        if (event.show == destinationListVisible) {
            return;
        }
        changeDestinationListVisibility();
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

                                Service service = new Service();
                                for(int i=0; i<mapItems.size(); i++){

                                    final MapItem item = mapItems.get(i);

                                    Glide.with(MapsActivity.this).load(service.getImageUrl(item)).asBitmap().listener(new RequestListener<String, Bitmap>() {
                                        @Override
                                        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                            item.init(resource);

                                            mapRootView.addItem(item);

                                            return false;
                                        }
                                    }).into(200, 200);
                                }
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
