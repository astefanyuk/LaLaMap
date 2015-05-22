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

    private static final String API_URL = "https://dl.dropboxusercontent.com/u/8155438";

    public MapItemList items = new MapItemList();

    public MapController() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        }).start();


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

    private void loadData(){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .build();

        MapItemsService mapItemsService = restAdapter.create(MapItemsService.class);

        MapItemList mapItemList = mapItemsService.getList();
        for(MapItem item : mapItemList){
            item.init();
        }

        items = mapItemList;
    }
}
