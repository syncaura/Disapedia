package com.syncaura.disapedia.config;

/**
 * Crafted in the heart of Wales!
 *
 * @author CaLxCyMru
 */
@SuppressWarnings("FieldCanBeLocal")
public class WikiData {

    private String apiBaseUrl = "https://en.wikipedia.org/w/"; // We want to use wikipedia for the default one

    private String subWikiName = ""; // By default let's not include a sub wiki

    private int resultLimit = 10; // Default to 10 results

    private String language = "en"; // Default to english

    public String getApiBaseUrl() {
        return apiBaseUrl;
    }

    public String getSubWikiName() {
        return subWikiName;
    }

    public String getWikiUrl() {
        return getWikiUrl(getSubWikiName());
    }

    public String getWikiUrl(String wikiName) {
        return apiBaseUrl.replace("{subWikiName}", wikiName);
    }

    public int getResultLimit() {
        return resultLimit;
    }

    public String getLanguage() {
        return language;
    }
}
