package com.mariko.lalamap;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by AStefaniuk on 10.04.2015.
 */
public class GApp extends Application {
    public static GApp sInstance;
    public float DPI;

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
    }
}
