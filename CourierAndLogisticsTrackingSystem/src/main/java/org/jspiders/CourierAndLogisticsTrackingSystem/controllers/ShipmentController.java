package org.jspiders.CourierAndLogisticsTrackingSystem.controllers;

import java.time.LocalDate;
import java.util.List;

import org.jspiders.CourierAndLogisticsTrackingSystem.dto.ResponseStructure;
import org.jspiders.CourierAndLogisticsTrackingSystem.dto.ShipmentDTO;
import org.jspiders.CourierAndLogisticsTrackingSystem.entities.Shipment;
import org.jspiders.CourierAndLogisticsTrackingSystem.services.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shipment")
public class ShipmentController {
	@Autowired
	private ShipmentService shipmentService;
	
	@PostMapping("/add")
	public ResponseEntity<ResponseStructure<ShipmentDTO>> createShipment(@RequestBody Shipment s){
		return new ResponseEntity<ResponseStructure<ShipmentDTO>>(shipmentService.createShipmentInDatabase(s),HttpStatus.CREATED);
	}
	
	@GetMapping("/")
	public ResponseEntity<ResponseStructure<List<ShipmentDTO>>> getShipments(){
		return new ResponseEntity<ResponseStructure<List<ShipmentDTO>>>(shipmentService.getAllShipmentsFromDatabase(),HttpStatus.FOUND);
	}
	
	@GetMapping("/id/{id}")
	public ResponseEntity<ResponseStructure<ShipmentDTO>> getShipmentById(@PathVariable("id") int shipmentId){
		return new ResponseEntity<ResponseStructure<ShipmentDTO>>(shipmentService.getShipmentByIdFromDatabase(shipmentId),HttpStatus.FOUND);
	}
	
	@GetMapping("/trackingNumber/{trackingNumber}")
	public ResponseEntity<ResponseStructure<ShipmentDTO>> getShipmentByTrackingNumber(@PathVariable("trackingNumber") int tn){
		return new ResponseEntity<ResponseStructure<ShipmentDTO>>(shipmentService.getShipmentByITrackingNumberFromDatabase(tn),HttpStatus.FOUND);
	}
	
	@PatchMapping("/updateDeliveryAgent")
	public ResponseEntity<ResponseStructure<ShipmentDTO>> updateDeliveryAgentForShipment(@RequestBody Shipment s){
		return new ResponseEntity<ResponseStructure<ShipmentDTO>>(shipmentService.updateDeliveryAgentForShipmentInDatabase(s),HttpStatus.OK);
	} 
	
	@PatchMapping("/updateWareHouse")
	public ResponseEntity<ResponseStructure<ShipmentDTO>> updateWareHouseForShipment(@RequestBody Shipment s){
		return new ResponseEntity<ResponseStructure<ShipmentDTO>>(shipmentService.updateWareHouseForShipmentInDatabase(s),HttpStatus.OK);
	} 
	
	@DeleteMapping("/id/{id}")
	public ResponseEntity<ResponseStructure<ShipmentDTO>> deleteShipmentById(@PathVariable("id") int shipmentId){
		return new ResponseEntity<ResponseStructure<ShipmentDTO>>(shipmentService.deleteShipmentById(shipmentId),HttpStatus.OK);
	}
	
	@PatchMapping("/updateShipmentStatus")
	public ResponseEntity<ResponseStructure<ShipmentDTO>> updateShipmentStatusForShipment(@RequestBody Shipment s){
		return new ResponseEntity<ResponseStructure<ShipmentDTO>>(shipmentService.updateShipmentStatusInDatabase(s),HttpStatus.OK);
	}
	
	@GetMapping("/shipmentsOfCustomer/{id}")
	public ResponseEntity<ResponseStructure<List<ShipmentDTO>>> getShipmentsOfCustomerById(@PathVariable int id){
		return new ResponseEntity<ResponseStructure<List<ShipmentDTO>>>(shipmentService.getAllShipmentsOfCustomerFromDatabase(id),HttpStatus.FOUND);
	}
	
	@GetMapping("/shipmentsOfWareHouse/{id}")
	public ResponseEntity<ResponseStructure<List<ShipmentDTO>>> getShipmentsOfWareHouseById(@PathVariable int id){
		return new ResponseEntity<ResponseStructure<List<ShipmentDTO>>>(shipmentService.getAllShipmentsOfWareHouseFromDatabase(id),HttpStatus.FOUND);
	}
	
	@GetMapping("/shipmentsOfDeliveryAgent/{id}")
	public ResponseEntity<ResponseStructure<List<ShipmentDTO>>> getShipmentsOfDeliveryAgentById(@PathVariable int id){
		return new ResponseEntity<ResponseStructure<List<ShipmentDTO>>>(shipmentService.getAllShipmentsOfDeliveryAgentFromDatabase(id),HttpStatus.FOUND);
	}
	
	@GetMapping("/shipmentsBySourceAndDest/{source}/{dest}")
	public ResponseEntity<ResponseStructure<List<ShipmentDTO>>> getShipmentsBySourceAndDestination(@PathVariable String source , @PathVariable String dest){
		return new ResponseEntity<ResponseStructure<List<ShipmentDTO>>>(shipmentService.getShipmentsBySourceAndDestinationFromDatabase(source,dest),HttpStatus.FOUND);
	}
	
	@GetMapping("/shipmentsByDeliveryDate/{date}")
	public ResponseEntity<ResponseStructure<List<ShipmentDTO>>> getShipmentsByDeliveryDate(@PathVariable LocalDate date){
		return new ResponseEntity<ResponseStructure<List<ShipmentDTO>>>(shipmentService.getShipmentsByDeliveryDateFromDatabase(date),HttpStatus.FOUND);
	}
	
	@GetMapping("/{pn}/{ps}/{field}")
	public ResponseEntity<ResponseStructure<Page<ShipmentDTO>>> getShipmentsByPagination(@PathVariable int pn , @PathVariable int ps , @PathVariable String field){
		return new ResponseEntity<ResponseStructure<Page<ShipmentDTO>>>(shipmentService.getShipmentsByPaginationAndSorting(pn , ps , field),HttpStatus.FOUND);
	}
	
}
