package com.mariko.lalamap;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mariko.data.Service;
import com.mariko.data.WikiData;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by AStefaniuk on 27.04.2015.
 */
public class MarkerBrowser extends LinearLayout {

    private final RecyclerView list;
    private final TextView title;
    private final TextView body;

    public MarkerBrowser(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.marker_browser, this);

        list = (RecyclerView) findViewById(R.id.list);
        title = (TextView) findViewById(R.id.title);
        body = (TextView) findViewById(R.id.body);

        list.setHasFixedSize(true);

        list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        list.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new AAAItemHolder(LayoutInflater.from(getContext()).inflate(R.layout.image_item, null));
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

                MediaItem mediaItem = item.images.get(position);

                AAAItemHolder destinationItemHolder = (AAAItemHolder) holder;

                destinationItemHolder.item = mediaItem;
                Glide.with(getContext()).load(mediaItem.url).override(400, 400).fitCenter().into(destinationItemHolder.image);

                //Glide.with(getContext()).load(new Service().getImageUrl(item)).into(destinationItemHolder.image);
            }

            @Override
            public int getItemCount() {
                return item == null ? 0 : item.images.size();
            }
        });
    }

    private static class AAAItemHolder extends android.support.v7.widget.RecyclerView.ViewHolder {

        //public TextView title;
        public ImageView image;
        public MediaItem item;

        public AAAItemHolder(View itemView) {
            super(itemView);

            //this.title = (TextView) itemView.findViewById(R.id.title);
            this.image = (ImageView) itemView.findViewById(R.id.image);

            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                  //  GApp.sInstance.getBus().post(new MapsActivity.DestinationSelectedEvent(item));
                }
            });
        }
    }

    private MapItem item;

    public void load(MapItem item) {
        this.item = item;

        list.getAdapter().notifyDataSetChanged();

        title.setText("");
        body.setText("");

        new Service().getWiki(item.key).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<WikiData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                //TODO: show error
            }

            @Override
            public void onNext(WikiData wikiData) {
                title.setText(wikiData.getTitle());
                body.setText(wikiData.getBody());
            }
        });
    }
}
