package com.mariko.data;

import retrofit.http.GET;
/**
 * Created by AStefaniuk on 22.05.2015.
 */
public interface MapItemsService {
    @GET("/data.json")
    MapItemList getList();
}
