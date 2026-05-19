package org.jspiders.CourierAndLogisticsTrackingSystem.services;


import java.util.List;
import java.util.Optional;

import org.jspiders.CourierAndLogisticsTrackingSystem.dto.ResponseStructure;
import org.jspiders.CourierAndLogisticsTrackingSystem.entities.PackageEntity;
import org.jspiders.CourierAndLogisticsTrackingSystem.exceptions.IdNotFoundException;
import org.jspiders.CourierAndLogisticsTrackingSystem.exceptions.NoRecordFoundException;
import org.jspiders.CourierAndLogisticsTrackingSystem.repositories.PackageEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class PackageEntityService {
	@Autowired
	private PackageEntityRepository packageEntityRepo;
	

	public ResponseStructure<List<PackageEntity>> getAllPackageEntitiesFromDatabase(){
		List<PackageEntity> li=packageEntityRepo.findAll();
		if(li.size()==0) {
			throw new NoRecordFoundException("No records exist for package entity in database. .");
		}
		ResponseStructure<List<PackageEntity>> res = new ResponseStructure<>();
		res.setData(li);
		res.setMessage(li.size()+" records found in database.");
		res.setStatusCode(HttpStatus.FOUND.value());
		
		return res;
	}
	 
	public ResponseStructure<PackageEntity> getPackageEntityByIdFromDatabase(int id){
		Optional<PackageEntity> opt = packageEntityRepo.findById(id);
		if(opt.isEmpty()) {
			throw new IdNotFoundException("Record with package id : "+id+" not exist in database . .");
		}
		ResponseStructure<PackageEntity> res = new ResponseStructure<>();
		res.setData(opt.get());
		res.setMessage("Record fetched successfully from database.");
		res.setStatusCode(HttpStatus.FOUND.value());
		
		return res;
		
	}
	
	public  ResponseStructure<PackageEntity> updatePackageEntityInDatabase(PackageEntity p){
		if(p.getId()==null) {
			throw new IdNotFoundException("Id must be passed to upade package entity record");
		}
		Optional<PackageEntity> opt=packageEntityRepo.findById(p.getId());
		if(opt.isEmpty()) {
			throw new NoRecordFoundException("Record not found for package id : "+p.getId()+" in database . .");
		}
		ResponseStructure<PackageEntity> res = new ResponseStructure<>();
		res.setData(packageEntityRepo.save(p));
		res.setMessage("Record updated successfully in database.");
		res.setStatusCode(HttpStatus.OK.value());
		
		return res;

	}
	
	public ResponseStructure<PackageEntity> getPackageEntityByShipmentFromDatabase(int shipmentId){
		Optional<PackageEntity> opt = packageEntityRepo.findPackageEntityByShipmentId(shipmentId);
		if(opt.isEmpty()) {
			throw new IdNotFoundException("Package Record with shipment id : "+shipmentId+" not exist in database . .");
		}
		ResponseStructure<PackageEntity> res = new ResponseStructure<>();
		res.setData(opt.get());
		res.setMessage("Record fetched successfully from database.");
		res.setStatusCode(HttpStatus.FOUND.value());
		
		return res;
		
	}
}
