package org.jspiders.CourierAndLogisticsTrackingSystem.repositories;

import java.util.Optional;

import org.jspiders.CourierAndLogisticsTrackingSystem.entities.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageEntityRepository extends JpaRepository<PackageEntity, Integer>{
	@Query("select p from PackageEntity p where p.shipment.id=?1")
	Optional<PackageEntity> findPackageEntityByShipmentId(int id);
}
