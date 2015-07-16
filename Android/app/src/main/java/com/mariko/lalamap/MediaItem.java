package com.mariko.lalamap;

/**
 * Created by AStefaniuk on 16.07.2015.
 */
public class MediaItem {
    public String title;
    public String description;
    public String url;
    public boolean video;

    public MediaItem(){

    }

    public MediaItem(String url, String title, String description){

        this.url = url;
        this.title = title;
        this.description = description;
    }

    public MediaItem(String url){

        this.url = url;

    }

    public MediaItem(String url, boolean video){

        this.url = url;
        this.video = video;

    }
}
