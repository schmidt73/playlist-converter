package com.dhdh.app.spotify;

import com.dhdh.app.App;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import spark.Route;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.json.JSONObject;

import java.util.Set;
import java.util.Base64;

public class AuthController {
    private Application spotifyApp;

    public AuthController(Application spotifyApp)
    {
        this.spotifyApp = spotifyApp;
    }

    public Route authorize()
    {
        Route callback = (req, resp) -> {
            resp.redirect(spotifyApp.getCred().buildAuthenticationURL());
            return null;
        };

        return callback;
    }


    public Route acceptAuthorization()
    {
        Route callback = (req, resp) -> {
            String authToken = req.queryParams("code");

            App.logger.debug(authToken);

            try {
                HttpResponse<String> resp = performFinalAuth(authToken);
                App.logger.debug(resp.getBody());
            } catch(UnirestException e) {
                App.logger.debug(e);
            }
           
            resp.redirect("http://www.google.com");
            return null;
        };

        return callback;
    }

    private HttpResponse<String> performFinalAuth(String authToken) throws UnirestExecption
    {
        Credentials cred = spotifyApp.getCred();

        JSONObject data = new JSONObject()
            .put("grant_type", "authorization_code")
            .put("code", authToken)
            .put("redirect_uri", cred.redirectURL);

        JSONObject headers = new JSONObject()
            .put("Authorization", "Basic " + cred.buildEncodedAuth());

        return Unirest.post(Credentials.SPOTIFY_TOKEN_URL)
            .header("Authorization", "Basic " + cred.buildEncodedAuth())
            .body(data.toString()).asString();
    }
}
