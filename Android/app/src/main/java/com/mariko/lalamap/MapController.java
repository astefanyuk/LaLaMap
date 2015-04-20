package com.mariko.lalamap;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by AStefaniuk on 20.04.2015.
 */
public class MapController {
    public MapItemList items = new MapItemList();

    public MapController() {
        try {
            items = (new Gson().fromJson(new InputStreamReader(GApp.sInstance.getAssets().open("data.json")), MapItemList.class));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (MapItem mapItem : items) {
            mapItem.init();
        }
    }
}
