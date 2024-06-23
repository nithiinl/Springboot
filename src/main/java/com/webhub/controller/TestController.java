package com.webhub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.webhub.service.LogService;
import com.webhub.service.TestService;

@RestController
@RequestMapping("/{name}")
public class TestController {
	
	@Autowired
	private TestService service;
	
	
	//sent using url encoded form
	@PostMapping()
	public String getDet(@PathVariable String name, @RequestParam String username,@RequestParam String password) {
		return "name is"+name+" "+username+" "+password;
	}
	
	@PostMapping("/profile")
	public ResponseEntity<?> getWebHubClientData(@PathVariable String name) {
		return this.service.getWebHubClientData(name);
	}
	
	
	@GetMapping("/names")
	public ResponseEntity<?> getWebHubClients(@PathVariable String name) {
		return this.service.getWebHubClients(name);
	}
	
	  	@Autowired
	    private LogService logService;

	    @PostMapping("/log")
	    public void logSectionInView(@RequestBody LogRequest logRequest) {
	        logService.log(logRequest.getSection());
	    }

	    public static class LogRequest {
	        private String section;

	        // Getters and setters

	        public String getSection() {
	            return section;
	        }

	        public void setSection(String section) {
	            this.section = section;
	        }
	    }
	

	

}
