package com.webhub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webhub.service.BrandsService;

@RestController
@RequestMapping("/brands")
//@CrossOrigin(origins = "http://localhost:3000")
public class BrandsController {
	
	
	@Autowired
	private BrandsService service;
	
	@PostMapping("/post")
	public ResponseEntity<?> getBrands(){
		return this.service.brands();
	}

}
