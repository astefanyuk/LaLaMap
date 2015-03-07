package com.mariko.lalamap;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.widget.RelativeLayout;

/**
 * Created by AStefaniuk on 06.03.2015.
 */
public class YoutubePlayer extends RelativeLayout {

    private WebView webView;

    public YoutubePlayer(Context context, AttributeSet attrs) {
        super(context, attrs);

        /*
        LayoutInflater.from(context).inflate(R.layout.youtube_player, this);

        webView = (WebView) findViewById(R.id.webView);

        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.setWebChromeClient(new WebChromeClient ());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.setInitialScale(1);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        //mWebView.loadDataWithBaseURL(null,load,"text/html","UTF-8",null);

        load();*/
        /*
        com.google.android.youtube.player.YouTubePlayerView youTubePlayerView = new YouTubePlayerView(context);

        youTubePlayerView.initialize(context.getString(R.string.google_maps_key), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo("aq8RQtERJVo");
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });

        addView(youTubePlayerView);
        */

    }

    public void load(){
        /*

        String youtubeId = "aq8RQtERJVo";

        int width = 400;
        int height = 400;

        String video= "<table bgcolor=\"#666666\"><tr><td><iframe width=\"" +
                width +
                "\" height=\"" +
                height +
                "\"frameborder=\"0\" id=\"player\" type=\"text/html\"src=\"http://www.youtube.com/embed/" +
                youtubeId +
                "?enablejsapi=1&autoplay=1\"></iframe> </td> </tr><tr><td></table>";

        String table =  "<iframe src=\"http://www.youtube.com/embed/x5uYnBgxee8?rel=0&amp;autoplay=1\" width=\"960\" height=\"447\" frameborder=\"0\" allowfullscreen></iframe>";

        webView.loadData(table, "text/html", "UTF-8");
        */
    }
}
