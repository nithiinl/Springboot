package com.webhub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webhub.service.ServicesService;
import com.webhub.service.SquidexService;

@RestController
@RequestMapping("/prof")
//@CrossOrigin(origins = "http://localhost:3000")
public class ServicesController {
	
	@Autowired
	private SquidexService squidexService;
	
	@Autowired
	private ServicesService service;
	
	
	//it will get all the services
	@PostMapping("/services/all")
	public ResponseEntity<?> getServices() {
		return this.service.getAllServices();
	}
	
	//it will get the default services which are flagged to true
	@PostMapping("/services/flag")
	public ResponseEntity<?> getServicesCategoryById(){
		return this.service.getAllServicesByFlag();
	}
	
	//it will get all the categories
	@PostMapping("/services/categories")
	public ResponseEntity<?> getCategories(){
		return this.service.getAllCategories();
	}
	
	
	//this endpoint has to get the services and based on that service category id, it should also get categprory details of that data
	@PostMapping("/services/categories/")
	public ResponseEntity<?> getServicesWithCategory(){
		return this.service.getServiceWithCategory();
	}
	
	
	
	//it will get the categories based on the ID in services mapped to it
	@PostMapping("/categ")
	public ResponseEntity<?> getServCat(@RequestHeader String id){
		return this.service.serviceCategory(id);
	}
	
	//it will get more details for the id came from d-services
	@PostMapping("/getmoredetails")
	public ResponseEntity<?> getMoreDetai(@RequestHeader String id){
		return this.service.moreDetails(id);
	}
	
	@PostMapping("/getprofileservices")
	public ResponseEntity<?> getProfDetails(@RequestHeader String id){
		return this.service.getProfileLevelServices(id);
	}
	
	@PostMapping("/getprofileservicescateg")
	public ResponseEntity<?> getProfCateg(@RequestHeader String id){
		return this.service.getProfileServiceCatge(id);
	}
	
	@PostMapping("/getservicemaster")
	public ResponseEntity<?> getSeviceMaster(@RequestHeader String categid){
		return this.service.getServiceMasterServiceSegment(categid);
	}
	

}
