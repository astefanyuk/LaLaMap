package com.mariko.data;

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

    public void setItems(MapItemList items) {
        this.items = items;
    }
}
