package org.jspiders.CourierAndLogisticsTrackingSystem.entities;

import org.jspiders.CourierAndLogisticsTrackingSystem.dto.TrackingStatusType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class TrackingHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String location;
	
	private String remarks;
	
	@Enumerated(EnumType.STRING)
	private TrackingStatusType trackingStatus;
	
	@ManyToOne
	@JoinColumn(name = "shipment_id", nullable = true)
    @JsonIgnore
	private Shipment shipment;
	
	public Shipment getShipment() {
		return shipment;
	}

	public void setShipment(Shipment shipment) {
		this.shipment = shipment;
	}

	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public TrackingStatusType getTrackingStatus() {
		return trackingStatus;
	}

	public void setTrackingStatus(TrackingStatusType trackingStatus) {
		this.trackingStatus = trackingStatus;
	}
	
	
	
}
