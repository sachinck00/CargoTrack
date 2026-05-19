package org.jspiders.CourierAndLogisticsTrackingSystem.controllers;

import java.util.List;

import org.jspiders.CourierAndLogisticsTrackingSystem.dto.DeliveryAgentDTO;
import org.jspiders.CourierAndLogisticsTrackingSystem.dto.ResponseStructure;
import org.jspiders.CourierAndLogisticsTrackingSystem.entities.DeliveryAgent;
import org.jspiders.CourierAndLogisticsTrackingSystem.services.DeliveryAgentService;
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
@RequestMapping("/deliveryAgent")
public class DeliveryAgentController {
	
	@Autowired
	private DeliveryAgentService deliveryAgentService;
	
	@PostMapping("/add")
	public ResponseEntity<ResponseStructure<DeliveryAgentDTO>> createDeliveryAgent(@RequestBody DeliveryAgent agent){
		return new ResponseEntity<ResponseStructure<DeliveryAgentDTO>>(deliveryAgentService.createDeliveryAgentInDatabase(agent), HttpStatus.CREATED);
	}

	@GetMapping("/")
	public ResponseEntity<ResponseStructure<List<DeliveryAgentDTO>>> getAllAgents(){
		return new ResponseEntity<ResponseStructure<List<DeliveryAgentDTO>>>(deliveryAgentService.getAllDeliveryAgents(),HttpStatus.FOUND);
	}
	
	@GetMapping("/id/{id}")
	public ResponseEntity<ResponseStructure<DeliveryAgentDTO>> getAgentById(@PathVariable int id){
		return new ResponseEntity<ResponseStructure<DeliveryAgentDTO>>(deliveryAgentService.getDeliveryAgentById(id),HttpStatus.FOUND);
	}
	
	@GetMapping("/vehicleNumber/{vehicleNumber}")
	public ResponseEntity<ResponseStructure<DeliveryAgentDTO>> getAgentByVehicleNumber(@PathVariable("vehicleNumber") String vn){
		return new ResponseEntity<ResponseStructure<DeliveryAgentDTO>>(deliveryAgentService.getDeliveryAgentByVehicleNumber(vn),HttpStatus.FOUND);
	}
	
	@GetMapping("/contact/{contact}")
	public ResponseEntity<ResponseStructure<DeliveryAgentDTO>> getAgentByContact(@PathVariable long contact){
		return new ResponseEntity<ResponseStructure<DeliveryAgentDTO>>(deliveryAgentService.getDeliveryAgentByContact(contact),HttpStatus.FOUND);
	}
	
	@GetMapping("/rating/{rating}")
	public ResponseEntity<ResponseStructure<List<DeliveryAgentDTO>>> getAgentByRatingGreaterThan(@PathVariable double rating){
		return new ResponseEntity<ResponseStructure<List<DeliveryAgentDTO>>>(deliveryAgentService.getDeliveryAgentsByRatingGreaterThan(rating),HttpStatus.FOUND);
	}
	
	@PatchMapping("/update")
	public ResponseEntity<ResponseStructure<DeliveryAgentDTO>> updateDeliveryAgent(@RequestBody DeliveryAgent da){
		return new ResponseEntity<ResponseStructure<DeliveryAgentDTO>>(deliveryAgentService.updateAgentInDatabase(da),HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseStructure<DeliveryAgentDTO>> deleteAgentById(@PathVariable int id){
		return new ResponseEntity<ResponseStructure<DeliveryAgentDTO>>(deliveryAgentService.deleteAgentFromDatabase(id),HttpStatus.FOUND);
	}
	@PatchMapping("/updateAgentAvailability/{id}/{status}")
	public ResponseEntity<ResponseStructure<DeliveryAgentDTO>> updateAvailability(@PathVariable int id, @PathVariable("status") boolean availabilityStatus) {
		return new ResponseEntity<ResponseStructure<DeliveryAgentDTO>>(deliveryAgentService.updateAvailability(id, availabilityStatus),HttpStatus.OK);
	}
	@GetMapping("/sortBy/{field}")
	public ResponseEntity<ResponseStructure<List<DeliveryAgentDTO>>> getDeliveryAgentsInSortedOrder(@PathVariable String field){
		return new ResponseEntity<ResponseStructure<List<DeliveryAgentDTO>>>(deliveryAgentService.getDeliveryAgentsInDescendingOrder(field), HttpStatus.FOUND);
	}
}
