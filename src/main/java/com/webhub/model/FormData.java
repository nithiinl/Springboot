package com.webhub.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormData {
	
	private String name;
	
	private String email;
	
	private String number;
	
	private String message;
	
	private String wId;

}
