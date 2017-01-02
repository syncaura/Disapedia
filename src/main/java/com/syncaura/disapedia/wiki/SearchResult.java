package com.syncaura.disapedia.wiki;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

/**
 * Crafted in the heart of Wales!
 *
 * @author CaLxCyMru
 */
public class SearchResult {

    private String query;
    private String[] matchedElements;
    private String[] unknownParam;
    private String[] urls;

    public SearchResult(JsonArray search) throws Exception {
        this.query = search.get(0).getAsString();

        Gson gson = new Gson();

        this.matchedElements = gson.fromJson(search.get(1).toString(), String[].class);
        this.unknownParam = gson.fromJson(search.get(2).toString(), String[].class);
        this.urls = gson.fromJson(search.get(3).toString(), String[].class);
    }

    public String getQuery() {
        return query;
    }

    public String[] getMatchedElements() {
        return matchedElements;
    }

    public String[] getUnknownParam() {
        return unknownParam;
    }

    public String[] getUrls() {
        return urls;
    }

    public boolean isValid() {
        return getUrls() != null && urls.length > 0;
    }
}
