package com.webhub.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.webhub.config.SquidexConfig;

@Service
public class BrandsService {
	
	@Autowired
	private RestTemplate restTemplatee;
	
	@Value("${squidex.base.url}")
	private String squidexBaseUrl;

	@Autowired
	private SquidexConfig squidexConfig;
	
	
	public ResponseEntity<?> brands() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + this.squidexConfig.getToken()); 
		headers.add("X-Flatten", "True");
		headers.add("X-Languages", "en");
		HttpEntity<String> request = new HttpEntity<>(headers); 
		String url = this.squidexBaseUrl + "d-brands/"; 
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
		//uriBuilder.queryParam("$filter", "data/category/iv+eq+'" + id + "'");
		ResponseEntity<SquidexBaseResponse> responseBaseEntity = restTemplatee.exchange(uriBuilder.toUriString(),
				HttpMethod.GET, request, SquidexBaseResponse.class);// to the Rest Template we are passing full URL with
																	// all end points, GET request,Headers

		SquidexBaseResponse squidexBaseResponse = responseBaseEntity.getBody();
		List<SquidexItem> it=squidexBaseResponse.getItems();
		/*
		 * List<Map<String, Object>> responseData = new ArrayList<>();
		 * Map<String,Object> map= new HashMap<>(); for(SquidexItem itemm:it) {
		 * //Map<String, Object> data= new HashMap<>(); //data.put("id", itemm.getId());
		 * map.put("data", itemm.getData()); responseData.add(map); }
		 */
		
		return ResponseEntity.ok(it);
	}

}
