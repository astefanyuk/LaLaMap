package com.mariko.lalamap;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import java.net.URLEncoder;

/**
 * Created by AStefaniuk on 27.04.2015.
 */
public class MarkerBrowser extends LinearLayout {

    private WebView[] web;

    public MarkerBrowser(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.marker_browser, this);

        final ViewFlipper flipper = (ViewFlipper) findViewById(R.id.flipper);

        web = new WebView[3];
        for (int i = 0; i < web.length; i++) {
            web[i] = new WebView(context, null);
            initWebView(web[i]);

            flipper.addView(web[i]);
        }


        findViewById(R.id.title).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                flipper.showNext();
            }
        });
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
        web[0].loadUrl("https://www.youtube.com/results?search_query=" + URLEncoder.encode(item.key));
        web[1].loadUrl("https://www.google.com.ua/search?q=" + URLEncoder.encode(item.key) + "&es_sm=93&source=lnms&tbm=isch");

        //web2.loadUrl("https://uk.wikipedia.org/wiki/"  + URLEncoder.encode(item.key));
        web[2].loadUrl("http://en.wikipedia.org/wiki/Eiffel_Tower");
    }
}
