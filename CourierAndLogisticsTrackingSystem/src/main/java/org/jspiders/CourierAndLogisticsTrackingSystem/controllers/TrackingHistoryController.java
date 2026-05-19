package org.jspiders.CourierAndLogisticsTrackingSystem.controllers;

import java.util.List;

import org.jspiders.CourierAndLogisticsTrackingSystem.dto.ResponseStructure;
import org.jspiders.CourierAndLogisticsTrackingSystem.dto.ShipmentStatusType;
import org.jspiders.CourierAndLogisticsTrackingSystem.dto.TrackingStatusType;
import org.jspiders.CourierAndLogisticsTrackingSystem.entities.TrackingHistory;
import org.jspiders.CourierAndLogisticsTrackingSystem.services.TrackingHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trackingHistory")
public class TrackingHistoryController {
	
	@Autowired
	private TrackingHistoryService trackingHistoryService;
		
	@PatchMapping("/updateTrackingHistory")
	public ResponseEntity<ResponseStructure<TrackingHistory>> updateTrakingHistory(@RequestBody TrackingHistory th){
		return new ResponseEntity<ResponseStructure<TrackingHistory>>(trackingHistoryService.updateTrakingHistoryInDatabase(th),HttpStatus.OK);
	}
	
	@GetMapping("/trackingNumber/{tn}")
	public ResponseEntity<ResponseStructure<List<TrackingHistory>>> getTrackingHistoryByTrackingNumber(@PathVariable int tn){
		return new ResponseEntity<ResponseStructure<List<TrackingHistory>>>(trackingHistoryService.getTrackingHistoryByTrackingNumberFromDatabase(tn),HttpStatus.FOUND);
	}
	
	@GetMapping("/")
	public ResponseEntity<ResponseStructure<List<TrackingHistory>>> getAllTrackingHistory(){
		return new ResponseEntity<ResponseStructure<List<TrackingHistory>>>(trackingHistoryService.getAllTrackingHistoryFromDatabase(),HttpStatus.FOUND);
	}
	
	@GetMapping("/id/{id}")
	public ResponseEntity<ResponseStructure<TrackingHistory>> getTrakingHistoryById(@PathVariable int id){
		return new ResponseEntity<ResponseStructure<TrackingHistory>>(trackingHistoryService.getTrackingHistoryByIdFromDatabase(id),HttpStatus.OK);
	}
	
	@GetMapping("/shipmentId/{id}")
	public ResponseEntity<ResponseStructure<List<TrackingHistory>>> getTrackingHistoryByShipmentId(@PathVariable int id){
		return new ResponseEntity<ResponseStructure<List<TrackingHistory>>>(trackingHistoryService.getTrackingHistoryByShipmentIdFromDatabase(id),HttpStatus.FOUND);
	}
	
	@GetMapping("/trackingStatus/{status}")
	public ResponseEntity<ResponseStructure<List<TrackingHistory>>> getTrackingHistoryByTrackingStatus(@PathVariable TrackingStatusType status){
		return new ResponseEntity<ResponseStructure<List<TrackingHistory>>>(trackingHistoryService.getTrackingHistoryByTrackingStatusFromDatabase(status),HttpStatus.FOUND);
	}
	
	
}
