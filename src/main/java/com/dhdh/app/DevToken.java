package com.dhdh.app;

import java.util.*;

class DevToken {
	private String alg, kid, iss, iat, exp;

	DevToken() {
		alg = null;
		kid = null;
		iss = null;
		iat = null;
		exp = null;
	}	

	void setAlg(String algorithim){
		this.alg = algorithim;
	}

	String getAlg(){
		return alg;
	}

	void setKid(String keyIdentifier){
		this.kid = keyIdentifier;
	}

	String getKid() {
		return kid;
	}

	void setIss(String issuer){
		this.iss = issuer;
	}

	String getIss(){
		return iss;
	}

	void setIat(String issued_at){
		this.iat = issued_at;
	}

	String getIat(){
		return iat;
	}

	void setExp(String expiration_time){
		this.exp = expiration_time;
	}

	String getExp(){
		return exp;
	}
}