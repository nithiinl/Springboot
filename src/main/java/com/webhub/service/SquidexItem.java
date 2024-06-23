package com.webhub.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SquidexItem {

	private String id;
	
	private HashMap<String, Object> data;
	
	private List<SquidexItem> items;
	
	private Object prefix;
	
	private Object name;
	


}