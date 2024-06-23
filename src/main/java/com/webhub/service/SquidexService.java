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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.webhub.config.SquidexConfig;
import com.webhub.model.DServices;
import com.webhub.model.FormData;
import com.webhub.model.dModel;
import com.webhub.model.workshopD;

@Service
public class SquidexService {

	@Value("${squidex.base.url}")
	private String squidexBaseUrl;

	@Autowired
	private SquidexConfig squidexConfig;

	@Autowired
	private RestTemplate restTemplatee;

	@Value("${twilio.accountSid}")
	private String accountSid;

	@Value("${twilio.authToken}")
	private String authToken;

	@Value("${twilio.phoneNumber}")
	private String twilioPhoneNumber;

	@Value("${twilio.whatsappNumber}")
	private String twilioWhatsAppNumber;

	// this will get only details of user based on profileRef from squidex
	public ResponseEntity<?> getDprofile1(String profRef) {

		HttpHeaders headers = new HttpHeaders(); // takes values from Headers
		dModel resp = new dModel();
		headers.add("Authorization", "Bearer " + this.squidexConfig.getToken()); // adds token into Authorization, token
																					// generated from squidexConfig
		headers.add("X-Flatten", "True");// it will tell to the server to flatten the data and send the response
		headers.add("X-Languages", "en");// by default lang is eng
		headers.add("Content-Type", "application/json");
		HttpEntity<String> request = new HttpEntity<>(headers); // it will take all the Headers values like Token, lang
																// etc...
		String url = this.squidexBaseUrl + "d-profile/" + profRef; // to the base URL we are appending the /workshops
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);// it will build and URL
		// uriBuilder.queryParam("$filter", "data/d-profiles");// again to that URL, we
		// are appending the end points
		ResponseEntity<SquidexItem> responseBaseEntity;
		try {
			responseBaseEntity = restTemplatee.exchange(uriBuilder.toUriString(), HttpMethod.GET, request,
					SquidexItem.class);// to the Rest Template we are passing full URL with
										// all end points, GET request,Headers
										// values,ResponseType class
		} catch (HttpClientErrorException e) {
			return ResponseEntity.ofNullable("Invalid Profile details");

		}

		SquidexItem squidexBaseResponse = responseBaseEntity.getBody();// then getting values from Body and

