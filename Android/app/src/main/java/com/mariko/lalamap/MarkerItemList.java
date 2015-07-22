package com.mariko.lalamap;

import com.mariko.data.MapItem;

import java.util.ArrayList;

/**
 * Created by astef on 26.04.15.
 */
public class MarkerItemList extends ArrayList<MarkerItem> {
    public MarkerItem findByMapItem(MapItem item) {
        for (MarkerItem markerItem : this) {
            if (item == markerItem.item) {
                return markerItem;
            }
        }
        return null;
    }
}
