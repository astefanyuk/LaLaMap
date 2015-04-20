package com.mariko.lalamap;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.squareup.otto.Bus;

/**
 * Created by AStefaniuk on 10.04.2015.
 */
public class GApp extends Application {
    public static GApp sInstance;
    public float DPI;

    private MapController mapController;
    private Bus bus;

    public GApp() {
        sInstance = this;


    }

    @Override
    public void onCreate() {
        super.onCreate();

        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        DPI = metrics.density;

        mapController = new MapController();
        bus = new Bus();
    }

    public MapController getMapController() {
        return mapController;
    }

    public Bus getBus() {
        return bus;
    }
}
