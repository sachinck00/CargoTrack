package org.jspiders.CourierAndLogisticsTrackingSystem.services;

import java.util.Optional;

import org.jspiders.CourierAndLogisticsTrackingSystem.configurations.CustomUserDetails;
import org.jspiders.CourierAndLogisticsTrackingSystem.entities.UserCredentials;
import org.jspiders.CourierAndLogisticsTrackingSystem.repositories.UserCredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	private UserCredentialsRepository userCredentialsRepo;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Optional<UserCredentials> opt = userCredentialsRepo.findByUserName(username);
		if(opt.isPresent()) {
			return new CustomUserDetails(opt.get());
		}
		
		return null;
	}
	

}
