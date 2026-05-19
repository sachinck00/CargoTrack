package org.jspiders.CourierAndLogisticsTrackingSystem.controllers;

import java.util.List;

import javax.sound.midi.Patch;

import org.jspiders.CourierAndLogisticsTrackingSystem.dto.ResponseStructure;
import org.jspiders.CourierAndLogisticsTrackingSystem.dto.WareHouseDTO;
import org.jspiders.CourierAndLogisticsTrackingSystem.entities.WareHouse;
import org.jspiders.CourierAndLogisticsTrackingSystem.services.WareHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wareHouse")
public class WareHouseController {
	
	@Autowired
	private WareHouseService wareHouseService;
	
	@PostMapping("/add")
	public ResponseEntity<ResponseStructure<WareHouseDTO>> createWareHouse(@RequestBody WareHouse house){
		return new ResponseEntity<ResponseStructure<WareHouseDTO>>(wareHouseService.createWareHouseInDatabase(house), HttpStatus.CREATED);
	}
	
	@GetMapping("/")
	public ResponseEntity<ResponseStructure<List<WareHouseDTO>>> getAllWareHouses() {
	    return new ResponseEntity<ResponseStructure<List<WareHouseDTO>>>( wareHouseService.getAllWareHouses(),HttpStatus.FOUND);
	}
	
	@GetMapping("/id/{id}")
	public ResponseEntity<ResponseStructure<WareHouseDTO>> getWareHouseById(@PathVariable int id) {
	    return new ResponseEntity<ResponseStructure<WareHouseDTO>>( wareHouseService.getWareHouseById(id), HttpStatus.FOUND);
	}
	
	@GetMapping("/location/{location}")
	public ResponseEntity<ResponseStructure<WareHouseDTO>> getWareHouseByILocation(@PathVariable("location") String loc) {
	    return new ResponseEntity<ResponseStructure<WareHouseDTO>>(wareHouseService.getWareHouseByLocation(loc), HttpStatus.FOUND);
	}
	
	@GetMapping("/capacityGreaterThan/{capacity}")
	public ResponseEntity<ResponseStructure<List<WareHouseDTO>>> getWareHousesByCapacityGreateThan(@PathVariable int capacity) {
	    return new ResponseEntity<ResponseStructure<List<WareHouseDTO>>>(wareHouseService.getWareHousesByCapacityGreaterThan(capacity),HttpStatus.OK);
	}
	
	@PatchMapping("/update")
	public ResponseEntity<ResponseStructure<WareHouseDTO>> updateWareHouse(@RequestBody WareHouse w) {
	    return new ResponseEntity<ResponseStructure<WareHouseDTO>> (wareHouseService.updateWareHouse(w), HttpStatus.OK);
	} 
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseStructure<WareHouseDTO>> deleteWareHouseById(@PathVariable int id) {
	    return new ResponseEntity<ResponseStructure<WareHouseDTO>>(wareHouseService.deleteWareHouse(id), HttpStatus.OK);
	}
}
