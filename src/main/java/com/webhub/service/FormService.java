package com.webhub.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.webhub.model.FormData;

@Service
public class FormService {
	
	List<FormData> details=new ArrayList<FormData>();
	
	public ResponseEntity<?> postForm(FormData form){
		
		details.add(form);
		return ResponseEntity.ok(details);
	}

}
