package com.webhub.model;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class dModel {
	
	private String id;
	
	private String longDescription;
	
	private List<Map<String, Object>> images;
	
	private String workshopName;
	
	private String licenceDate;
	
	private String shortDescription;
	
	private Object contactDetails;
	
	private String theme1;
	
	private String theme2;
	
	private String defaulLang;
	
	private Object favicon;
	
	private Object displayName;
	
	//private Object links;
	
	 private List<Map<String, String>> links;
	
	private String currency;
	
	private String key;
	
	

}
