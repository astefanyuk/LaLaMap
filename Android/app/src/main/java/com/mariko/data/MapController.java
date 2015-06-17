package com.mariko.data;

import com.google.gson.Gson;
import com.mariko.lalamap.GApp;
import com.mariko.lalamap.MapItem;
import com.mariko.lalamap.MapItemList;

import java.io.IOException;
import java.io.InputStreamReader;

import retrofit.RestAdapter;

/**
 * Created by AStefaniuk on 20.04.2015.
 */
public class MapController {

    public MapItemList items = new MapItemList();

    public MapController() {


        /*
        // Create an instance of our GitHub API interface.
        GitHub github = restAdapter.create(GitHub.class);

        try {
            items = (new Gson().fromJson(new InputStreamReader(GApp.sInstance.getAssets().open("data.json")), MapItemList.class));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (MapItem mapItem : items) {
            mapItem.init();
        }
        */
    }

    public void setItems(MapItemList items){
        this.items = items;
    }
}
