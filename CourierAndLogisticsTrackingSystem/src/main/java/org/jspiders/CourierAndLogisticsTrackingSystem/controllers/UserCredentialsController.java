package org.jspiders.CourierAndLogisticsTrackingSystem.controllers;

import org.jspiders.CourierAndLogisticsTrackingSystem.dto.ResponseStructure;
import org.jspiders.CourierAndLogisticsTrackingSystem.entities.UserCredentials;
import org.jspiders.CourierAndLogisticsTrackingSystem.services.UserCredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userCrd")
public class UserCredentialsController {
	
	@Autowired
	private UserCredentialsService userCredentialsService;
	
	@PostMapping("/add")
	public ResponseEntity<ResponseStructure<UserCredentials>> addUserCrd(@RequestBody UserCredentials uc){
		return new ResponseEntity<ResponseStructure<UserCredentials>>(userCredentialsService.addUserCrd(uc) , HttpStatus.CREATED);
	}
}
