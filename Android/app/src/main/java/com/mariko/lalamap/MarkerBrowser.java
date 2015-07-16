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

        new Service().getWiki(item.key).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<WikiData>() {
            @Override
            public void call(WikiData wikiData) {
                title.setText(wikiData.getTitle());
                body.setText(wikiData.getBody());
            }
        });
    }

    /*
    private ImageView back;
    private ImageView forward;
    private ImageView youtube;
    private ImageView gallery;
    private ImageView wikipedia;
    private ImageView close;

    private final ViewFlipper flipper;
    private WebView[] web;
    private ImageView[] webButtons;

    public MarkerBrowser(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.marker_browser, this);

        this.back = (ImageView) findViewById(R.id.back);
        this.forward = (ImageView) findViewById(R.id.forward);

        this.youtube = (ImageView) findViewById(R.id.youtube);
        this.gallery = (ImageView) findViewById(R.id.gallery);
        this.wikipedia = (ImageView) findViewById(R.id.wikipedia);

        webButtons = new ImageView[]{this.youtube, this.gallery, this.wikipedia};

        this.close = (ImageView) findViewById(R.id.close);

        flipper = (ViewFlipper) findViewById(R.id.flipper);

        web = new WebView[3];
        for (int i = 0; i < web.length; i++) {
            web[i] = new WebView(context, null);
            initWebView(web[i]);

            flipper.addView(web[i]);

            final int index = i;

            webButtons[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    showView(web[index]);
                }
            });
        }

        this.close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                GApp.sInstance.getBus().post(new MapsActivity.ShowBrowserEvent(false));
            }
        });

        findViewById(R.id.tint).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                GApp.sInstance.getBus().post(new MapsActivity.ShowBrowserEvent(false));
            }
        });
    }

    private void showView(View view) {
        while (flipper.getCurrentView() != view) {
            flipper.showNext();
        }
        update();
    }

    private void update() {
        for (int i = 0; i < webButtons.length; i++) {
            if (flipper.getCurrentView() == web[i]) {
                webButtons[i].setBackgroundResource(R.drawable.marker_browser_selected_toolbar);
            } else {
                webButtons[i].setBackgroundResource(0);
            }
        }
    }

    private void initWebView(WebView webView) {

        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {

            }
        });

    }

    public void load(MapItem item) {

        web[0].loadUrl("https://m.youtube.com/#/results?search_query=" + URLEncoder.encode(item.key));
        web[1].loadUrl("https://www.google.com.ua/search?noj=1&site=imghp&tbm=isch&sa=1&oq=tig&q=" + URLEncoder.encode(item.key));
        web[2].loadUrl("http://en.wikipedia.org/wiki/" + URLEncoder.encode(item.key.replace(" ", "_")));

        update();

    }
    */
}
