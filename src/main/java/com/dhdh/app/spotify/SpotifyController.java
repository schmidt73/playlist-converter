package com.dhdh.app.spotify;

import spark.Route;

public class SpotifyController {
    private SpotifyCredentials cred;

    public SpotifyController(SpotifyCredentials cred)
    {
        this.cred = cred;
    }

    public Route buildCallbackRoute()
    {
        Route callbackRoute = (req, resp) -> {
            resp.redirect(cred.buildAuthenticationURL());
            return null;
        };

        return callbackRoute;
    }

}
