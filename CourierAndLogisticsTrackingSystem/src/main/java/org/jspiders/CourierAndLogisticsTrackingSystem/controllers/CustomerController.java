package org.jspiders.CourierAndLogisticsTrackingSystem.controllers;

import java.util.List;

import org.jspiders.CourierAndLogisticsTrackingSystem.dto.CustomerDTO;
import org.jspiders.CourierAndLogisticsTrackingSystem.dto.ResponseStructure;
import org.jspiders.CourierAndLogisticsTrackingSystem.entities.Customer;
import org.jspiders.CourierAndLogisticsTrackingSystem.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
@RequestMapping("/customer")
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	@GetMapping("/test")
	public String testAPI() {
		return "Welcome to app . . ";
	}

	@PostMapping("/add")
	public ResponseEntity<ResponseStructure<CustomerDTO>> createCustomer(@RequestBody Customer cust){
		return new ResponseEntity<ResponseStructure<CustomerDTO>>(customerService.createCustomerInDatabase(cust), HttpStatus.CREATED);
	}
	
	@GetMapping("/")
	public ResponseEntity<ResponseStructure<List<CustomerDTO>>> getAllCustomers(){
		return new ResponseEntity<ResponseStructure<List<CustomerDTO>>>(customerService.getAllCustomersFromDatabase(), HttpStatus.FOUND);
	}
	
	@GetMapping("/id/{id}")
	public ResponseEntity<ResponseStructure<CustomerDTO>> getCustomerById(@PathVariable int id){
		return new ResponseEntity<ResponseStructure<CustomerDTO>>(customerService.getCustomerByIdFromDatabase(id), HttpStatus.FOUND);
	}
	
	@GetMapping("/email/{email}")
	public ResponseEntity<ResponseStructure<CustomerDTO>> getCustomerByEmail(@PathVariable String email){
		return new ResponseEntity<ResponseStructure<CustomerDTO>>(customerService.getCustomerByEmailFromDatabase(email), HttpStatus.FOUND);
	}
	
	@GetMapping("/contact/{contact}")
	public ResponseEntity<ResponseStructure<CustomerDTO>> getCustomerByContact(@PathVariable long contact){
		return new ResponseEntity<ResponseStructure<CustomerDTO>>(customerService.getCustomerByContactFromDatabase(contact), HttpStatus.FOUND);
	}
	
	@PatchMapping("/update")
	public ResponseEntity<ResponseStructure<CustomerDTO>> updateCustomerDetails(@RequestBody Customer cust){
		return new ResponseEntity<ResponseStructure<CustomerDTO>>(customerService.updateCustomerInDatabase(cust), HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseStructure<CustomerDTO>> deleteCustomerById(@PathVariable int id){
		return new ResponseEntity<ResponseStructure<CustomerDTO>>(customerService.deleteCustomerFromDatabaseById(id), HttpStatus.OK);
	}
	
	@GetMapping("/{pageNum}/{pageSize}/{field}")
	public ResponseEntity<ResponseStructure<Page<CustomerDTO>>> getCustomerByPagination(@PathVariable int pageNum ,@PathVariable int pageSize ,@PathVariable String field){
		return new ResponseEntity<ResponseStructure<Page<CustomerDTO>>>(customerService.getCustomerByPaginationAndSorting(pageNum , pageSize,field), HttpStatus.FOUND);
	}
	
	
}
