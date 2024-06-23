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
import com.webhub.model.DServices;
import com.webhub.model.ServicesCategoryResponse;
import com.webhub.model.workshopD;

@Service
public class ServicesService {

	@Value("${squidex.base.url}")
	private String squidexBaseUrl;

	@Autowired
	private SquidexConfig squidexConfig;

	@Autowired
	private RestTemplate restTemplatee;

	// it will get all the services
	public ResponseEntity<?> getAllServices() {
		DServices service = new DServices();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + this.squidexConfig.getToken());
		headers.add("X-Flatten", "True");
		headers.add("X-Languages", "en");
		headers.add("Content-Type", "application/json");
		HttpEntity<String> request = new HttpEntity<>(headers);
		String url = this.squidexBaseUrl + "d-serviceses/";
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
		ResponseEntity<SquidexBaseResponse> responseBaseEntity;
		try {
			responseBaseEntity = restTemplatee.exchange(uriBuilder.toUriString(), HttpMethod.GET, request,
					SquidexBaseResponse.class);
		} catch (HttpClientErrorException e) {
			return ResponseEntity.ofNullable("Invalid Profile details");

		}

		SquidexBaseResponse squidexBaseResponse = responseBaseEntity.getBody();
		List<SquidexItem> item = squidexBaseResponse.getItems();
		List<Map<String, Object>> responseData = new ArrayList<>();
		if (item != null) {
			for (SquidexItem itemm : item) {
				Map<String, Object> responseMap = new HashMap<>();
				responseMap.put("data", itemm.getData());
				responseMap.put("id", itemm.getId());
				System.out.println("All data in d-servicess: " + itemm.getData());
				responseData.add(responseMap);

			}
		}
		return ResponseEntity.ok(responseData);

	}

	// it will get the services only if true is there
	public ResponseEntity<?> getAllServicesByFlag() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + this.squidexConfig.getToken());
		headers.add("X-Flatten", "True");
		headers.add("X-Languages", "en");
		headers.add("Content-Type", "application/json");
		HttpEntity<String> request = new HttpEntity<>(headers);
		String url = this.squidexBaseUrl + "d-serviceses/";
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
		ResponseEntity<SquidexBaseResponse> responseBaseEntity;
		try {
			responseBaseEntity = restTemplatee.exchange(uriBuilder.toUriString(), HttpMethod.GET, request,
					SquidexBaseResponse.class);
		} catch (HttpClientErrorException e) {
			return ResponseEntity.ofNullable("Invalid Profile details");

		}

		SquidexBaseResponse squidexBaseResponse = responseBaseEntity.getBody();
		List<SquidexItem> item = squidexBaseResponse.getItems();
		List<Map<String, Object>> responseData = new ArrayList<>();
		if (item != null) {
			for (SquidexItem itemm : item) {
				Map<String, Object> responseMap = new HashMap<>();
				System.out.println("All data in d-servicess: " + itemm.getData());
				HashMap<String, Object> data = itemm.getData();
				if (data != null && Boolean.TRUE.equals(data.get("defaultPromotingService"))) {
					responseMap.put("data", itemm.getData());
					responseMap.put("id", itemm.getId());
					//responseData.add(data);
					responseData.add(responseMap);

				}

			}
		}
		/*
		 * service.setId(item.getId()); HashMap<String, Object> detail = item.getData();
		 * service.setIcon(detail.get("icon"));
		 * service.setShortDescription(detail.get("shortDescription"));
		 * service.setServiceName(detail.get("serviceName"));
		 * service.setCategory(detail.get("category")); System.out.println(service);
		 */

		return ResponseEntity.ok(responseData);

	}
	
	//this will get all the categories
	public ResponseEntity<?> getAllCategories() {
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + this.squidexConfig.getToken());
		headers.add("X-Flatten", "True");
		headers.add("X-Languages", "en");
		headers.add("Content-Type", "application/json");
		HttpEntity<String> request = new HttpEntity<>(headers);
		String url = this.squidexBaseUrl + "d-service-categories/";
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
		ResponseEntity<SquidexBaseResponse> responseBaseEntity;
		try {
			responseBaseEntity = restTemplatee.exchange(uriBuilder.toUriString(), HttpMethod.GET, request,
					SquidexBaseResponse.class);
		} catch (HttpClientErrorException e) {
			return ResponseEntity.ofNullable("Invalid Profile details");

		}

		SquidexBaseResponse squidexBaseResponse = responseBaseEntity.getBody();
		List<SquidexItem> item = squidexBaseResponse.getItems();
		
		List<Map<String, Object>> responseData1 = new ArrayList<>();
		if (item != null) {
			for (SquidexItem itemm : item) {
				Map<String, Object> responseMap = new HashMap<>();
				responseMap.put("id", itemm.getId());
				responseMap.put("data", itemm.getData());
				responseData1.add(responseMap);
			}
		}
		return ResponseEntity.ok(responseData1);
	}

	// it will get all the services
	public ResponseEntity<?> getServiceWithCategory() {
		
		//1. here first i will get the data from d-services api and extract category id 
		//2. and pass that id to d-service-category api and get the data
		//3. then i will send the final response with both service data and category data
		
		DServices service = new DServices();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + this.squidexConfig.getToken());
		headers.add("X-Flatten", "True");
		headers.add("X-Languages", "en");
		headers.add("Content-Type", "application/json");
		HttpEntity<String> request = new HttpEntity<>(headers);
		String url = this.squidexBaseUrl + "d-service-categories/";
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
		ResponseEntity<SquidexBaseResponse> responseBaseEntity;
		try {
			responseBaseEntity = restTemplatee.exchange(uriBuilder.toUriString(), HttpMethod.GET, request,
					SquidexBaseResponse.class);
		} catch (HttpClientErrorException e) {
			throw new ResourceNotFoundException("Not found or URL is wrong");// here this is an custom exception handler

		}

		SquidexBaseResponse squidexBaseResponse = responseBaseEntity.getBody();
		List<SquidexItem> item = squidexBaseResponse.getItems();
		List<Map<String, Object>> responseData = new ArrayList<>();
		
		
		for(SquidexItem itemm:item) {
			Map<String, Object> data=new HashMap<>();
			data.put("id", itemm.getId());
			data.put("data", itemm.getData());
			responseData.add(data);
		}
		
		
		
		
		/*List<Map<String, Object>> responseData = new ArrayList<>();
	
		if (item != null) {
			for (SquidexItem itemm : item) {
				Map<String, Object> responseMap = new HashMap<>();
				List<Object> li=(List<Object>) itemm.getData().get("category");
				
				for(Object list:li) {
					System.out.println(list);
					
				}
				System.out.println(li.get(0));//here it is getting first category id, if multiple are there
				System.out.println(itemm.getData().get("category"));
				responseMap.put("data", itemm.getData());
				responseMap.put("id", itemm.getId());
				System.out.println("All data in d-servicess: " + itemm.getData());
				responseData.add(responseMap);

			}*/
		
		return ResponseEntity.ok(responseData);

	}

	public ResponseEntity<?> serviceCategory(String id) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + this.squidexConfig.getToken()); 
		headers.add("X-Flatten", "True");
		headers.add("X-Languages", "en");
		HttpEntity<String> request = new HttpEntity<>(headers); 
		String url = this.squidexBaseUrl + "d-serviceses/"; 
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
		uriBuilder.queryParam("$filter", "data/category/iv+eq+'" + id + "'");
		ResponseEntity<SquidexBaseResponse> responseBaseEntity = restTemplatee.exchange(uriBuilder.toUriString(),
				HttpMethod.GET, request, SquidexBaseResponse.class);// to the Rest Template we are passing full URL with
																	// all end points, GET request,Headers

		SquidexBaseResponse squidexBaseResponse = responseBaseEntity.getBody();
		List<SquidexItem> it=squidexBaseResponse.getItems();
		
		return ResponseEntity.ok(it);
	}
	
	
	//it will get more details of the clicked service
	public ResponseEntity<?> moreDetails(String dservid) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + this.squidexConfig.getToken()); 
		headers.add("X-Flatten", "True");
		headers.add("X-Languages", "en");
		HttpEntity<String> request = new HttpEntity<>(headers); 
		String url = this.squidexBaseUrl + "d-service-details/"; 
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
		uriBuilder.queryParam("$filter", "data/service/iv+eq+'" + dservid + "'");
		ResponseEntity<SquidexBaseResponse> responseBaseEntity = restTemplatee.exchange(uriBuilder.toUriString(),
				HttpMethod.GET, request, SquidexBaseResponse.class);// to the Rest Template we are passing full URL with
																	// all end points, GET request,Headers

		SquidexBaseResponse squidexBaseResponse = responseBaseEntity.getBody();
		List<SquidexItem> it=squidexBaseResponse.getItems();
		
		return ResponseEntity.ok(it);
	}
	
	
	
	
	//This is will get the services based on profile ID
	public ResponseEntity<?> getProfileLevelServices(String profId){
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + this.squidexConfig.getToken()); 
		headers.add("X-Flatten", "True");
		headers.add("X-Languages", "en");
		HttpEntity<String> request = new HttpEntity<>(headers); 
		String url = this.squidexBaseUrl + "d-profileservicemapping/"; 
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
		uriBuilder.queryParam("$filter", "data/profile/iv+eq+'" + profId + "'");
		ResponseEntity<SquidexBaseResponse> responseBaseEntity;
		try {
			responseBaseEntity = restTemplatee.exchange(uriBuilder.toUriString(), HttpMethod.GET, request,
					SquidexBaseResponse.class);
		} catch (HttpClientErrorException e) {
			throw new ResourceNotFoundException("Not found or URL is wrong");// here this is an custom exception handler

		}

		SquidexBaseResponse squidexBaseResponse = responseBaseEntity.getBody();
		List<SquidexItem> item = squidexBaseResponse.getItems();
		
		List<Map<String, Object>> responseData = new ArrayList<>();
		HashMap<String, Object> item1 = null;
		for(SquidexItem itemm:item) {
			System.out.println(itemm.getId());
			
			List<String> serv=(List<String>) itemm.getData().get("services");
			for(String serviceId:serv) {
				Map<String, Object> id= new HashMap<>();
				id.put("id", serviceId);
				String url1 = this.squidexBaseUrl + "d-serviceses/" + serviceId; 
				UriComponentsBuilder uriBuilder1 = UriComponentsBuilder.fromHttpUrl(url1);
				ResponseEntity<SquidexBaseResponse> responseBaseEntity1;
				try {
					responseBaseEntity1 = restTemplatee.exchange(uriBuilder1.toUriString(), HttpMethod.GET, request,
							SquidexBaseResponse.class);
				} catch (HttpClientErrorException e) {
					throw new ResourceNotFoundException("Not found or URL is wrong");// here this is an custom exception handler

				}

				SquidexBaseResponse squidexBaseResponse1 = responseBaseEntity1.getBody();
				item1 = squidexBaseResponse1.getData();
				id.put("data", item1);
				responseData.add(id);
				
			}
			
		}
		return ResponseEntity.ok(responseData);
		
	}

