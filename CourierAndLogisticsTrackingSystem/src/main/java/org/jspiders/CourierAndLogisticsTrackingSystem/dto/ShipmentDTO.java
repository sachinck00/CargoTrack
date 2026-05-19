package org.jspiders.CourierAndLogisticsTrackingSystem.dto;

import java.time.LocalDate;

public class ShipmentDTO {
	private Integer id;
	
	private String source;
	
	private String destination;

		
	private LocalDate deliveryDate;
	
	private ShipmentStatusType shipmentStatus;
	
	private double amount;
	
	
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	public LocalDate getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(LocalDate deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	
	
	public ShipmentStatusType getShipmentStatus() {
		return shipmentStatus;
	}
	public void setShipmentStatus(ShipmentStatusType shipmentStatus) {
		this.shipmentStatus = shipmentStatus;
	}
}
