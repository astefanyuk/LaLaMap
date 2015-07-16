package com.mariko.data;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Query;
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

                    for(MapItem item : mapItemList){

                       // item.images.add(new MediaItem("https://www.youtube.com/watch?v=xwidefc2wpc", true));

                        item.images.add(new MediaItem("http://vignette4.wikia.nocookie.net/animalcrossing/images/e/e3/Lion-013-2048x2048.jpg/revision/latest?cb=20130406213028"));
                        item.images.add(new MediaItem("https://upload.wikimedia.org/wikipedia/commons/a/a1/Lions_Family_Portrait_Masai_Mara.jpg"));
                        item.images.add(new MediaItem("http://cdn.zmescience.com/wp-content/uploads/2014/04/lionanimals-male-lion-wallpaper-hd-wallpapers-1600x1200px-b6cot4xc.jpg"));
                        item.images.add(new MediaItem("https://upload.wikimedia.org/wikipedia/commons/2/23/African_Lion_3.jpg"));
                        item.images.add(new MediaItem("https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcRyVKXbX70XSN5pmS96n4XPnPZCQAdlfh6JBeOgvLINwfMlh8ibTQ"));
                        item.images.add(new MediaItem("https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcT33wyCbiCvcoVfy_aFJa1iqLpx7HNWsXfPZJCgdwBcL8c9BvJ7bQ"));
                        item.images.add(new MediaItem("http://i.telegraph.co.uk/multimedia/archive/02846/papa-lion-1_2846987b.jpg"));
                        item.images.add(new MediaItem("https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcQ41t0Z7bQ4qFm3Tl2CnVHQYmQNSugMmc3y_Awp1OClOlsw69ah"));
                    }

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
        return API_URL + "/images/" + item.icon + ".png";
    }

    public Observable<WikiData> getWiki(String data){

        RequestInterceptor requestInterceptor = new RequestInterceptor() {

            @Override
            public void intercept(RequestInterceptor.RequestFacade request) {
                request.addHeader("Accept", "application/json");
                int maxAge = 60 * 60;
                request.addHeader("Cache-Control", "public, max-age=" + maxAge);
            }
        };

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://en.wikipedia.org")
                .setRequestInterceptor(requestInterceptor)
                .build();

        WikiService service = restAdapter.create(WikiService.class);

        return service.getData(data.replace(" ", "_"));

    }

    public static interface WikiService {
        @GET("/w/api.php?format=json&action=query&prop=extracts&exintro=&explaintext=")
        Observable<WikiData> getData(@Query("titles") String title);
    }
}
