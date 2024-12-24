package com.iocl.Dispatch_Portal_Application.ServiceLayer;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.iocl.Dispatch_Portal_Application.DTO.MstCourierDto;
import com.iocl.Dispatch_Portal_Application.Entity.MstCourier;
import com.iocl.Dispatch_Portal_Application.Repositaries.MstCourierRepository;
import com.iocl.Dispatch_Portal_Application.Security.JwtUtils;
import com.iocl.Dispatch_Portal_Application.composite_pk.MstCourierPK;
import com.iocl.Dispatch_Portal_Application.modal.StatusCodeModal;

@Service
public class MstCourierService {

	@Autowired
    private MstCourierRepository mstCourierRepository;
	 @Autowired
	    private JwtUtils jwtUtils;

	 public List<MstCourier> findAll(HttpServletRequest request) {
	      
	        String token = jwtUtils.getJwtFromCookies(request);
	        String locCode = jwtUtils.getLocCodeFromJwtToken(token);

	        return mstCourierRepository.findByLocCode(locCode);
	    }

    public ResponseEntity<?> createCourier(MstCourierDto courierDto, HttpServletRequest request) {
    	
    	try {
    		MstCourier courier=new MstCourier();
        	courier.setCourierCode(courierDto.getCourierCode());
        	courier.setCourierName(courierDto.getCourierName());
      
            String token = jwtUtils.getJwtFromCookies(request);

            // Extract locCode from token
            String locCode = jwtUtils.getLocCodeFromJwtToken(token);
           
            if (locCode == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid JWT token");
            }
            courier.setLocCode(locCode);  // Setting locCode from token

            MstCourierPK courierpk = new MstCourierPK();
            
            courierpk.setLocCode(locCode);
            courierpk.setCourierCode(courierDto.getCourierCode());
            
            
               Optional<MstCourier>          existCourier=mstCourierRepository.findById(courierpk);
            
            if (existCourier.isEmpty()) {
            	 MstCourier createdCourier = mstCourierRepository.save(courier);
                 StatusCodeModal statusCodeModal = new StatusCodeModal();
                 statusCodeModal.setStatus_code(HttpStatus.CREATED.value());
                 statusCodeModal.setStatus("Courier created successfully.");
                 return ResponseEntity.status(HttpStatus.CREATED).body(statusCodeModal);
            	
            }
            else {
                StatusCodeModal statusCodeModal = new StatusCodeModal();
                statusCodeModal.setStatus_code(HttpStatus.BAD_REQUEST.value());
                statusCodeModal.setStatus("Courier already exists: " + courier.getCourierCode());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCodeModal);
            }
			
		} catch (Exception e) {
			// TODO: handle exception
			
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Inter server erreor");
		}
		
    	
   

       
    }

    public ResponseEntity<StatusCodeModal> updateCourier(String courierCode, MstCourierDto mstCourierdto, HttpServletRequest request) {
      
    	try {
    		
    		String token = jwtUtils.getJwtFromCookies(request);
            String locCode = jwtUtils.getLocCodeFromJwtToken(token);

            // Create composite key CourierId
            MstCourierPK courierpk = new MstCourierPK();
            
            courierpk.setLocCode(locCode);
            courierpk.setCourierCode(courierCode);
            
            Optional<MstCourier> existingCourier = mstCourierRepository.findById(courierpk);
            StatusCodeModal statusCodeModal = new StatusCodeModal();

            if (!existingCourier.isPresent()) {
            
                statusCodeModal.setStatus_code(HttpStatus.NOT_FOUND.value());
                statusCodeModal.setStatus("Courier not found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(statusCodeModal);
            }
            else {
            	 MstCourier courierToUpdate = existingCourier.get();
            	 courierToUpdate.setCourierCode(courierCode);
            	 courierToUpdate.setLocCode(courierToUpdate.getLocCode()); 
            	 courierToUpdate.setCourierName(mstCourierdto.getCourierName());
            	 MstCourier updatedCourier = mstCourierRepository.save(courierToUpdate);
            	 statusCodeModal.setStatus_code(HttpStatus.OK.value());
                 statusCodeModal.setStatus("Courier updated successfully.");
                 return ResponseEntity.ok(statusCodeModal);
            }

			
		} catch (Exception e) {
			 StatusCodeModal statusCodeModal = new StatusCodeModal();
			statusCodeModal.setStatus_code(HttpStatus.INTERNAL_SERVER_ERROR.value());
            statusCodeModal.setStatus("Internal server error");
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(statusCodeModal);
		}
		
    	
    	
       

  
    }

    public ResponseEntity<StatusCodeModal> deleteCourier(String courierCode, HttpServletRequest request) {
    	
    	try {
    		
    		 String token = jwtUtils.getJwtFromCookies(request);
    	        String locCode = jwtUtils.getLocCodeFromJwtToken(token);

    	        // Create composite key CourierId
    	        MstCourierPK courierpk = new MstCourierPK();
    	        
    	        courierpk.setLocCode(locCode);
    	        courierpk.setCourierCode(courierCode);
    	        
    	        Optional<MstCourier> existingCourier = mstCourierRepository.findById(courierpk);
    	        StatusCodeModal statusCodeModal = new StatusCodeModal();

    	        if (existingCourier.isPresent()) {
    	            mstCourierRepository.deleteById(courierpk);
    	            statusCodeModal.setStatus_code(HttpStatus.OK.value());
    	            statusCodeModal.setStatus("Courier deleted successfully.");
    	            return ResponseEntity.ok(statusCodeModal);
    	        } else {
    	            statusCodeModal.setStatus_code(HttpStatus.NOT_FOUND.value());
    	            statusCodeModal.setStatus("Courier not found.");
    	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(statusCodeModal);
    	        }
			
		} catch (Exception e) {
			  StatusCodeModal statusCodeModal = new StatusCodeModal();
			// TODO: handle exception
			statusCodeModal.setStatus_code(HttpStatus.INTERNAL_SERVER_ERROR.value());
            statusCodeModal.setStatus("INTERNAL_SERVER_ERROR .");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(statusCodeModal);
		}
		
       
    }

    public Optional<MstCourier> getCourierByCourierCode(String courierCode, HttpServletRequest request) {
        String token = jwtUtils.getJwtFromCookies(request);
        String locCode = jwtUtils.getLocCodeFromJwtToken(token);

        // Create composite key CourierId
            MstCourierPK courierpk = new MstCourierPK();
        
        courierpk.setLocCode(locCode);
        courierpk.setCourierCode(courierCode);
        return mstCourierRepository.findById(courierpk);
    }

    
    public List<String> getCourierNamesByLocCode(HttpServletRequest request) {
        // Extract the JWT token from cookies or headers
        String token = jwtUtils.getJwtFromCookies(request);

        // Extract the locCode from the JWT token
        String locCode = jwtUtils.getLocCodeFromJwtToken(token);

        // Query couriers by locCode, passing the extracted locCode (String)
        List<MstCourier> couriers = mstCourierRepository.findByLocCode(locCode);

        // Return the list of courier names
        return couriers.stream()
                .map(MstCourier::getCourierName)
                .collect(Collectors.toList());
    }}

