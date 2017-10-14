package com.dhdh.app;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.HttpResponse;
import com.auth0;


class TokenFactory {

	public static String masterKid = "43L5TK9F24";
	public static String alg = "ES256";
	publi static String masterIss = "SX2RLAM34X";

	public static void balls()
	{
		DevToken devToken = new DevToken();
		token.setKid(masterKid);
		token.setAlg(alg);
		token.setIss(masterIss);
		String token = createAndSign(devToken);
		System.out.println("Token is: " + token);
		try {
			DecodedJWT jwt = JWT.decode(token);
		} catch (JWTDecodeException e) {
			System.out.println("Invalid token");
		}

	}

	String void createAndSign(DevToken devToken)
	{
		try {
			Algorithm alg = Algorithm.ECDSA256(devToken.getKid());
			String token = JWT.create()
					.withIssuer("auth0")
					.sign(alg)
					.withClaim("iss" , devToken.getIss());
			return token;

		} catch (UnsupportedEncodingException e) {
			System.out.println("UTF-8 encoding not supported");
		} catch (JWTCreationException e) {
			System.out.println("Invalid signing config / couldn't convert claims.");
		}
	}
}
