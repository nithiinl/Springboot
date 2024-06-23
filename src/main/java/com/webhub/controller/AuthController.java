package com.webhub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.webhub.security.JwtRequest;
import com.webhub.security.JwtResponse;
import com.webhub.security.JwtUtil;
import com.webhub.security.MyUserDetailsService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {
	
	@Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @PostMapping("/authenticate")
    public JwtResponse createAuthenticationToken(@RequestHeader String username, @RequestHeader String password) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
        } catch (AuthenticationException e) {
            throw new Exception("Incorrect username or password", e);
        }
        
        
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(username);

        final String accessToken = jwtUtil.generateAccessToken(userDetails);
        final String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        return new JwtResponse(accessToken, refreshToken);
    }
    
    
	/*
	 * @PostMapping("/refresh") public JwtResponse
	 * refreshAccessToken(@RequestParam("refreshToken") String refreshToken) throws
	 * Exception { // Validate the refresh token UserDetails userDetails =
	 * getUserDetailsFromRefreshToken(refreshToken);
	 * 
	 * // If refresh token is valid, generate a new access token if (userDetails !=
	 * null) { String accessToken = jwtUtil.generateAccessToken(userDetails); return
	 * new JwtResponse(accessToken, refreshToken); } else { throw new
	 * Exception("Invalid refresh token"); } }
	 */
    
    
    @PostMapping("/refresh")
    public JwtResponse refreshAccessToken(@RequestParam("refreshToken") String refreshToken) throws Exception {
        try {
        	
        	if (jwtUtil.isTokenExpired(refreshToken)) {
                throw new Exception("Refresh token is expired");
            }
        	
            String username = jwtUtil.extractUsername(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(refreshToken, userDetails)) {
                String newAccessToken = jwtUtil.generateAccessToken(userDetails);
                String newRefreshToken = jwtUtil.generateRefreshToken(userDetails);
                return new JwtResponse(newAccessToken, newRefreshToken);
            } else {
                throw new Exception("Invalid refresh token");
            }
        } catch (Exception e) {
            throw new Exception("Invalid refresh token", e);
        }
    }
    

    private UserDetails getUserDetailsFromRefreshToken(String refreshToken) {
        String username = jwtUtil.extractUsername(refreshToken);
        // Load user details from database or other storage mechanism
        return userDetailsService.loadUserByUsername(username);
    }

   
}
