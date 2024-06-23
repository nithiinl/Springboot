package com.webhub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webhub.model.FormData;
import com.webhub.model.dModel;
import com.webhub.service.SquidexService;

@RestController
@RequestMapping("/squid")
//@CrossOrigin(origins = "http://localhost:3000")
public class SquidexController {

	@Autowired
	private SquidexService squidexService;

	/*
	 * @GetMapping("/profiles") public dModel getProfile() { return
	 * this.squidexService.getDprofile(); }
	 */

	@PostMapping("/profiles")
	public ResponseEntity<?> getPrefData(@RequestHeader String profRef) {
		return this.squidexService.getDprofile1(profRef);
	}

	@PostMapping("/workshop")
	public ResponseEntity<?> getWebHubClientData(@RequestHeader String name) {
		return this.squidexService.getWebHubClientData(name);
	}
	
	@PostMapping("/form")
 	public String postData(@RequestHeader String name,
 									@RequestHeader String email, @RequestHeader String number, 
 									@RequestHeader String message, @RequestHeader String wid){
 		
 		
 		FormData fd= new FormData(name,email,number,message,wid);
 		
 		 return squidexService.postForm(fd);
 		
 	}

}
