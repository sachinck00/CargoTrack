package org.jspiders.CourierAndLogisticsTrackingSystem.services;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jspiders.CourierAndLogisticsTrackingSystem.dto.ResponseStructure;
import org.jspiders.CourierAndLogisticsTrackingSystem.dto.ShipmentStatusType;
import org.jspiders.CourierAndLogisticsTrackingSystem.dto.WareHouseDTO;
import org.jspiders.CourierAndLogisticsTrackingSystem.entities.Shipment;
import org.jspiders.CourierAndLogisticsTrackingSystem.entities.UserCredentials;
import org.jspiders.CourierAndLogisticsTrackingSystem.entities.WareHouse;
import org.jspiders.CourierAndLogisticsTrackingSystem.exceptions.IdNotFoundException;
import org.jspiders.CourierAndLogisticsTrackingSystem.exceptions.InvalidContactNumberException;
import org.jspiders.CourierAndLogisticsTrackingSystem.exceptions.InvalidInputException;
import org.jspiders.CourierAndLogisticsTrackingSystem.exceptions.NoRecordFoundException;
import org.jspiders.CourierAndLogisticsTrackingSystem.repositories.ShipmentRepository;
import org.jspiders.CourierAndLogisticsTrackingSystem.repositories.UserCredentialsRepository;
import org.jspiders.CourierAndLogisticsTrackingSystem.repositories.WareHouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class WareHouseService {

	@Autowired
	private WareHouseRepository wareHouseRepo;
	
	@Autowired
	private ShipmentRepository shipmentRepo;
	
	@Autowired
	private UserCredentialsRepository userCredentialsRepo;
	
	public static WareHouseDTO convertWareHouseToWareHouseDTO(WareHouse w) {
		WareHouseDTO dto=new WareHouseDTO();
		dto.setCapacity(w.getCapacity());
		dto.setContact(w.getContact());
		dto.setId(w.getId());
		dto.setLocation(w.getLocation());
		dto.setWareHouseName(w.getWareHouseName());
		return dto;
	}
	
	public ResponseStructure<WareHouseDTO> createWareHouseInDatabase(WareHouse house){
		if(String.valueOf(house.getContact()).length()!=10) {
			throw new InvalidContactNumberException("Invalid Contact Number. Contact number must be 10 digits . .");
		}
		WareHouse savedHouse = wareHouseRepo.save(house);
		ResponseStructure<WareHouseDTO> res = new ResponseStructure<>();
		res.setData(convertWareHouseToWareHouseDTO(savedHouse));
		res.setMessage("1 record inserted into ware house table");
		res.setStatusCode(HttpStatus.CREATED.value());
		return res;
		
	}
	
	public ResponseStructure<List<WareHouseDTO>> getAllWareHouses() {
		List<WareHouse> warehouses = wareHouseRepo.findAll();
		if (warehouses.isEmpty()) {
	        throw new NoRecordFoundException("No warehouses exist  in database . .");
	    }
		List<WareHouseDTO> dtoList=new ArrayList<>();
		for(WareHouse w: warehouses) {
			dtoList.add(convertWareHouseToWareHouseDTO(w));
		}
	    ResponseStructure<List<WareHouseDTO>> res =new ResponseStructure<>();
	    res.setData(dtoList);
	    res.setMessage(warehouses.size()+" Warehouses records fetched successfully");
	    res.setStatusCode(HttpStatus.FOUND.value());
	    return res;
	}
	
	public ResponseStructure<WareHouseDTO> getWareHouseById(int id) {
		Optional<WareHouse> opt =  wareHouseRepo.findById(id);
		if(opt.isEmpty()) {
			throw new IdNotFoundException("Record with ware house id : "+id+" not exist in database");
		}
	    WareHouse wareHouse = opt.get();
	    ResponseStructure<WareHouseDTO> res = new ResponseStructure<>();
	    res.setData(convertWareHouseToWareHouseDTO(wareHouse));
	    res.setMessage("Record with ware house id "+id+" found in databse");
	    res.setStatusCode(HttpStatus.FOUND.value());
	    return res;
	}
	
	public ResponseStructure<WareHouseDTO> getWareHouseByLocation(String loc) {
		Optional<WareHouse> opt =  wareHouseRepo.findWareHouseByLocation(loc);
		if(opt.isEmpty()) {
			throw new NoRecordFoundException("Record with ware house location : "+loc+" not exist in database");
		}
	    WareHouse wareHouse = opt.get();
	    ResponseStructure<WareHouseDTO> res = new ResponseStructure<>();
	    res.setData(convertWareHouseToWareHouseDTO(wareHouse));
	    res.setMessage("Record with ware house location : "+loc+" found in databse");
	    res.setStatusCode(HttpStatus.FOUND.value());
	    return res;
	}
	
	public ResponseStructure<List<WareHouseDTO>> getWareHousesByCapacityGreaterThan(int capacity) {
	    List<WareHouse> list = wareHouseRepo.findWareHousesByCapacityGreaterThan(capacity);
	    if (list.isEmpty()) {
	        throw new NoRecordFoundException("No warehouses exist with capacity greater than " + capacity+" in database . .");
	    }
	    List<WareHouseDTO> dtoList=new ArrayList<>();
		for(WareHouse w: list) {
			dtoList.add(convertWareHouseToWareHouseDTO(w));
		}
	    ResponseStructure<List<WareHouseDTO>> res = new ResponseStructure<>();
	    res.setData(dtoList);
	    res.setMessage(dtoList.size()+ " Warehouses fetched successfully");
	    res.setStatusCode(HttpStatus.FOUND.value());
	    return res;
	}
	
	
	public ResponseStructure<WareHouseDTO> updateWareHouse(WareHouse w){
		if(w.getId() == null) {
			throw new IdNotFoundException("Id must be pass to upadate a record");
		}
		Optional<WareHouse> opt = wareHouseRepo.findById(w.getId());
		if(opt.isEmpty()) {
			throw new NoRecordFoundException("No record exist with warehouse id : "+w.getId()+ " in database . .");
		}
		WareHouse fetchedWarehouse = opt.get();
		w.setUserCredentials(fetchedWarehouse.getUserCredentials());

		WareHouse updatedWareHouse = wareHouseRepo.save(w);
		ResponseStructure<WareHouseDTO> res = new ResponseStructure<>();
	    res.setData(convertWareHouseToWareHouseDTO(updatedWareHouse));
	    res.setMessage("Record updated in databse");
	    res.setStatusCode(HttpStatus.OK.value());
	    return res;
	}
	
	public ResponseStructure<WareHouseDTO> deleteWareHouse(int id){
		Optional<WareHouse> opt=wareHouseRepo.findById(id);
		if(opt.isEmpty()) {
			throw new NoRecordFoundException("No record fount with ware house id : " + id + " in database .");
		}
		List<Shipment> li = shipmentRepo.findShipmentsByWareHouseId(id);
		boolean isAllDelivered = true;
		if(li.size()!=0) {
			for(Shipment s:li) {
				if(s.getShipmentStatus()!=ShipmentStatusType.DELIVERED) {
					isAllDelivered = false;
				}
			}
		}
		if(isAllDelivered == false) {
			throw new InvalidInputException("some shipment are active in ware house , cannt be deleted");
		}
		
		for(Shipment s:li) {
			s.setWareHouse(null);
		}
		shipmentRepo.saveAll(li);
		
		wareHouseRepo.deleteById(id);
		ResponseStructure<WareHouseDTO> res = new ResponseStructure<>();
	    res.setData(null);
	    res.setMessage("Record with ware house id : "+id+" deleted from databse");
	    res.setStatusCode(HttpStatus.OK.value());
	    return res;
		
	}
}
