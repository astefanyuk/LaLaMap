package com.mariko.lalamap;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import java.net.URLEncoder;
import java.util.logging.Handler;

/**
 * Created by AStefaniuk on 27.04.2015.
 */
public class MarkerBrowser extends LinearLayout {

    private ImageView back;
    private ImageView forward;
    private ImageView marker;
    private ImageView youtube;
    private ImageView wikipedia;
    private ImageView close;

    private final DestinationList destinationList;
    private final ViewFlipper flipper;
    private WebView[] web;
    private ImageView[] webButtons;

    public MarkerBrowser(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.marker_browser, this);

        this.back = (ImageView) findViewById(R.id.back);
        this.forward = (ImageView) findViewById(R.id.forward);

        this.marker = (ImageView) findViewById(R.id.marker);
        this.youtube = (ImageView) findViewById(R.id.youtube);
        this.wikipedia = (ImageView) findViewById(R.id.wikipedia);

        webButtons = new ImageView[]{this.marker, this.youtube, this.wikipedia};

        this.close = (ImageView) findViewById(R.id.close);

        flipper = (ViewFlipper) findViewById(R.id.flipper);

        destinationList = new DestinationList(context, null);
        flipper.addView(destinationList);

        web = new WebView[3];
        for (int i = 0; i < web.length; i++) {
            web[i] = new WebView(context, null);
            initWebView(web[i]);

            flipper.addView(web[i]);
        }

        this.marker.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentWebView(web[0]);
            }
        });

        this.youtube.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentWebView(web[1]);
            }
        });

        this.wikipedia.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentWebView(web[2]);
            }
        });
    }

    private void setCurrentWebView(WebView webView) {
        while (flipper.getCurrentView() != webView) {
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

        if (flipper.getCurrentView() == destinationList) {
            flipper.showNext();
        }

        update();
    }
}
