package com.webhub.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.webhub.config.ResourceNotFoundException;
import com.webhub.config.SquidexConfig;
import com.webhub.model.workshopD;
import com.webhub.security.Jwtutil1;

@Service
public class AuthService {
	
	@Value("${squidex.base.url}")
	private String squidexBaseUrl;

	@Autowired
	private SquidexConfig squidexConfig;

	@Autowired
	private RestTemplate restTemplatee;
	
	@Autowired
    private Jwtutil1 jwtUtil;
	
	public ResponseEntity<?> getTokenByName(String name) {
		// String clientName = "Penna";
		HttpHeaders headers = new HttpHeaders(); // takes values from Headers
		workshopD details = new workshopD();
		// RestTemplate restTemplate = new RestTemplate();
		headers.add("Authorization", "Bearer " + this.squidexConfig.getToken()); // adds token into Authorization, token
																					// generated from squidexConfig
		headers.add("X-Flatten", "True");// it will tell to the server to flatten the data and send the response
		headers.add("X-Languages", "en");// by default lang is eng
		headers.add("Content-Type", "application/json");
		HttpEntity<String> request = new HttpEntity<>(headers); // it will take all the Headers values like Token, lang
																// etc...
		String url = this.squidexBaseUrl + "profilessecurityconfig/"; // to the base URL we are appending the /workshops
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);// it will build and URL
		uriBuilder.queryParam("$filter", "data/name/iv+eq+'" + name + "'");// again to that URL, we are appending the end points
		ResponseEntity<SquidexBaseResponse> responseBaseEntity;
		SquidexItem item;
		String token = null;
		 Map<String,String> tokenm= new HashMap<>();
		try {
			responseBaseEntity = restTemplatee.exchange(uriBuilder.toUriString(), HttpMethod.GET, request,
					SquidexBaseResponse.class);// to the Rest Template we are passing full URL with
										// all end points, GET request,Headers
										// values,ResponseType class
		
		

		SquidexBaseResponse squidexBaseResponse = responseBaseEntity.getBody();
		try{
			item = squidexBaseResponse.getItems().get(0);
			if(item!=null) {
				
				  token = jwtUtil.generateToken(name);
			      tokenm.put("Access Token", token);
				
			}
		}
		catch(IndexOutOfBoundsException i) {
			throw new ResourceNotFoundException("Invalid profile Name, Please provide correct workshop Name");
		}
		} catch (HttpClientErrorException e) {
			return ResponseEntity.ofNullable("Invalid Profile details");

		}
		HashMap<String, Object> detail = item.getData();
	
		 details.setName(detail.get("name")); details.setPrefix(detail.get("Prefix"));
		details.setProfileRef(detail.get("profileRef"));
		 
		 System.out.println("Name: " + detail.get("name"));
		 System.out.println("Prefix: " + detail.get("Prefix"));
		 System.out.println("Profile Ref: " + detail.get("profileRef"));
		return ResponseEntity.ok(tokenm);
		 
	}

	public ResponseEntity<?> validateTokenAccessOther(String token) {
		String name=jwtUtil.extractName(token);
		HttpHeaders headers = new HttpHeaders(); // takes values from Headers
		workshopD details = new workshopD();
		// RestTemplate restTemplate = new RestTemplate();
		headers.add("Authorization", "Bearer " + this.squidexConfig.getToken()); // adds token into Authorization, token
																					// generated from squidexConfig
		headers.add("X-Flatten", "True");// it will tell to the server to flatten the data and send the response
		headers.add("X-Languages", "en");// by default lang is eng
		headers.add("Content-Type", "application/json");
		HttpEntity<String> request = new HttpEntity<>(headers); // it will take all the Headers values like Token, lang
																// etc...
		String url = this.squidexBaseUrl + "d-profile/"; // to the base URL we are appending the /workshops
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);// it will build and URL
		uriBuilder.queryParam("$filter", "data/workshopName/iv+eq+'" + name + "'");// again to that URL, we are appending the end points
		ResponseEntity<SquidexBaseResponse> responseBaseEntity;
		SquidexItem item;
		 Map<String,String> tokenm= new HashMap<>();
		
			responseBaseEntity = restTemplatee.exchange(uriBuilder.toUriString(), HttpMethod.GET, request,
					SquidexBaseResponse.class);// to the Rest Template we are passing full URL with
										// all end points, GET request,Headers
										// values,ResponseType class
		
		SquidexBaseResponse squidexBaseResponse = responseBaseEntity.getBody();
		
	
		
		
		return ResponseEntity.ok(squidexBaseResponse);
	}
	
	
	
}
