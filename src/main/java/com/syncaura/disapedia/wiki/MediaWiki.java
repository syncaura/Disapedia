package com.syncaura.disapedia.wiki;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.sun.istack.internal.NotNull;

import java.util.HashMap;

/**
 * Crafted in the heart of Wales!
 *
 * @author CaLxCyMru
 */
public class MediaWiki {

    public static final String API_BASE = "api.php?";

    private String baseWikiUrl;

    private HttpTransport httpTransport;

    public MediaWiki(String baseWikiUrl) {
        if (!baseWikiUrl.endsWith("/")) {
            baseWikiUrl = baseWikiUrl + "/";
        }
        this.baseWikiUrl = baseWikiUrl;
        this.httpTransport = new NetHttpTransport();
    }

    public String getBaseWikiUrl() {
        return baseWikiUrl;
    }

    public String getWikiUrl() {
        return getBaseWikiUrl() + API_BASE;
    }

    public String getActionAsJson(@NotNull String action, @NotNull HashMap<String, String> params) {
        HttpRequestFactory requestFactory = httpTransport.createRequestFactory();

        String apiUrl = getWikiUrl() + "action=" + action;

        for (String paramKey : params.keySet()) {
            apiUrl = apiUrl + "&" + paramKey + "=" + params.get(paramKey);
        }

        try {
            HttpRequest request = requestFactory.buildGetRequest(new GenericUrl(apiUrl));

            HttpResponse response = request.execute();

            if (response.isSuccessStatusCode()) {
                return response.parseAsString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public SearchResult search(String searchQuery, String lang) {
        HashMap<String, String> params = new HashMap<>();
        params.put("search", searchQuery);
        params.put("uselang", lang);

        String rawJsonCallback = getActionAsJson("opensearch", params);

        if(rawJsonCallback == null) {
            return null;
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        JsonArray callback = gson.fromJson(rawJsonCallback, JsonArray.class);

        if(callback == null || callback.size() != 4) {
            System.out.println("API Could have changed!");
            return null;
        }

        try {
            return new SearchResult(callback);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
