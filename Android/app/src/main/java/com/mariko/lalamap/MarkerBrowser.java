package com.mariko.lalamap;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

/**
 * Created by AStefaniuk on 27.04.2015.
 */
public class MarkerBrowser extends LinearLayout {

    private WebView webView;

    public MarkerBrowser(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.marker_browser, this);

        webView = (WebView) findViewById(R.id.web);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {

            }
        });
    }

    public void load(MapItem item) {
        // webView.loadUrl("https://www.youtube.com/results?search_query=" + item.key);
        webView.loadUrl("https://www.google.com.ua/search?q=Eiffel+Tower&es_sm=93&source=lnms&tbm=isch");
    }
}
