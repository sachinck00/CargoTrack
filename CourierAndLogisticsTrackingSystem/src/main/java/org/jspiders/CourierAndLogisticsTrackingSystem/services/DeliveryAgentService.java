package org.jspiders.CourierAndLogisticsTrackingSystem.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.DoubleToLongFunction;

import org.jspiders.CourierAndLogisticsTrackingSystem.dto.DeliveryAgentDTO;
import org.jspiders.CourierAndLogisticsTrackingSystem.dto.ResponseStructure;
import org.jspiders.CourierAndLogisticsTrackingSystem.dto.ShipmentStatusType;
import org.jspiders.CourierAndLogisticsTrackingSystem.entities.Customer;
import org.jspiders.CourierAndLogisticsTrackingSystem.entities.DeliveryAgent;
import org.jspiders.CourierAndLogisticsTrackingSystem.entities.Shipment;
import org.jspiders.CourierAndLogisticsTrackingSystem.entities.UserCredentials;
import org.jspiders.CourierAndLogisticsTrackingSystem.exceptions.IdNotFoundException;
import org.jspiders.CourierAndLogisticsTrackingSystem.exceptions.InvalidContactNumberException;
import org.jspiders.CourierAndLogisticsTrackingSystem.exceptions.NoRecordFoundException;
import org.jspiders.CourierAndLogisticsTrackingSystem.repositories.DeliveryAgentRepository;
import org.jspiders.CourierAndLogisticsTrackingSystem.repositories.ShipmentRepository;
import org.jspiders.CourierAndLogisticsTrackingSystem.repositories.UserCredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class DeliveryAgentService {
	
	@Autowired
	private ShipmentRepository shipmentRepo;
	@Autowired
	private DeliveryAgentRepository deliveryAgentRepo;
	@Autowired
	private UserCredentialsRepository userCredentialsRepo;
	
	public static DeliveryAgentDTO convertToDto(DeliveryAgent d) {
		DeliveryAgentDTO dto=new DeliveryAgentDTO();
		dto.setAgentName(d.getAgentName());
		dto.setAvailability(d.getAvailability());
		dto.setContact(d.getContact());
		dto.setId(d.getId());
		dto.setRating(d.getRating());
		dto.setVehicleNumber(d.getVehicleNumber());
		return dto;
	}
	
	public ResponseStructure<DeliveryAgentDTO> createDeliveryAgentInDatabase(DeliveryAgent agent){
		if(String.valueOf(agent.getContact()).length()!=10) {
			throw new InvalidContactNumberException("Invalid Contact Number. Contact number must be 10 digits . .");
		}
		DeliveryAgent savedAgent = deliveryAgentRepo.save(agent);
		ResponseStructure<DeliveryAgentDTO> res = new ResponseStructure<>();
		res.setData(convertToDto(savedAgent));
		res.setMessage("1 recored inserted into DeliveryAgent table . .");
		res.setStatusCode(HttpStatus.CREATED.value());
		return res;
	}
	
	public ResponseStructure<List<DeliveryAgentDTO>> getAllDeliveryAgents(){
		List<DeliveryAgent> li=deliveryAgentRepo.findAll();
		if(li.size()==0) {
			throw new NoRecordFoundException("No records exists in delivery_agent table .");
		}
		List<DeliveryAgentDTO> dtoList=new ArrayList<>();
		for(DeliveryAgent d:li) {
			dtoList.add(convertToDto(d));
		}
		ResponseStructure<List<DeliveryAgentDTO>> res = new ResponseStructure<>();
		res.setData(dtoList);
		res.setMessage(dtoList.size()+" records found in Database . .");
		res.setStatusCode(HttpStatus.FOUND.value());
		return res;
	}
	
	public ResponseStructure<DeliveryAgentDTO> getDeliveryAgentById(int id){
		Optional<DeliveryAgent> opt=deliveryAgentRepo.findById(id);
		if(opt.isEmpty()) {
			throw new IdNotFoundException("No record found for Delivery agent id: " + id);
		}
		ResponseStructure<DeliveryAgentDTO> res = new ResponseStructure<>();
		res.setData(convertToDto(opt.get()));
		res.setMessage("record with id : "+id+" found in Database . .");
		res.setStatusCode(HttpStatus.FOUND.value());
		return res;
	}
	
	public ResponseStructure<DeliveryAgentDTO> getDeliveryAgentByVehicleNumber(String vn){
		Optional<DeliveryAgent> opt=deliveryAgentRepo.findAgentByVehicleNumber(vn);
		if(opt.isEmpty()) {
			throw new NoRecordFoundException("No record exist for Delivery agent vehicle number : " + vn);
		}
		ResponseStructure<DeliveryAgentDTO> res = new ResponseStructure<>();
		res.setData(convertToDto(opt.get()));
		res.setMessage("Record found with Vehicle number : "+vn+" in Database . .");
		res.setStatusCode(HttpStatus.FOUND.value());
		return res;
	}
	
	public ResponseStructure<DeliveryAgentDTO> getDeliveryAgentByContact(long contact){
		Optional<DeliveryAgent> opt=deliveryAgentRepo.findAgentByContact(contact);
		if(opt.isEmpty()) {
			throw new NoRecordFoundException("No record exist for Delivery agent contact : " + contact);
		}
		ResponseStructure<DeliveryAgentDTO> res = new ResponseStructure<>();
		res.setData(convertToDto(opt.get()));
		res.setMessage("Record found with contact number : "+contact+" in Database . .");
		res.setStatusCode(HttpStatus.FOUND.value());
		return res;
	}
	
	public ResponseStructure<List<DeliveryAgentDTO>> getDeliveryAgentsByRatingGreaterThan(double rating){
		List<DeliveryAgent> li=deliveryAgentRepo.findAgentsByRatingGreaterThan(rating);
		if(li.size()==0) {
			throw new NoRecordFoundException("No agent record found for rating greater than "+rating);
		}
		List<DeliveryAgentDTO> dtoList=new ArrayList<>();
		for(DeliveryAgent d:li) {
			dtoList.add(convertToDto(d));
		}
		
		ResponseStructure<List<DeliveryAgentDTO>> res = new ResponseStructure<>();
		res.setData(dtoList);
		res.setMessage(dtoList.size()+" Records found in Database . .");
		res.setStatusCode(HttpStatus.FOUND.value());
		return res;
	}
	
	public ResponseStructure<DeliveryAgentDTO> updateAgentInDatabase(DeliveryAgent da){
		if(da.getId()==null) {
			throw new IdNotFoundException("To update , id must be passed ");
		}
		Optional<DeliveryAgent> opt=deliveryAgentRepo.findById(da.getId());
		if(opt.isEmpty()) {
			throw new NoRecordFoundException("Record with Id :" +da.getId()+" not exist in database");
		}
		DeliveryAgent fetchedDeliveryAgent = opt.get();
		da.setUserCredentials(fetchedDeliveryAgent.getUserCredentials());
		
		DeliveryAgent updatedAgent = deliveryAgentRepo.save(da);
		ResponseStructure<DeliveryAgentDTO> res = new ResponseStructure<>();
		res.setData(convertToDto(updatedAgent));
		res.setMessage("Record updated for id : "+updatedAgent.getId()+" successfully in Database . .");
		res.setStatusCode(HttpStatus.OK.value());
		return res;
		
	}
	
	public ResponseStructure<DeliveryAgentDTO> deleteAgentFromDatabase(int id){
		Optional<DeliveryAgent> opt=deliveryAgentRepo.findById(id);
		if(opt.isEmpty()) {
			throw new NoRecordFoundException("No record exist with delivery agent id : "+id+" , in database.");
		}
		List<Shipment> li=shipmentRepo.findShipmentByDeliveryAgentId(id);
		
		boolean isAllDelivered=true;
		if(li.size()!=0) {
			for(Shipment s:li) {
				if(s.getShipmentStatus() != ShipmentStatusType.DELIVERED) {
					isAllDelivered = false;
				}
			}	
		}
		if(isAllDelivered==false) {
			throw new IdNotFoundException("a shipment is active for that delivery agent id : "+id+", cann't be deleted ");
		}
		
		for(Shipment s : li) {
			s.setDeliveryAgent(null);
		}
		shipmentRepo.saveAll(li);
		deliveryAgentRepo.deleteById(id);
		
		ResponseStructure<DeliveryAgentDTO> res = new ResponseStructure<>();
		res.setData(null);
		res.setMessage("Record deleted successfully for customer id : "+id);
		res.setStatusCode(HttpStatus.OK.value());
		return res;
	}
	
	public ResponseStructure<DeliveryAgentDTO> updateAvailability(int id, boolean status) {
	    Optional<DeliveryAgent> opt = deliveryAgentRepo.findById(id);
	    if(opt.isEmpty()) {
	    	throw new NoRecordFoundException("No record found for Delivery agent id: " + id);
	    }
	    
	    DeliveryAgent da=opt.get();
	    da.setAvailability(status);

	    DeliveryAgent updatedAgent = deliveryAgentRepo.save(da);

	    ResponseStructure<DeliveryAgentDTO> res = new ResponseStructure<>();
	    res.setData(convertToDto(updatedAgent));
	    res.setMessage("Agent Availability updated successfully");
	    res.setStatusCode(HttpStatus.OK.value());
	    return res;
	}
	
	public ResponseStructure<List<DeliveryAgentDTO>> getDeliveryAgentsInDescendingOrder(String field){
		List<DeliveryAgent> agents = deliveryAgentRepo.findAll(Sort.by(field).descending());

	    List<DeliveryAgentDTO> dtoList = agents.stream()
	            .map(a -> {
	                DeliveryAgentDTO dto = new DeliveryAgentDTO();
	                dto.setId(a.getId());
	                dto.setAgentName(a.getAgentName());
	                dto.setContact(a.getContact());
	                dto.setVehicleNumber(a.getVehicleNumber());
	                dto.setAvailability(a.getAvailability());
	                dto.setRating(a.getRating());
	                return dto;
	            })
	            .toList();

	    ResponseStructure<List<DeliveryAgentDTO>> res = new ResponseStructure<>();
	    res.setData(dtoList);
	    res.setMessage(dtoList.size() + " records sorted in descending order successfully . .");
	    res.setStatusCode(HttpStatus.OK.value());
	    return res;
	}
}
