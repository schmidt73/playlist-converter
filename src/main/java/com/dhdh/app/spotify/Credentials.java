package com.dhdh.app.spotify;

import java.util.HashMap;
import java.util.Map;

import java.net.URLEncoder;

public class Credentials {
    public static final String AUTH_URL = "https://accounts.spotify.com/authorize";
    public static final String TOKEN_URL = "https://accounts.spotify.com/api/token";
    public static final String API_BASE_URL = "https://api.spotify.com";
    public static final String API_VERSION = "v1";

    public String clientID;
    public String secretID;
    public String scope;

    public String redirectURL;

    public Credentials(String clientID, String secretID, 
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

    public String buildEncodedAuth()
    {
        return base64Encode(clientID + ":" + secretID);
    }

    private static String base64Encode(String str)
    {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encode(str.getBytes());
    }


}
