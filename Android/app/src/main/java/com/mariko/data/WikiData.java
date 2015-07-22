package com.mariko.data;

import java.util.Hashtable;

/**
 * Created by AStefaniuk on 16.07.2015.
 */
public class WikiData {

    //{"batchcomplete":"","query":{"pages":{"214505":{"pageid":214505,"ns":0,"title":"Tower","extract":"A tower is a tall structure"}}}}

    public Query query;

    public static class Query {
        public Hashtable<String, Page> pages;
    }

    public static class Page {
        public String pageid;
        public String title;
        public String extract;
    }

    public String getTitle() {
        if (query != null && query.pages != null && !query.pages.isEmpty()) {
            return query.pages.values().iterator().next().title;
        }
        return "";
    }

    public String getBody() {
        if (query != null && query.pages != null && !query.pages.isEmpty()) {
            return query.pages.values().iterator().next().extract;
        }
        return "";
    }
}
