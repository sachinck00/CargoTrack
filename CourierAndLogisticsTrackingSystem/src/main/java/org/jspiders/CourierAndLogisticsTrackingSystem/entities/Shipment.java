package org.jspiders.CourierAndLogisticsTrackingSystem.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.jspiders.CourierAndLogisticsTrackingSystem.dto.ShipmentStatusType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Shipment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(unique = true)
	private int trackingNumber;
	
	private String source;
	
	private String destination;
	
	private int distance;
	

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public void setTrackingNumber(int trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	private double weight;
	
	@CreationTimestamp
	private LocalDateTime shipmentDate;
	
	private LocalDate deliveryDate;
	
	@Enumerated(EnumType.STRING)
	private ShipmentStatusType shipmentStatus;
	
	
	@ManyToOne
	@JoinColumn(name = "customer_id",nullable = true)
	private Customer customer;
	
	
	@ManyToOne
	@JoinColumn(name = "wareHouse_id",nullable = true)
	private WareHouse wareHouse;
	
	@ManyToOne
	@JoinColumn(name="deliveryAgent_id" , nullable = true)
	private DeliveryAgent deliveryAgent;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "packageEntity_id",nullable = true)
	private PackageEntity packageEntity;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="payment_id",nullable = true)
	private Payment payment;
	
	@OneToMany(mappedBy = "shipment" , cascade = CascadeType.ALL)
	private List<TrackingHistory> trackingHistory = new ArrayList<>();
	
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public WareHouse getWareHouse() {
		return wareHouse;
	}

	public void setWareHouse(WareHouse wareHouse) {
		this.wareHouse = wareHouse;
	}

	public DeliveryAgent getDeliveryAgent() {
		return deliveryAgent;
	}

	public void setDeliveryAgent(DeliveryAgent deliveryAgent) {
		this.deliveryAgent = deliveryAgent;
	}

	public PackageEntity getPackageEntity() {
		return packageEntity;
	}

	public void setPackageEntity(PackageEntity packageEntity) {
		this.packageEntity = packageEntity;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	
	
	public List<TrackingHistory> getTrackingHistory() {
		return trackingHistory;
	}

	public void setTrackingHistory(List<TrackingHistory> trackingHistory) {
		this.trackingHistory = trackingHistory;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(Integer trackingNumber) {
		this.trackingNumber = trackingNumber;
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

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public LocalDateTime getShipmentDate() {
		return shipmentDate;
	}

	public void setShipmentDate(LocalDateTime shipmentDate) {
		this.shipmentDate = shipmentDate;
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
