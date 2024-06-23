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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.webhub.config.ResourceNotFoundException;
import com.webhub.config.SquidexConfig;
import com.webhub.model.workshopD;

@Service
public class TestService {
	
	@Value("${squidex.base.url}")
	private String squidexBaseUrl;

	@Autowired
	private SquidexConfig squidexConfig;

	@Autowired
	private RestTemplate restTemplatee;
	
	
	public ResponseEntity<?> getWebHubClientData(String name) {
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
		try {
			responseBaseEntity = restTemplatee.exchange(uriBuilder.toUriString(), HttpMethod.GET, request,
					SquidexBaseResponse.class);// to the Rest Template we are passing full URL with
										// all end points, GET request,Headers
										// values,ResponseType class
		
		

		SquidexBaseResponse squidexBaseResponse = responseBaseEntity.getBody();
		try{
			item = squidexBaseResponse.getItems().get(0);
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
		 
		 String data=details.toString();
		 System.out.println("data:"+data);
		 
		 
		 // this will extract the profile ref from [] this to normal string
		 String profileRef = null;
		 int startIndex = data.indexOf("profileRef=[");
	        if (startIndex != -1) {        
	            startIndex += "profileRef=[".length();
	            int endIndex = data.indexOf("]", startIndex);
	            if (endIndex != -1) {
	            	profileRef= data.substring(startIndex, endIndex);
	                System.out.println(profileRef); 
	            } else {
	                System.out.println("Closing bracket for profileRef not found");
	            }
	        } else {
	            System.out.println("profileRef not found");
	        }
		 
		 
			String url1 = this.squidexBaseUrl + "d-profile/" + profileRef; // to the base URL we are appending the /workshops
			UriComponentsBuilder uriBuilder1 = UriComponentsBuilder.fromHttpUrl(url1);// it will build and URL
			// uriBuilder.queryParam("$filter", "data/d-profiles");// again to that URL, we
			// are appending the end points
			ResponseEntity<SquidexItem> responseBaseEntity1;
			try {
				responseBaseEntity1 = restTemplatee.exchange(uriBuilder1.toUriString(), HttpMethod.GET, request,
						SquidexItem.class);// to the Rest Template we are passing full URL with
											// all end points, GET request,Headers
											// values,ResponseType class
			} catch (HttpClientErrorException e) {
				return ResponseEntity.ofNullable("Invalid Profile details");

			}

			SquidexItem squidexBaseResponse1 = responseBaseEntity1.getBody();// then getting values from Body and

			HashMap<String, Object> it = squidexBaseResponse1.getData();
			/*
			 * System.out.println(it); resp.setId(squidexBaseResponse.getId());
			 * resp.setLongDescription((String) it.get("longDescription"));
			 * resp.setImages((List<Map<String, Object>>) it.get("images"));
			 * resp.setWorkshopName((String) it.get("workshopName"));
			 * resp.setLicenceDate((String) it.get("licenceExpiryDate"));
			 * resp.setShortDescription((String) it.get("shortDescription"));
			 * resp.setContactDetails(it.get("contactDetails")); resp.setTheme1((String)
			 * it.get("theme1")); resp.setTheme2((String) it.get("theme2"));
			 * resp.setDefaulLang((String) it.get("defaultLanguage"));
			 * resp.setFavicon(it.get("favIcon")); resp.setDisplayName((String)
			 * it.get("DisplayName")); resp.setLinks((List<Map<String, String>>)
			 * it.get("links")); resp.setCurrency((String) it.get("currency"));
			 * resp.setKey((String) it.get("key"));
			 */
			return ResponseEntity.ok(it);


		//return ResponseEntity.ok(detail);
	}
	
	
	public ResponseEntity<?> getWebHubClients(String name) {
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
		String url = this.squidexBaseUrl + "d-profile/"; // to the base URL we are appending the /workshops
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);// it will build and URL
		uriBuilder.queryParam("$filter", "data/workshopName/iv+eq+'" + name + "'");// again to that URL, we are appending the end points
		//List<SquidexItem> it;
		//HashMap<String, Object> it;
		ResponseEntity<SquidexBaseResponse> responseBaseEntity;
		
		
			responseBaseEntity = restTemplatee.exchange(uriBuilder.toUriString(), HttpMethod.GET, request,
					SquidexBaseResponse.class);// to the Rest Template we are passing full URL with
										// all end points, GET request,Headers
										// values,ResponseType class
		
			SquidexBaseResponse squidexBaseResponse = responseBaseEntity.getBody();
			/*
			 * if (squidexBaseResponse.getItems() == null ||
			 * squidexBaseResponse.getItems().isEmpty()) { throw new
			 * ResourceNotFoundException("The workshop name is Invalid, Please provide valid workshopName"
			 * ); }else { it = squidexBaseResponse.getData();
			 * 
			 * }
			 */
			 
			List<SquidexItem> item = squidexBaseResponse.getItems();
			List<Map<String, Object>> responseData = new ArrayList<>();
			
			if (squidexBaseResponse.getItems() == null || squidexBaseResponse.getItems().isEmpty()) { 
				
				throw new ResourceNotFoundException("The workshop name is Invalid, Please provide valid workshopName"); 
				}else { 

					for(SquidexItem itemm:item) {
						Map<String, Object> data=new HashMap<>();
						data.put("id", itemm.getId());
						data.put("data", itemm.getData());
						responseData.add(data);
					} 
				}
			
				
				
		
		return ResponseEntity.ok(responseData);
	}

	
	
	
}