//this api will take profileID and loads all the servicesegmentsMaster it has or category
public ResponseEntity<?> getProfileServiceCatge(String profId){
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + this.squidexConfig.getToken()); 
		headers.add("X-Flatten", "True");
		headers.add("X-Languages", "en");
		HttpEntity<String> request = new HttpEntity<>(headers); 
		String url = this.squidexBaseUrl + "d-profileservicecategoriemapping/"; 
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
		uriBuilder.queryParam("$filter", "data/profile/iv+eq+'" + profId + "'");
		ResponseEntity<SquidexBaseResponse> responseBaseEntity;
		try {
			responseBaseEntity = restTemplatee.exchange(uriBuilder.toUriString(), HttpMethod.GET, request,
					SquidexBaseResponse.class);
		} catch (HttpClientErrorException e) {
			throw new ResourceNotFoundException("Not found or URL is wrong");// here this is an custom exception handler

		}

		SquidexBaseResponse squidexBaseResponse = responseBaseEntity.getBody();
		List<SquidexItem> item = squidexBaseResponse.getItems();
		List<Map<String, Object>> responseData = new ArrayList<>();
		HashMap<String, Object> item1 = null;
		for(SquidexItem itemm:item) {
			System.out.println(itemm.getId());
			
			List<String> serv=(List<String>) itemm.getData().get("serviceCategory");
			for(String serviceId:serv) {
				Map<String, Object> id= new HashMap<>();
				id.put("id", serviceId);String url1 = this.squidexBaseUrl + "d-service-categories/" + serviceId; 
				UriComponentsBuilder uriBuilder1 = UriComponentsBuilder.fromHttpUrl(url1);
				ResponseEntity<SquidexBaseResponse> responseBaseEntity1;
				try {
					responseBaseEntity1 = restTemplatee.exchange(uriBuilder1.toUriString(), HttpMethod.GET, request,
							SquidexBaseResponse.class);
				} catch (HttpClientErrorException e) {
					throw new ResourceNotFoundException("Not found or URL is wrong");// here this is an custom exception handler

				}

				SquidexBaseResponse squidexBaseResponse1 = responseBaseEntity1.getBody();
				item1 = squidexBaseResponse1.getData();
				id.put("data", item1);
				responseData.add(id);
				
			}
		}
		return ResponseEntity.ok(responseData);
}

