package com.mariko.lalamap;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by AStefaniuk on 20.04.2015.
 */
public class DestinationList extends LinearLayout {

    private RecyclerView list;

    public DestinationList(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.destination_selector, this);

        list = (RecyclerView) findViewById(R.id.list);

        list.setLayoutManager(new GridLayoutManager(getContext(), 3));

        list.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new DestinationItemHolder(LayoutInflater.from(getContext()).inflate(R.layout.destination_item, null));
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                MapItem item = GApp.sInstance.getMapController().items.get(position);
                DestinationItemHolder destinationItemHolder = (DestinationItemHolder) holder;

                destinationItemHolder.image.setImageDrawable(item.drawable);
                destinationItemHolder.title.setText(item.icon);
            }

            @Override
            public int getItemCount() {
                return GApp.sInstance.getMapController().items.size();
            }
        });
    }

    private static class DestinationItemHolder extends android.support.v7.widget.RecyclerView.ViewHolder {

        public TextView title;
        public ImageView image;

        public DestinationItemHolder(View itemView) {
            super(itemView);

            this.title = (TextView) itemView.findViewById(R.id.title);
            this.image = (ImageView) itemView.findViewById(R.id.image);


        }
    }

}
