package org.jspiders.CourierAndLogisticsTrackingSystem.services;

import java.util.List;
import java.util.Optional;

import org.jspiders.CourierAndLogisticsTrackingSystem.dto.PaymentStatusType;
import org.jspiders.CourierAndLogisticsTrackingSystem.dto.ResponseStructure;
import org.jspiders.CourierAndLogisticsTrackingSystem.dto.ShipmentStatusType;
import org.jspiders.CourierAndLogisticsTrackingSystem.dto.TrackingStatusType;
import org.jspiders.CourierAndLogisticsTrackingSystem.entities.Payment;
import org.jspiders.CourierAndLogisticsTrackingSystem.entities.Shipment;
import org.jspiders.CourierAndLogisticsTrackingSystem.entities.TrackingHistory;
import org.jspiders.CourierAndLogisticsTrackingSystem.exceptions.IdNotFoundException;
import org.jspiders.CourierAndLogisticsTrackingSystem.exceptions.InvalidInputException;
import org.jspiders.CourierAndLogisticsTrackingSystem.exceptions.NoRecordFoundException;
import org.jspiders.CourierAndLogisticsTrackingSystem.repositories.PaymentRepository;
import org.jspiders.CourierAndLogisticsTrackingSystem.repositories.ShipmentRepository;
import org.jspiders.CourierAndLogisticsTrackingSystem.repositories.TrackingHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class TrackingHistoryService {
	@Autowired
	private TrackingHistoryRepository trackingHistoryRepo;

	public ResponseStructure<List<TrackingHistory>> getTrackingHistoryByTrackingNumberFromDatabase(int tn) {
		List<TrackingHistory> li = trackingHistoryRepo.getTrackingHistoryByTrackingNumber(tn);
		if (li.size() == 0) {
			throw new NoRecordFoundException(
					"No tracking history record exists for tracking number : " + tn + " in database");
		}
		ResponseStructure<List<TrackingHistory>> res = new ResponseStructure<>();
		res.setData(li);
		res.setMessage(li.size() + " Record fetched successfully . .");
		res.setStatusCode(HttpStatus.FOUND.value());
		return res;
	}

	public ResponseStructure<List<TrackingHistory>> getAllTrackingHistoryFromDatabase() {
		List<TrackingHistory> li = trackingHistoryRepo.findAll();
		if (li.size() == 0) {
			throw new NoRecordFoundException("No tracking history record exists in database");
		}
		ResponseStructure<List<TrackingHistory>> res = new ResponseStructure<>();
		res.setData(li);
		res.setMessage(li.size() + " Record fetched successfully . .");
		res.setStatusCode(HttpStatus.FOUND.value());
		return res;
	}

	public ResponseStructure<TrackingHistory> getTrackingHistoryByIdFromDatabase(int id) {
		Optional<TrackingHistory> opt = trackingHistoryRepo.findById(id);
		if (opt.isEmpty()) {
			throw new NoRecordFoundException("No tracking history record exists with id : " + id + "  in database");
		}
		ResponseStructure<TrackingHistory> res = new ResponseStructure<>();
		res.setData(opt.get());
		res.setMessage(" Record fetched successfully . .");
		res.setStatusCode(HttpStatus.FOUND.value());
		return res;
	}

	public ResponseStructure<List<TrackingHistory>> getTrackingHistoryByShipmentIdFromDatabase(int shipmentId) {
		List<TrackingHistory> li = trackingHistoryRepo.findByShipmentId(shipmentId);
		if (li.size() == 0) {
			throw new NoRecordFoundException(
					"No tracking history record exists for shipment id : " + shipmentId + "  in database");
		}
		ResponseStructure<List<TrackingHistory>> res = new ResponseStructure<>();
		res.setData(li);
		res.setMessage(" Record fetched successfully . .");
		res.setStatusCode(HttpStatus.FOUND.value());
		return res;
	}

	public ResponseStructure<List<TrackingHistory>> getTrackingHistoryByTrackingStatusFromDatabase(
			TrackingStatusType status) {
		List<TrackingHistory> li = trackingHistoryRepo.findTrackingHistoryByTrackingStatus(status);
		if (li.size() == 0) {
			throw new NoRecordFoundException(
					"No tracking history record exists for tracking status : " + status + "  in database");
		}
		ResponseStructure<List<TrackingHistory>> res = new ResponseStructure<>();
		res.setData(li);
		res.setMessage(" Record fetched successfully . .");
		res.setStatusCode(HttpStatus.FOUND.value());
		return res;
	}

	public ResponseStructure<TrackingHistory> updateTrakingHistoryInDatabase(TrackingHistory th) {
		if (th.getId() == null) {
			throw new IdNotFoundException("TrackingHistory id must be passed to update the detatils . ");
		}
		Optional<TrackingHistory> opt = trackingHistoryRepo.findById(th.getId());
		if (opt.isEmpty()) {
			throw new NoRecordFoundException(
					"No record exist with tracking history id : " + th.getId() + " in database . .");
		}
		TrackingHistory fetched = opt.get();
		fetched.setLocation(th.getLocation());
		fetched.setRemarks(th.getRemarks());
		fetched.setTrackingStatus(th.getTrackingStatus());
		ResponseStructure<TrackingHistory> res = new ResponseStructure<>();
		res.setData(trackingHistoryRepo.save(fetched));
		res.setMessage("Record updates successfully . .");
		res.setStatusCode(HttpStatus.OK.value());
		return res;
	}

}
