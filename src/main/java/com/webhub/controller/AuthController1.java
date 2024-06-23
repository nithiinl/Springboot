package com.webhub.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.webhub.security.Jwtutil1;
import com.webhub.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController1 {
	
	 @Autowired
	    private Jwtutil1 jwtUtil;
	 
	 @Autowired
	 private AuthService service;

	    @PostMapping("/generate-token")
	    public ResponseEntity<Map<String, String>> generateToken(@RequestParam String name) {
	        String token = jwtUtil.generateToken(name);
	        Map<String,String> tokenm= new HashMap<>();
	        tokenm.put("Access Token", token);
	        return ResponseEntity.ok(tokenm);
	    }

		/*
		 * @GetMapping("/validate-token") public ResponseEntity<Boolean>
		 * validateToken(@RequestParam String token, @RequestParam String name) {
		 * boolean isValid = jwtUtil.validateToken(token, name); return
		 * ResponseEntity.ok(isValid); }
		 */
	    
	    @PostMapping("/profile")
		public ResponseEntity<?> getTokenByName(@RequestParam String name) {
			return this.service.getTokenByName(name);
		}
	    
	    	
	    @PostMapping("/token")
		public ResponseEntity<?> validateToken(@RequestParam String token) {
			return this.service.validateTokenAccessOther(token);
		}
	@GetMapping("/string")
	public String getString(){
		return "Hello world";
	}
}
