package org.jspiders.CourierAndLogisticsTrackingSystem.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.query.Page;
import org.jspiders.CourierAndLogisticsTrackingSystem.dto.CustomerDTO;
import org.jspiders.CourierAndLogisticsTrackingSystem.dto.ResponseStructure;
import org.jspiders.CourierAndLogisticsTrackingSystem.dto.ShipmentStatusType;
import org.jspiders.CourierAndLogisticsTrackingSystem.entities.Customer;
import org.jspiders.CourierAndLogisticsTrackingSystem.entities.Shipment;
import org.jspiders.CourierAndLogisticsTrackingSystem.exceptions.IdNotFoundException;
import org.jspiders.CourierAndLogisticsTrackingSystem.exceptions.InvalidContactNumberException;
import org.jspiders.CourierAndLogisticsTrackingSystem.exceptions.NoRecordFoundException;
import org.jspiders.CourierAndLogisticsTrackingSystem.repositories.CustomerRepository;
import org.jspiders.CourierAndLogisticsTrackingSystem.repositories.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
	
	@Autowired
	private CustomerRepository customerRepo;
	
	@Autowired
	private ShipmentRepository shipmentRepo;
	
	public static CustomerDTO converCustomerToCustomerDTO(Customer c) {
		CustomerDTO cd=new CustomerDTO();
		cd.setAddress(c.getAddress());
		cd.setContact(c.getContact());;
		cd.setCustomerEmail(c.getCustomerEmail());
		cd.setCustomerName(c.getCustomerName());
		cd.setId(c.getId());
		return cd;
	}
	
	public ResponseStructure<CustomerDTO> createCustomerInDatabase(Customer cust){
		if(String.valueOf(cust.getContact()).length()!=10) {
			throw new InvalidContactNumberException("Invalid Contact Number. Contact number must be 10 digits . .");
		}
		Customer savedCustomer = customerRepo.save(cust);
		ResponseStructure<CustomerDTO> res = new ResponseStructure<>();
		res.setData(converCustomerToCustomerDTO(savedCustomer));
		res.setMessage("1 record inserted into Customer table . .");
		res.setStatusCode(HttpStatus.CREATED.value());
		return res;
	}
	
	public ResponseStructure<List<CustomerDTO>> getAllCustomersFromDatabase(){
		List<Customer> li = customerRepo.findAll();
		List<CustomerDTO> dtoList = new ArrayList<>();
		for(Customer c:li) {
			dtoList.add(converCustomerToCustomerDTO(c));
		}
		ResponseStructure<List<CustomerDTO>> res = new ResponseStructure<>();
		res.setData(dtoList);
		res.setMessage(dtoList.size()+" records found in Customer table . .");
		res.setStatusCode(HttpStatus.FOUND.value());
		return res;
	}
	
	public ResponseStructure<CustomerDTO> getCustomerByIdFromDatabase(int id){
		Optional<Customer> opt = customerRepo.findById(id);
		if(opt.isEmpty()) {
			throw new IdNotFoundException("Record with id : "+id+", not exist in database . .");
		}
		ResponseStructure<CustomerDTO> res = new ResponseStructure<>();
		res.setData(converCustomerToCustomerDTO(opt.get()));
		res.setMessage("Record found in Customer table . .");
		res.setStatusCode(HttpStatus.FOUND.value());
		return res;
	}
	
	public ResponseStructure<CustomerDTO> getCustomerByEmailFromDatabase(String customerEmail){
		Optional<Customer> opt = customerRepo.findCustomerWithEmail(customerEmail);
		if(opt.isEmpty()) {
			throw new NoRecordFoundException("Record with email : "+customerEmail+", not exist in database . .");
		}
		ResponseStructure<CustomerDTO> res = new ResponseStructure<>();
		res.setData(converCustomerToCustomerDTO(opt.get()));
		res.setMessage("Record found in Customer table . .");
		res.setStatusCode(HttpStatus.FOUND.value());
		return res;
	}
	
	public ResponseStructure<CustomerDTO> getCustomerByContactFromDatabase(long contact){
		Optional<Customer> opt = customerRepo.findCustomerWithContact(contact);
		
		if(opt.isEmpty()) {
			throw new NoRecordFoundException("Record with contact : "+contact+", not exist in database . .");
		}
		ResponseStructure<CustomerDTO> res = new ResponseStructure<>();
		res.setData(converCustomerToCustomerDTO(opt.get()));
		res.setMessage("Record found in Customer table . .");
		res.setStatusCode(HttpStatus.FOUND.value());
		return res;
	}
	
	public ResponseStructure<CustomerDTO> updateCustomerInDatabase(Customer cust){
		if(cust.getId()==null) {
			throw new IdNotFoundException("To update a record , id must be passed . .");
		}
		Optional<Customer> opt=customerRepo.findById(cust.getId());
		if(opt.isEmpty()) {
			throw new NoRecordFoundException("No record exist for id : "+cust.getId()+" that you have requested .");
		}
		Customer fetchedCustomer = opt.get();
		cust.setUserCredentials(fetchedCustomer.getUserCredentials());
		Customer updatedCustomer=customerRepo.save(cust);
		ResponseStructure<CustomerDTO> res = new ResponseStructure<>();
		res.setData(converCustomerToCustomerDTO(updatedCustomer));
		res.setMessage("Record updated successfully for customer id : "+updatedCustomer.getId());
		res.setStatusCode(HttpStatus.OK.value());
		return res;
	}
	
	public ResponseStructure<CustomerDTO> deleteCustomerFromDatabaseById(int id){
		Optional<Customer> opt=customerRepo.findById(id);
		if(opt.isEmpty()) {
			throw new IdNotFoundException("Record does not found with customer id : "+id+", in database");
		}
		List<Shipment> li=shipmentRepo.findShipmentByCustomerId(id);
		boolean isAllDelivered=true;
		if(li.size()!=0) {
			for(Shipment s:li) {
				if(s.getShipmentStatus() != ShipmentStatusType.DELIVERED) {
					isAllDelivered = false;
				}
			}	
		}
		if(isAllDelivered==false) {
			throw new IdNotFoundException("a shipment is active for that customer id : "+id+", cann't be deleted ");
		}
		
		for(Shipment s:li) {
			s.setCustomer(null);
		}
		customerRepo.deleteById(id);
		
		ResponseStructure<CustomerDTO> res = new ResponseStructure<>();
		res.setData(null);
		res.setMessage("Record deleted successfully for customer id : "+id);
		res.setStatusCode(HttpStatus.OK.value());
		return res;
	}
	
	public ResponseStructure<org.springframework.data.domain.Page<CustomerDTO>> getCustomerByPaginationAndSorting(int pageNum , int pageSize,String field){
		org.springframework.data.domain.Page<Customer> pages = customerRepo.findAll(PageRequest.of(pageNum, pageSize,Sort.by(field).ascending())
	    );
		org.springframework.data.domain.Page<CustomerDTO> dtoPage = pages.map(c -> {
			CustomerDTO dto = new CustomerDTO();
	        dto.setId(c.getId());
	        dto.setCustomerName(c.getCustomerName());
	        dto.setCustomerEmail(c.getCustomerEmail());
	        dto.setContact(c.getContact());
	        dto.setAddress(c.getAddress());
	        return dto;
	    });

	    ResponseStructure<org.springframework.data.domain.Page<CustomerDTO>> res = new ResponseStructure<>();
	    res.setData(dtoPage);
	    res.setMessage("Page fetched successfully");
	    res.setStatusCode(HttpStatus.FOUND.value());

	    return res;
	}
	
}