//this api will take car or bike ID when he clicks on it and loads the data from the serviceMaster
public ResponseEntity<?> getServiceMasterServiceSegment(String segmentId){
	
	HttpHeaders headers = new HttpHeaders();
	headers.add("Authorization", "Bearer " + this.squidexConfig.getToken()); 
	headers.add("X-Flatten", "True");
	headers.add("X-Languages", "en");
	HttpEntity<String> request = new HttpEntity<>(headers); 
	String url = this.squidexBaseUrl + "d-serviceses/"; 
	UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
	uriBuilder.queryParam("$filter", "data/category/iv+eq+'" + segmentId + "'");
	ResponseEntity<SquidexBaseResponse> responseBaseEntity;
	try {
		responseBaseEntity = restTemplatee.exchange(uriBuilder.toUriString(), HttpMethod.GET, request,
				SquidexBaseResponse.class);
	} catch (HttpClientErrorException e) {
		throw new ResourceNotFoundException("Not found or URL is wrong");// here this is an custom exception handler

	}

	SquidexBaseResponse squidexBaseResponse = responseBaseEntity.getBody();
	List<SquidexItem> item = squidexBaseResponse.getItems();
	List<Map<String, Object>> responseData = new ArrayList<>();
	for(SquidexItem itemm:item) {
		Map<String, Object> data=new HashMap<>();
		data.put("id", itemm.getId());
		data.put("data", itemm.getData());
		responseData.add(data);
	}
	return ResponseEntity.ok(responseData);
}
	
}
