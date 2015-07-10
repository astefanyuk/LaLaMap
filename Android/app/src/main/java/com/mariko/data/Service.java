package com.mariko.data;

import com.mariko.lalamap.MapItem;
import com.mariko.lalamap.MapItemList;

import retrofit.RestAdapter;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by AStefaniuk on 17.06.2015.
 */
public class Service {

    private static final String API_URL = "https://dl.dropboxusercontent.com/u/8155438";

    public Observable<MapItemList> getMapItemList(){

        return Observable.create(new Observable.OnSubscribe<MapItemList>() {
            @Override
            public void call(Subscriber<? super MapItemList> subscriber) {
                try {

                    RestAdapter restAdapter = new RestAdapter.Builder()
                            .setEndpoint(API_URL)
                            .build();

                    MapItemsService mapItemsService = restAdapter.create(MapItemsService.class);

                    MapItemList mapItemList = mapItemsService.getList();
                    subscriber.onNext(mapItemList);

                }catch(Throwable e){
                    subscriber.onError(e);
                }finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    public String getImageUrl(MapItem item){
        return API_URL + "/" + item.icon + ".png";
    }
}