		HashMap<String, Object> it = squidexBaseResponse.getData();
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

	}

	public ResponseEntity<?> getWebHubClientData(String name) {
		// String clientName = "Penna";
		HttpHeaders headers = new HttpHeaders(); // takes values from Headers
		workshopD details = new workshopD();
		// RestTemplate restTemplate = new RestTemplate();
		headers.add("Authorization", "Bearer " + this.squidexConfig.getToken()); // adds token into Authorization, token
																					// generated from squidexConfig
		headers.add("X-Flatten", "True");// it will tell to the server to flatten the data and send the response
		headers.add("X-Languages", "en");// by default lang is eng
		HttpEntity<String> request = new HttpEntity<>(headers); // it will take all the Headers values like Token, lang
																// etc...
		String url = this.squidexBaseUrl + "profilessecurityconfig/"; // to the base URL we are appending the /workshops
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);// it will build and URL
		uriBuilder.queryParam("$filter", "data/name/iv+eq+'" + name + "'");// again to that URL, we are appending
																			// the end points
		ResponseEntity<SquidexBaseResponse> responseBaseEntity = restTemplatee.exchange(uriBuilder.toUriString(),
				HttpMethod.GET, request, SquidexBaseResponse.class);// to the Rest Template we are passing full URL with
																	// all end points, GET request,Headers

		SquidexBaseResponse squidexBaseResponse = responseBaseEntity.getBody();
		SquidexItem item = squidexBaseResponse.getItems().get(0);
		HashMap<String, Object> detail = item.getData();
		/*
		 * details.setName(detail.get("name")); details.setPrefix(detail.get("Prefix"));
		 * details.setProfileRef(detail.get("profileRef"));
		 * 
		 * System.out.println("Name: " + detail.get("name"));
		 * System.out.println("Prefix: " + detail.get("Prefix"));
		 * System.out.println("Profile Ref: " + detail.get("profileRef"))
		 */;

		return ResponseEntity.ok(detail);
	}

	public String postForm(FormData form) {
		Twilio.init(accountSid, authToken);

		Map<String, String> map = new HashMap<>();
		map.put("Name", form.getName());
		map.put("number", form.getNumber());
		map.put("email", form.getEmail());
		map.put("Message", form.getMessage());
		List<FormData> details = new ArrayList<FormData>();
		details.add(form);

		String template = "Hello %s !!.\nYour Service Request Has Been Successfully Received!!\nWe will contact you soon!!,\nYour number %s is registered. Email: %s. Message: %s";
		String formattedMessage = String.format(template, form.getName(), form.getNumber(), form.getEmail(),
				form.getMessage());
		Message message = Message
				.creator(new PhoneNumber(form.getNumber()), new PhoneNumber(twilioPhoneNumber), formattedMessage)
				.create();
		return message.getSid();
	}

	public ResponseEntity<?> getServiceCat() {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + this.squidexConfig.getToken());
		headers.add("X-Flatten", "True");
		headers.add("X-Languages", "en");
		headers.add("Content-Type", "application/json");
		HttpEntity<String> request = new HttpEntity<>(headers);
		String url = this.squidexBaseUrl + "d-service-categories/";
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
		ResponseEntity<SquidexItem> responseBaseEntity;
		try {
			responseBaseEntity = restTemplatee.exchange(uriBuilder.toUriString(), HttpMethod.GET, request,
					SquidexItem.class);
		} catch (HttpClientErrorException e) {
			return ResponseEntity.ofNullable("Invalid Profile details");

		}

		SquidexItem squidexBaseResponse = responseBaseEntity.getBody();

		return null;
	}

	// this will get the details of an user with name
	public void getWebHubClientData() {
		String clientName = "Penna";
		HttpHeaders headers = new HttpHeaders(); // takes values from Headers
		// RestTemplate restTemplate = new RestTemplate();
		headers.add("Authorization", "Bearer " + this.squidexConfig.getToken()); // adds token into Authorization, token
																					// generated from squidexConfig
		headers.add("X-Flatten", "True");// it will tell to the server to flatten the data and send the response
		headers.add("X-Languages", "en");// by default lang is eng
		HttpEntity<String> request = new HttpEntity<>(headers); // it will take all the Headers values like Token, lang
																// etc...
		String url = this.squidexBaseUrl + "workshops/"; // to the base URL we are appending the /workshops
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);// it will build and URL
		uriBuilder.queryParam("$filter", "data/name/iv+eq+'" + clientName + "'");// again to that URL, we are appending
																					// the end points
		ResponseEntity<SquidexBaseResponse> responseBaseEntity = restTemplatee.exchange(uriBuilder.toUriString(),
				HttpMethod.GET, request, SquidexBaseResponse.class);// to the Rest Template we are passing full URL with
																	// all end points, GET request,Headers
																	// values,ResponseType class
		SquidexBaseResponse squidexBaseResponse = responseBaseEntity.getBody();// then getting values from Body and
		for (SquidexItem item : squidexBaseResponse.getItems()) { // and here iterating them
			System.out.println(item.getData());
			HashMap<String, Object> s = item.getData();
			SquidexItem it = new SquidexItem();
			it.setName(s.get("name"));
			it.setPrefix(s.get("Prefix"));
			System.out.println("name:" + it.getName());
			System.out.println("prefix:" + it.getPrefix());
		}
	}

	// this will get all the profiles details
	public dModel getDprofile() {
		// String clientName = "Penna";
		HttpHeaders headers = new HttpHeaders(); // takes values from Headers
		//
//RestTemplate restTemplate = new RestTemplate();
		dModel resp = new dModel();
		headers.add("Authorization", "Bearer " + this.squidexConfig.getToken()); // adds token into Authorization, token
																					// generated from squidexConfig
		headers.add("X-Flatten", "True");// it will tell to the server to flatten the data and send the response
		headers.add("X-Languages", "en");// by default lang is eng
		HttpEntity<String> request = new HttpEntity<>(headers); // it will take all the Headers values like Token, lang
																// etc...
		String url = this.squidexBaseUrl + "/d-profile"; // to the base URL we are appending the /workshops
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);// it will build and URL
		// uriBuilder.queryParam("$filter", "data/d-profiles");// again to that URL, we
		// are appending the end points
		ResponseEntity<SquidexBaseResponse> responseBaseEntity = restTemplatee.exchange(uriBuilder.toUriString(),
				HttpMethod.GET, request, SquidexBaseResponse.class);// to the Rest Template we are passing full URL with
																	// all end points, GET request,Headers
																	// values,ResponseType class
		SquidexBaseResponse squidexBaseResponse = responseBaseEntity.getBody();// then getting values from Body and
		for (SquidexItem item : squidexBaseResponse.getItems()) { // and here iterating them

			HashMap<String, Object> data = item.getData();
			data.get("workshopName");

			System.out.println(item.getData()); // main data from squidex
			resp.setId((String) data.get("id"));
			resp.setLongDescription((String) data.get("longDescription"));
			resp.setImages((List<Map<String, Object>>) data.get("images"));
			resp.setWorkshopName((String) data.get("workshopName"));
			resp.setLicenceDate((String) data.get("licenceExpiryDate"));
			resp.setShortDescription((String) data.get("shortDescription"));
			resp.setContactDetails(data.get("contactDetails"));
			resp.setTheme1((String) data.get("theme1"));
			resp.setTheme2((String) data.get("theme2"));
			resp.setDefaulLang((String) data.get("defaultLanguage"));
			resp.setFavicon(data.get("favIcon"));
			resp.setDisplayName((String) data.get("DisplayName"));
			resp.setLinks((List<Map<String, String>>) data.get("links"));
			resp.setCurrency((String) data.get("currency"));
			resp.setKey((String) data.get("key"));

		}
		return resp;
	}

}
