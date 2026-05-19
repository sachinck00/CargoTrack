package org.jspiders.CourierAndLogisticsTrackingSystem.repositories;

import java.util.Optional;

import org.jspiders.CourierAndLogisticsTrackingSystem.entities.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCredentialsRepository extends JpaRepository<UserCredentials, Integer>{
	Optional<UserCredentials> findByUserName(String userName);
}
