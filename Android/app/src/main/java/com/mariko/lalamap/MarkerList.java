package com.mariko.lalamap;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mariko.data.MapItem;
import com.mariko.data.Service;

/**
 * Created by AStefaniuk on 20.04.2015.
 */
public class MarkerList extends RelativeLayout {

    private RecyclerView list;


    public MarkerList(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.marker_list, this);

        list = (RecyclerView) findViewById(R.id.list);

        list.setLayoutManager(new StaggeredGridLayoutManager(getSpanCount(), StaggeredGridLayoutManager.VERTICAL));

        list.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new DestinationItemHolder(LayoutInflater.from(getContext()).inflate(R.layout.marker_item, null));
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                MapItem item = GApp.sInstance.getMapController().items.get(position);
                DestinationItemHolder destinationItemHolder = (DestinationItemHolder) holder;

                destinationItemHolder.item = item;
                Glide.with(getContext()).load(new Service().getImageUrl(item)).fitCenter().into(destinationItemHolder.image);

            }

            @Override
            public int getItemCount() {
                return GApp.sInstance.getMapController().items.size();
            }
        });
    }

    public void update() {
        ((StaggeredGridLayoutManager) list.getLayoutManager()).setSpanCount(getSpanCount());
    }

    public int getSpanCount() {
        return GApp.sInstance.isLandscape() ? 2 : 1;
    }


    public void mapReady() {
        list.getAdapter().notifyDataSetChanged();
    }

    private static class DestinationItemHolder extends android.support.v7.widget.RecyclerView.ViewHolder {

        public TextView title;
        public ImageView image;
        public MapItem item;

        public DestinationItemHolder(View itemView) {
            super(itemView);

            this.title = (TextView) itemView.findViewById(R.id.title);
            this.image = (ImageView) itemView.findViewById(R.id.image);

            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    GApp.sInstance.getBus().post(new MapsActivity.DestinationSelectedEvent(item));
                }
            });
        }
    }

}
