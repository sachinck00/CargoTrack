package org.jspiders.CourierAndLogisticsTrackingSystem.services;

import org.jspiders.CourierAndLogisticsTrackingSystem.dto.ResponseStructure;
import org.jspiders.CourierAndLogisticsTrackingSystem.entities.UserCredentials;
import org.jspiders.CourierAndLogisticsTrackingSystem.repositories.UserCredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UserCredentialsService {
	@Autowired
	private UserCredentialsRepository userCredentialsRepo;
	
	public ResponseStructure<UserCredentials> addUserCrd(UserCredentials uc){
		ResponseStructure<UserCredentials> res = new ResponseStructure<>();
		res.setData(userCredentialsRepo.save(uc));
		res.setMessage("user credentials added");
		res.setStatusCode(HttpStatus.CREATED.value());
		return res;
	}
}
