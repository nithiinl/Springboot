package com.webhub.model;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicesCategoryResponse {
	
	private String id;
    private Map<String, Object> data;

}
