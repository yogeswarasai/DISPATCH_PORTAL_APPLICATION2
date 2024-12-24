package com.iocl.Dispatch_Portal_Application.Controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iocl.Dispatch_Portal_Application.DTO.MstCourierDto;
import com.iocl.Dispatch_Portal_Application.Entity.MstCourier;
import com.iocl.Dispatch_Portal_Application.Repositaries.MstCourierRepository;
import com.iocl.Dispatch_Portal_Application.Security.JwtUtils;
import com.iocl.Dispatch_Portal_Application.ServiceLayer.MstCourierService;
import com.iocl.Dispatch_Portal_Application.composite_pk.MstCourierPK;
import com.iocl.Dispatch_Portal_Application.modal.StatusCodeModal;


@RestController
@RequestMapping("/api/couriers")
public class MstCourierController {

	
	@Autowired
    private MstCourierService mstCourierService;
	@Autowired
    private MstCourierRepository mstCourierrepositary;

	 @Autowired
	    private JwtUtils jwtUtils;
	 
	 @GetMapping("/courierCode/{courierCode}/exists")
	 public ResponseEntity<Boolean> checkCourierCodeExists(
	         @PathVariable String courierCode,HttpServletRequest request
	        ) {
		  String token = jwtUtils.getJwtFromCookies(request);

	        // Validate and extract information from the JWT token
	        String locCode = jwtUtils.getLocCodeFromJwtToken(token);
	     // Construct the composite primary key
	     MstCourierPK primaryKey = new MstCourierPK();
	     primaryKey.setCourierCode(courierCode);
	     primaryKey.setLocCode(locCode);

	     // Check if the record exists using the composite key
	     boolean exists = mstCourierrepositary.existsById(primaryKey);

	     return ResponseEntity.ok(exists);
	 }
	
	
 @PostMapping
 public ResponseEntity<?> createCourier(@RequestBody MstCourierDto courierDto, HttpServletRequest request) {
     return mstCourierService.createCourier(courierDto, request);
 }

 
 @GetMapping("/all")
 public ResponseEntity<List<MstCourier>> getAllCouriers(HttpServletRequest request) {
     List<MstCourier> couriers = mstCourierService.findAll(request);
     return ResponseEntity.ok(couriers);
 }

 // Update an existing Courier by courierCode
 @PutMapping("/{courierCode}")
 public ResponseEntity<StatusCodeModal> updateCourier(
         @PathVariable String courierCode, 
         @RequestBody MstCourierDto mstCourierdto, 
         HttpServletRequest request) {
     return mstCourierService.updateCourier(courierCode, mstCourierdto, request);
 }

 // Delete a Courier by courierCode
 @DeleteMapping("/{courierCode}")
 public ResponseEntity<StatusCodeModal> deleteCourier(@PathVariable String courierCode, HttpServletRequest request) {
     return mstCourierService.deleteCourier(courierCode, request);
 }

 // Retrieve a Courier by courierCode
 @GetMapping("/{courierCode}")
 public ResponseEntity<?> getCourierByCourierCode(@PathVariable String courierCode, HttpServletRequest request) {
     Optional<MstCourier> courier = mstCourierService.getCourierByCourierCode(courierCode, request);
     if (courier.isPresent()) {
         return ResponseEntity.ok(courier.get());
     } else {
         StatusCodeModal statusCodeModal = new StatusCodeModal();
         statusCodeModal.setStatus_code(HttpStatus.NOT_FOUND.value());
         statusCodeModal.setStatus("Courier not found.");
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(statusCodeModal);
     }
 }
 
 @GetMapping("/names")
 public ResponseEntity<List<String>> getCouriersByLocCode(HttpServletRequest request) {
     List<String> courierNames = mstCourierService.getCourierNamesByLocCode(request);
     return ResponseEntity.ok(courierNames);
 }
}