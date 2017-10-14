package com.dhdh.app.spotify;

import java.util.HashMap;
import java.util.Map;

import java.net.URLEncoder;

public class SpotifyCredentials {
    final String AUTH_URL = "https://accounts.spotify.com/authorize";
    final String TOKEN_URL = "https://accounts.spotify.com/api/token";
    final String API_BASE_URL = "https://api.spotify.com";
    final String API_VERSION = "v1";

    public String clientID;
    public String secretID;
    public String scope;

    public String redirectURL;

    public SpotifyCredentials(String clientID, String secretID, 
                              String scope, String redirectURL)
    {
        this.clientID = clientID;
        this.secretID = secretID;
        this.scope = scope;
        this.redirectURL = redirectURL;
    }

    public String buildAuthenticationURL()
    {
        Map<String, String> authQueryParams = new HashMap<String, String>();
        authQueryParams.put("response_type", "code");
        authQueryParams.put("redirect_uri", redirectURL);
        authQueryParams.put("scope", scope);
        authQueryParams.put("client_id", clientID);
    
        String urlArgs = "";
        for (Map.Entry<String, String> entry : authQueryParams.entrySet()) {
            urlArgs += entry.getKey() + "=" + URLEncoder.encode(entry.getValue()) + "&";
        }

        return AUTH_URL + "/?" + urlArgs;
    }
}
