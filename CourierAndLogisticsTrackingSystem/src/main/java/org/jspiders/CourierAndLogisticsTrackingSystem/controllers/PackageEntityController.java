package org.jspiders.CourierAndLogisticsTrackingSystem.controllers;

import java.util.List;

import org.jspiders.CourierAndLogisticsTrackingSystem.dto.ResponseStructure;
import org.jspiders.CourierAndLogisticsTrackingSystem.entities.PackageEntity;
import org.jspiders.CourierAndLogisticsTrackingSystem.services.PackageEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/packageEntity")
public class PackageEntityController {
	@Autowired
	private PackageEntityService packageEntityService;

	@GetMapping("/")
	public ResponseEntity<ResponseStructure<List<PackageEntity>>> getAllPackageEntities(){
		return new ResponseEntity<ResponseStructure<List<PackageEntity>>>(packageEntityService.getAllPackageEntitiesFromDatabase(),HttpStatus.FOUND);
	}
	
	@GetMapping("/id/{id}")
	public ResponseEntity<ResponseStructure<PackageEntity>> getPackageEntityById(@PathVariable int id){
		return new ResponseEntity<ResponseStructure<PackageEntity>>(packageEntityService.getPackageEntityByIdFromDatabase(id),HttpStatus.FOUND);
	}
	
	@PatchMapping("/update")
	public ResponseEntity<ResponseStructure<PackageEntity>> updatePackageEntity(@RequestBody PackageEntity p){
		return new ResponseEntity<ResponseStructure<PackageEntity>>(packageEntityService.updatePackageEntityInDatabase(p),HttpStatus.OK);
	}
	
	@GetMapping("/byShipment/{id}")
	public ResponseEntity<ResponseStructure<PackageEntity>> getPackageEntityByIShipment(@PathVariable int id){
		return new ResponseEntity<ResponseStructure<PackageEntity>>(packageEntityService.getPackageEntityByShipmentFromDatabase(id),HttpStatus.FOUND);
	}
}
