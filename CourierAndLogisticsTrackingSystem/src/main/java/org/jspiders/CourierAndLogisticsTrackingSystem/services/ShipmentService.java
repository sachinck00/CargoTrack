package org.jspiders.CourierAndLogisticsTrackingSystem.services;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jspiders.CourierAndLogisticsTrackingSystem.dto.PaymentStatusType;
import org.jspiders.CourierAndLogisticsTrackingSystem.dto.ResponseStructure;
import org.jspiders.CourierAndLogisticsTrackingSystem.dto.ShipmentDTO;
import org.jspiders.CourierAndLogisticsTrackingSystem.dto.ShipmentStatusType;
import org.jspiders.CourierAndLogisticsTrackingSystem.dto.TrackingStatusType;
import org.jspiders.CourierAndLogisticsTrackingSystem.entities.DeliveryAgent;
import org.jspiders.CourierAndLogisticsTrackingSystem.entities.Payment;
import org.jspiders.CourierAndLogisticsTrackingSystem.entities.Shipment;
import org.jspiders.CourierAndLogisticsTrackingSystem.entities.TrackingHistory;
import org.jspiders.CourierAndLogisticsTrackingSystem.entities.WareHouse;
import org.jspiders.CourierAndLogisticsTrackingSystem.exceptions.IdNotFoundException;
import org.jspiders.CourierAndLogisticsTrackingSystem.exceptions.InvalidInputException;
import org.jspiders.CourierAndLogisticsTrackingSystem.exceptions.NoRecordFoundException;
import org.jspiders.CourierAndLogisticsTrackingSystem.repositories.CustomerRepository;
import org.jspiders.CourierAndLogisticsTrackingSystem.repositories.DeliveryAgentRepository;
import org.jspiders.CourierAndLogisticsTrackingSystem.repositories.PaymentRepository;
import org.jspiders.CourierAndLogisticsTrackingSystem.repositories.ShipmentRepository;
import org.jspiders.CourierAndLogisticsTrackingSystem.repositories.TrackingHistoryRepository;
import org.jspiders.CourierAndLogisticsTrackingSystem.repositories.WareHouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ShipmentService {
	
	public static ShipmentDTO convertShipmentToShipmentDTO(Shipment s) {
		ShipmentDTO sdto=new ShipmentDTO();
	    sdto.setDeliveryDate(s.getDeliveryDate());
	    sdto.setDestination(s.getDestination());
	    sdto.setId(s.getId());
	    sdto.setDeliveryDate(s.getDeliveryDate());
	    sdto.setShipmentStatus(s.getShipmentStatus());
	    sdto.setSource(s.getSource());
	    sdto.setAmount(s.getPayment().getAmount());
	    return sdto;
	}
	
	@Autowired
	private ShipmentRepository shipmentRepo;
	
	@Autowired
	private DeliveryAgentRepository deliveryAgentRepo;
	
	@Autowired
	private WareHouseRepository wareHouseRepo;
	
	@Autowired
	private TrackingHistoryRepository trackingHistoryRepo;
	
	@Autowired
	private CustomerRepository cutomerRepo;
	
	public ResponseStructure<ShipmentDTO> createShipmentInDatabase(Shipment s) {
		if(s.getTrackingHistory()==null || s.getTrackingHistory().isEmpty()) {
			throw new InvalidInputException("Tracking History empty");
		}
		if(s.getCustomer()==null || cutomerRepo.findById(s.getCustomer().getId()).isEmpty()) {
			throw new InvalidInputException("Invalid Customer . . check customer");
		}
		if(s.getDeliveryAgent()==null || deliveryAgentRepo.findById(s.getDeliveryAgent().getId()).isEmpty()) {
			throw new InvalidInputException("Invalid DeliveryAgent . . check delivery agent.");
		}
		if(deliveryAgentRepo.findById(s.getDeliveryAgent().getId()).get().getAvailability()==false) {
			throw new InvalidInputException("Delivery agent with id : "+s.getDeliveryAgent().getId()+" is currently unavailable . ");
		}

		if(s.getWareHouse()==null || wareHouseRepo.findById(s.getWareHouse().getId()).isEmpty()) {
			throw new InvalidInputException("Invalid warehouse . . check warehouse");
		}
		if(s.getPayment()==null) {
			throw new InvalidInputException("payment empty");
		}
		if(s.getPackageEntity()==null) {
			throw new InvalidInputException("package entity  empty");
		}
				for(TrackingHistory t:s.getTrackingHistory()) {
			t.setShipment(s);
		}
		
		//fragile based price 100 extra
		if(s.getPackageEntity().getFragile()==true) {
			double currentPrice = s.getPayment().getAmount();
			s.getPayment().setAmount(currentPrice+100);
		}
		
		//weight based price  , for every kg , 10 extra
		if(s.getWeight()>1) {
			int w=(int)Math.ceil(s.getWeight());
			double totalAmount = s.getPayment().getAmount()+ w*10;
			s.getPayment().setAmount(totalAmount);
		}
		
		//distance based price for every 100km , 50 extra
		if(s.getDistance()>100) {
			double totalPrice = s.getPayment().getAmount()+(s.getDistance()/100)*50;
			s.getPayment().setAmount(totalPrice);
		}
		
	    Shipment savedShipment = shipmentRepo.save(s);
	    ShipmentDTO sdto=convertShipmentToShipmentDTO(savedShipment);
	  
	    ResponseStructure<ShipmentDTO> res = new ResponseStructure<>();
	    res.setData(sdto);
	    res.setMessage("Records added to database...");
	    res.setStatusCode(HttpStatus.CREATED.value());

	    return res;
	}
	
	public ResponseStructure<List<ShipmentDTO>> getAllShipmentsFromDatabase(){
		List<Shipment> li=shipmentRepo.findAll();
		ArrayList<ShipmentDTO> convertedArray = new ArrayList<>();
		for(Shipment s: li) {
			convertedArray.add(convertShipmentToShipmentDTO(s));
		}
		ResponseStructure<List<ShipmentDTO>> res = new ResponseStructure<List<ShipmentDTO>>();
		res.setData(convertedArray);
		res.setMessage(li.size()+" records found in database");
		res.setStatusCode(HttpStatus.FOUND.value());
		return res;
	}
	
	public ResponseStructure<ShipmentDTO> getShipmentByIdFromDatabase(int id){
		Optional<Shipment> opt=shipmentRepo.findById(id);
		if(opt.isEmpty()) {
			throw new IdNotFoundException("record doesn't exist with shipment id :"+id+" in database");
		}
		ResponseStructure<ShipmentDTO> res = new ResponseStructure<>();
	    res.setData(ShipmentService.convertShipmentToShipmentDTO(opt.get()));
	    res.setMessage("Record Found In database...");
	    res.setStatusCode(HttpStatus.FOUND.value());
	    return res;
	}
	
	public ResponseStructure<ShipmentDTO> getShipmentByITrackingNumberFromDatabase(int tn){
		Optional<Shipment> opt=shipmentRepo.findShipmentByTrackingNumber(tn);
		if(opt.isEmpty()) {
			throw new NoRecordFoundException("Record doesn't exist with shipment tracking number :"+tn+" in database");
		}
		ResponseStructure<ShipmentDTO> res = new ResponseStructure<>();
	    res.setData(ShipmentService.convertShipmentToShipmentDTO(opt.get()));
	    res.setMessage("Record Found In database...");
	    res.setStatusCode(HttpStatus.FOUND.value());
	    return res;
	}
	
	public ResponseStructure<ShipmentDTO> updateDeliveryAgentForShipmentInDatabase(Shipment s){
		if(s.getId()==null) {
			throw new IdNotFoundException("Shipment Id must be passed to assign delivery  agent for shipment . ");
		}
		if(s.getDeliveryAgent().getId()==null) {
			throw new IdNotFoundException("delivery agent Id must be passed to update in shipment details . ");
		}
		Optional<Shipment> opt=shipmentRepo.findById(s.getId());
		if(opt.isEmpty()) {
			throw new NoRecordFoundException("Record doesn't exist with shipment id :"+s.getId()+" in database");
		}
		Shipment fetchedShipment = opt.get();
		
		Optional<DeliveryAgent> opt2 = deliveryAgentRepo.findById(s.getDeliveryAgent().getId());
		if(opt2.isEmpty()) {
			throw new NoRecordFoundException("Record doesn't exist with Delivery agent id :"+s.getDeliveryAgent().getId()+" in database");
		}
		DeliveryAgent fetchedDeliveryAgent = opt2.get();
		if(fetchedDeliveryAgent.getAvailability()==false) {
			throw new InvalidInputException("Delivery agent with id:"+opt2.get().getId()+" is currently unavialbale . ");
		}
		fetchedShipment.setDeliveryAgent(fetchedDeliveryAgent);
		
		ResponseStructure<ShipmentDTO> res = new ResponseStructure<>();
	    res.setData(convertShipmentToShipmentDTO(shipmentRepo.save(fetchedShipment)));
	    res.setMessage("Record updated In database...");
	    res.setStatusCode(HttpStatus.OK.value());
	    return res;
	}
	
	public ResponseStructure<ShipmentDTO> updateWareHouseForShipmentInDatabase(Shipment s){
		if(s.getId()==null) {
			throw new IdNotFoundException("Id must be passed to update shipment details . ");
		}
		Optional<Shipment> opt1 = shipmentRepo.findById(s.getId());
		if(opt1.isEmpty()) {
			throw new NoRecordFoundException("Record doesn't exist with shipment id :"+s.getId()+" in database");
		}
		
		Shipment fetchedShipment = opt1.get();
		
		if(s.getWareHouse().getId()==null) {
			throw new IdNotFoundException("Warehouse Id must be passed to update in shipment. ");
		}
		Optional<WareHouse> opt2=wareHouseRepo.findById(s.getWareHouse().getId());
		if(opt2.isEmpty()) {
			throw new NoRecordFoundException("Record doesn't exist with WareHouse id :"+s.getWareHouse().getId()+" in database");
		}
		
		WareHouse w=opt2.get();
		
		fetchedShipment.setWareHouse(w);
		
		ResponseStructure<ShipmentDTO> res = new ResponseStructure<>();
	    res.setData(convertShipmentToShipmentDTO(shipmentRepo.save(fetchedShipment)));
	    res.setMessage("Record updated In database...");
	    res.setStatusCode(HttpStatus.OK.value());
	    return res;
		
	}
	
	public ResponseStructure<ShipmentDTO> updateShipmentStatusInDatabase(Shipment s){
		if(s.getId()==null) {
			throw new IdNotFoundException("Id must be passed to update shipment details . ");
		}
		if(s.getShipmentStatus()==null) {
			throw new InvalidInputException("shipment statis must be passes along with shipment id");
		}
		if(s.getTrackingHistory()==null || s.getTrackingHistory().size()==0) {
			throw new InvalidInputException("shipment tracking history must be passes along with shipment id and status");
		}
		Optional<Shipment> opt=shipmentRepo.findById(s.getId());
		if(opt.isEmpty()) {
			throw new NoRecordFoundException("Record doesn't exist with shipment id :"+s.getId()+" in database");
		}
		Shipment fetchedShipment = opt.get();
		for(TrackingHistory th:s.getTrackingHistory()) {
			th.setShipment(fetchedShipment);
		}
		fetchedShipment.setShipmentStatus(s.getShipmentStatus());
		fetchedShipment.getTrackingHistory().addAll(s.getTrackingHistory());
		fetchedShipment.getPayment().setPaymentStatus(s.getPayment().getPaymentStatus());
		ResponseStructure<ShipmentDTO> res = new ResponseStructure<>();
	    res.setData(convertShipmentToShipmentDTO(shipmentRepo.save(fetchedShipment)));
	    res.setMessage("Record updated In database...");
	    res.setStatusCode(HttpStatus.OK.value());
	    return res;
		
	}
	
	
	public ResponseStructure<ShipmentDTO> deleteShipmentById(int id){
		if(shipmentRepo.findById(id).isEmpty()) {
			throw new InvalidInputException("shipment with id : " + id + " doesnt exist in database .");
		}
	    List<TrackingHistory> li= trackingHistoryRepo.findByShipmentId(id);
	    if(li.size()!=0) {
	    	for(TrackingHistory th:li) {
	    		th.setShipment(null);
	    	}
	    }
	    shipmentRepo.deleteById(id);
	    ResponseStructure<ShipmentDTO> res = new ResponseStructure<>();
	    res.setData(null);
	    res.setMessage("Record with shipment id :"+id+", deleted from database...");
	    res.setStatusCode(HttpStatus.OK.value());
	    return res;
	} 
	
	
	public ResponseStructure<List<ShipmentDTO>> getAllShipmentsOfCustomerFromDatabase(int customerId){
		List<Shipment> li=shipmentRepo.findShipmentByCustomerId(customerId);
		if(li.size()==0) {
			throw new NoRecordFoundException("No shipments exist for customer id : "+ customerId+" in database .");
		}
		ArrayList<ShipmentDTO> convertedArray = new ArrayList<>();
		for(Shipment s: li) {
			convertedArray.add(convertShipmentToShipmentDTO(s));
		}
		ResponseStructure<List<ShipmentDTO>> res = new ResponseStructure<List<ShipmentDTO>>();
		res.setData(convertedArray);
		res.setMessage(li.size()+" records found in database");
		res.setStatusCode(HttpStatus.FOUND.value());
		return res;
	}
	
	public ResponseStructure<List<ShipmentDTO>> getAllShipmentsOfWareHouseFromDatabase(int wareHouseId){
		List<Shipment> li=shipmentRepo.findShipmentsByWareHouseId(wareHouseId);
		if(li.size()==0) {
			throw new NoRecordFoundException("No shipments exist for warehouse id : "+ wareHouseId+" in database .");
		}
		ArrayList<ShipmentDTO> convertedArray = new ArrayList<>();
		for(Shipment s: li) {
			convertedArray.add(convertShipmentToShipmentDTO(s));
		}
		ResponseStructure<List<ShipmentDTO>> res = new ResponseStructure<List<ShipmentDTO>>();
		res.setData(convertedArray);
		res.setMessage(li.size()+" records found in database");
		res.setStatusCode(HttpStatus.FOUND.value());
		return res;
	}
	
	public ResponseStructure<List<ShipmentDTO>> getAllShipmentsOfDeliveryAgentFromDatabase(int id){
		List<Shipment> li=shipmentRepo.findShipmentByDeliveryAgentId(id);
		if(li.size()==0) {
			throw new NoRecordFoundException("No shipments exist for Delivery agent id : "+ id+" in database .");
		}
		ArrayList<ShipmentDTO> convertedArray = new ArrayList<>();
		for(Shipment s: li) {
			convertedArray.add(convertShipmentToShipmentDTO(s));
		}
		ResponseStructure<List<ShipmentDTO>> res = new ResponseStructure<List<ShipmentDTO>>();
		res.setData(convertedArray);
		res.setMessage(li.size()+" records found in database");
		res.setStatusCode(HttpStatus.FOUND.value());
		return res;
	}
	
	public ResponseStructure<List<ShipmentDTO>> getShipmentsBySourceAndDestinationFromDatabase(String source , String dest){
		List<Shipment> li=shipmentRepo.findShipmentsBySourceAndDestination(source , dest);
		if(li.size()==0) {
			throw new NoRecordFoundException("No shipments exist from source : "+ source +" to "+dest+" in database .");
		}
		ArrayList<ShipmentDTO> convertedArray = new ArrayList<>();
		for(Shipment s: li) {
			convertedArray.add(convertShipmentToShipmentDTO(s));
		}
		ResponseStructure<List<ShipmentDTO>> res = new ResponseStructure<List<ShipmentDTO>>();
		res.setData(convertedArray);
		res.setMessage(li.size()+" records found in database");
		res.setStatusCode(HttpStatus.FOUND.value());
		return res;
	}
	
	public ResponseStructure<List<ShipmentDTO>> getShipmentsByDeliveryDateFromDatabase(LocalDate deliveryDate){
		List<Shipment> li=shipmentRepo.findShipmentsByDeliveryDate(deliveryDate);
		if(li.size()==0) {
			throw new NoRecordFoundException("No shipments exist for delivery date : "+deliveryDate+" in database .");
		}
		ArrayList<ShipmentDTO> convertedArray = new ArrayList<>();
		for(Shipment s: li) {
			convertedArray.add(convertShipmentToShipmentDTO(s));
		}
		ResponseStructure<List<ShipmentDTO>> res = new ResponseStructure<List<ShipmentDTO>>();
		res.setData(convertedArray);
		res.setMessage(li.size()+" records found in database");
		res.setStatusCode(HttpStatus.FOUND.value());
		return res;
	}
	
	public ResponseStructure<Page<ShipmentDTO>> getShipmentsByPaginationAndSorting(
	        int pn, int ps, String field) {

	    Page<Shipment> pages = shipmentRepo.findAll(PageRequest.of(pn, ps, Sort.by(field).ascending()));

	    Page<ShipmentDTO> dtoPage = pages.map(s -> {
	        ShipmentDTO dto = new ShipmentDTO();
	        dto.setId(s.getId());
	        dto.setSource(s.getSource());
	        dto.setDestination(s.getDestination());
	        dto.setDeliveryDate(s.getDeliveryDate());
	        dto.setShipmentStatus(s.getShipmentStatus());
	        dto.setAmount(s.getPayment().getAmount());
	        return dto;
	    });

	    ResponseStructure<Page<ShipmentDTO>> res = new ResponseStructure<>();

	    res.setData(dtoPage);
	    res.setMessage(dtoPage.getNumberOfElements() + " records found");
	    res.setStatusCode(HttpStatus.FOUND.value());

	    return res;
	}
	
	
	
	
	
	

}
