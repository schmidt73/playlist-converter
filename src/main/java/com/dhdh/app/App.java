package com.dhdh.app;

import com.dhdh.app.spotify.SpotifyCredentials;
import com.dhdh.app.spotify.SpotifyController;

import spark.Spark;

public class App 
{
    private static final String appURL = "http://45.76.16.65:8080";
    private static final String spotifyAuthRoute = "/spotifyAuthCallback/";

    private static final String clientID = "1d83da544aac4b9eb89613b05fee1d21";
    private static final String clientSecret = "c49c56beaa3540568a104efe741b8e91";
    private static final String scope = "playlist-modify-public playlist-modify-private";
    private static final String redirectURL = appURL + spotifyAuthRoute;
    
    public static void main(String[] args)
    {
        SpotifyCredentials appCredentials = new SpotifyCredentials(clientID, clientSecret,
                                                                   scope, redirectURL);
        SpotifyController controller = new SpotifyController(appCredentials);

        Spark.get("/redirect", controller.buildCallbackRoute());
    }
}
