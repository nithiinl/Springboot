package com.webhub.security;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.webhub.service.RandomStringGenerator;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
	
	
		private RandomStringGenerator string;

		private SecretKey key;
	
	    private long accessTokenExpiration=3600000; // 1hour
	    private long refreshTokenExpiration = 604800000; // 7 days
	    
	    public JwtUtil() {
	    	 String secret="askdsahkasd2rhkjaify4r2uhhd3kjdiuqd2dad32irkjshkjdhakjdhkjah3badjdjjbdasdadhahdhahsdsfjlkjskdfsjdsfdjhjshfhsfdsfhsjdhffjshadvs";
	    	 byte[] keyBytes = Base64.getDecoder().decode(secret.getBytes(StandardCharsets.UTF_8));
	 		this.key = new SecretKeySpec(keyBytes, "HmacSHA512"); // settng the decoded using SHA into key and initializing
		}
	    
	    
	    public String generateAccessToken(UserDetails userDetails) {
	        return generateToken(userDetails, accessTokenExpiration);
	    }

	    public String generateRefreshToken(UserDetails userDetails) {
	        return generateToken(userDetails, refreshTokenExpiration); // Set expiration to 0 for refresh token
	    }
	    
	    public String generateToken(UserDetails userDetails, long expiration) {
	    
	        return Jwts.builder()
	        		.setSubject(userDetails.getUsername())
	        		.claim("claims", string.generateRandomString())
	                .setIssuedAt(new Date(System.currentTimeMillis()))
	                .setExpiration(new Date(System.currentTimeMillis() + expiration))
	                .signWith(SignatureAlgorithm.HS512, key)
	                .compact();
	    }
	    

	    public boolean validateToken(String token, UserDetails userDetails) {
	        final String username = extractUsername(token);
	        String extractedPart= extractFirstAndLastCharacters(token);
	        if(token.equals(extractedPart)) {
	        	 return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));	
	        }else {
	        	return "Invalid Token" != null;
	        }
	       
	    }

	    public String extractUsername(String token) {
	        return extractClaim(token, Claims::getSubject);
	    }

	    public Date extractExpiration(String token) {
	        return extractClaim(token, Claims::getExpiration);
	    }

	    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
	        final Claims claims = extractAllClaims(token);
	        return claimsResolver.apply(claims);
	    }

	    private Claims extractAllClaims(String token) {
	        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
	    }

	    public boolean isTokenExpired(String token) {
	        return extractExpiration(token).before(new Date());
	    }
	    
	    
	    public String extractFirstAndLastCharacters(String token) {
	        if (token.length() < 10) {
	            throw new IllegalArgumentException("Token is too short to extract first and last 5 characters");
	        }
	        String first5 = token.substring(0, 10);
	        String last5 = token.substring(token.length() - 10);
	        return first5 + "" + last5; 
	    }
}
