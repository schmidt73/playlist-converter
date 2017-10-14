package com.dhdh.app.spotify;

public class Application {
    private Credentials cred;

    public Application(Credentials cred) {
        this.cred = cred; 
    }

    public Credentials getCred()
    {
        return cred;
    } 



}
