package com.webhub.security;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class Jwtutil1 {

	
	 private final String SECRET_KEY = "kjfdkjshfdwwe0242nfksdhf8232342sfsdhf84534nkjnshf45o434hw";
	
	 private SecretKey key;
	 
	 public Jwtutil1() {
    	 String secret="askdsahkasd2rhkjaify4r2uhhd3kjdiuqd2dad32irkjshkjdhakjdhkjah3badjdjjbdasdadhahdhahsdsfjlkjskdfsjdsfdjhjshfhsfdsfhsjdhffjshadvs";
    	 byte[] keyBytes = Base64.getDecoder().decode(secret.getBytes(StandardCharsets.UTF_8));
 		this.key = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS512.getJcaName()); // settng the decoded using SHA into key and initializing
	}
	 
	    public String generateToken(String name) {
	        return Jwts.builder()
	                .setSubject(name)
	                .setIssuedAt(new Date())
	                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
	                .signWith(SignatureAlgorithm.HS512, key)
	                .compact();
	    }

		/*
		 * public Boolean validateToken(String token, String name) { final String
		 * tokenName = extractName(token); return (tokenName.equals(name) &&
		 * !isTokenExpired(token)); }
		 */

	    
	    public String extractName(String token) {
	        return extractAllClaims(token).getSubject();
	    }

	    private Boolean isTokenExpired(String token) {
	        return extractAllClaims(token).getExpiration().before(new Date());
	    }

	    private Claims extractAllClaims(String token) {
	        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
	    }
}
