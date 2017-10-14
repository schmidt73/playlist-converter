package com.dhdh.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dhdh.app.spotify.Credentials;
import com.dhdh.app.spotify.AuthController;
import com.dhdh.app.spotify.Application;

import spark.Spark;


public class App 
{
    private static final String appURL = "http://45.76.16.65:4567";
    private static final String spotifyAuthRoute = "/spotifyAuthCallback";

    private static final String clientID = "1d83da544aac4b9eb89613b05fee1d21";
    private static final String clientSecret = "c49c56beaa3540568a104efe741b8e91";
    private static final String scope = "playlist-modify-public playlist-modify-private";
    private static final String redirectURL = appURL + spotifyAuthRoute;
    
    public static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args)
    {
        Credentials appCredentials = new Credentials(clientID, clientSecret,
                                                     scope, redirectURL);
        Application spotifyApp = new Application(appCredentials);
        AuthController controller = new AuthController(spotifyApp);

        Spark.get("/spotifyAuthorize", controller.authorize());
        Spark.get(spotifyAuthRoute, controller.acceptAuthorization());
    }
}
