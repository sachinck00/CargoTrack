package org.jspiders.CourierAndLogisticsTrackingSystem.repositories;

import java.util.List;
import java.util.Optional;

import org.jspiders.CourierAndLogisticsTrackingSystem.dto.ShipmentStatusType;
import org.jspiders.CourierAndLogisticsTrackingSystem.dto.TrackingStatusType;
import org.jspiders.CourierAndLogisticsTrackingSystem.entities.TrackingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackingHistoryRepository extends JpaRepository<TrackingHistory, Integer>{
	//@Query("select t from TrackingHistory t where t.shipment.id=?1")
	List<TrackingHistory> findByShipmentId(int id);
	
	@Query("select t from TrackingHistory t where t.shipment.trackingNumber=?1")
	List<TrackingHistory> getTrackingHistoryByTrackingNumber(int TrackingNumber);
	
	@Query("select t from TrackingHistory t where t.trackingStatus=?1")
	List<TrackingHistory> findTrackingHistoryByTrackingStatus(TrackingStatusType trackingStatus);
	
	@Query("select t from TrackingHistory t where t.shipment.id=?1")
	List<TrackingHistory> findTrackingHistoryByShipmentId(int shipmentId);
}
