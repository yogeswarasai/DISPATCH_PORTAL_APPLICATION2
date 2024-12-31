package com.iocl.Dispatch_Portal_Application.Controller;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iocl.Dispatch_Portal_Application.DTO.CourierContractDto;
import com.iocl.Dispatch_Portal_Application.DTO.MstCourierContractDto;
import com.iocl.Dispatch_Portal_Application.DTO.RatesAndDiscountsResponse;
import com.iocl.Dispatch_Portal_Application.Entity.MstCourierContract;
import com.iocl.Dispatch_Portal_Application.Entity.MstCourierContractDiscount;
import com.iocl.Dispatch_Portal_Application.Entity.MstCourierContractRate;
import com.iocl.Dispatch_Portal_Application.Repositaries.MstCourierContractDiscountRepository;
import com.iocl.Dispatch_Portal_Application.Repositaries.MstCourierContractRateRepository;
import com.iocl.Dispatch_Portal_Application.Repositaries.MstCourierContractRepository;
import com.iocl.Dispatch_Portal_Application.Security.JwtUtils;
import com.iocl.Dispatch_Portal_Application.ServiceLayer.MstCourierContractService;
import com.iocl.Dispatch_Portal_Application.composite_pk.CourierContractId;
import com.iocl.Dispatch_Portal_Application.modal.StatusCodeModal;

@RestController
@RequestMapping("/api/courier-contracts")
@CrossOrigin(origins = "http://localhost:4200")
public class MstCourierContractController {
	
	private MstCourierContractService mstCourierContractService;
	
	@Autowired
	private MstCourierContractDiscountRepository mstCourierContractDiscountRepository;
	@Autowired
	 private MstCourierContractRateRepository mstCourierContractRateRepository;
	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private MstCourierContractRepository mstCourierContractRepository;
	    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MstCourierContractController.class);

	
	   public MstCourierContractController(MstCourierContractService mstCourierContractService){
	        this.mstCourierContractService = mstCourierContractService;
	    }
  
	   @GetMapping("/courierContNo/{courierContNo}/exists")
		 public ResponseEntity<Boolean> checkCourierCodeExists(
		         @PathVariable String courierContNo,HttpServletRequest request
		        ) {
			  String token = jwtUtils.getJwtFromCookies(request);

		        // Validate and extract information from the JWT token
		        String locCode = jwtUtils.getLocCodeFromJwtToken(token);
		     // Construct the composite primary key
		        CourierContractId primaryKey = new CourierContractId();
		     primaryKey.setCourierContNo(courierContNo);
		     primaryKey.setLocCode(locCode);

		     // Check if the record exists using the composite key
		     boolean exists = mstCourierContractRepository.existsById(primaryKey);

		     return ResponseEntity.ok(exists);
		 }
	   
	   @PostMapping
	    public ResponseEntity<?> createCourierContract(@RequestBody CourierContractDto courierContractDto, HttpServletRequest request) {
	        return mstCourierContractService.createCourierContract(courierContractDto, request);
	    }

	    @PutMapping("/{courierContNo}")
	    public ResponseEntity<StatusCodeModal> updateCourierContract(@PathVariable String courierContNo, @RequestBody MstCourierContract mstCourierContract, HttpServletRequest request) {
	        return mstCourierContractService.updateCourierContract(courierContNo, mstCourierContract, request);
	    }

	    @DeleteMapping("/{courierContNo}")
	    public ResponseEntity<StatusCodeModal> deleteCourierContract(@PathVariable String courierContNo, HttpServletRequest request) {
	        return mstCourierContractService.deleteCourierContract(courierContNo, request);
	    }

	    @GetMapping
	    public ResponseEntity<List<MstCourierContract>> getCourierContractByContNo(HttpServletRequest request) {
	        List<MstCourierContract> contract = mstCourierContractService.getCourierContractByContNo(request);
	        
	        
	        return ResponseEntity.ok(contract);
	    }
	    
	    @GetMapping("/{courierCode}")
	    public ResponseEntity<List<MstCourierContract>> getContractsBasedOnCourierCode(
	            @PathVariable String courierCode, HttpServletRequest request) {
	        return mstCourierContractService.getContractsBasedOnCourierCodeAndLocCode(courierCode, request);
	    }
	    

	    
	    @GetMapping("/rates/discounts/{courierContNo}")
	    public ResponseEntity<?> getRatesAndDiscountsByCourierContNo(@PathVariable String courierContNo) {
	        try {
	            List<MstCourierContractDiscount> discounts = mstCourierContractDiscountRepository.findByCourierContNo(courierContNo);
	            List<MstCourierContractRate> rates = mstCourierContractRateRepository.findByCourierContNo(courierContNo);
	            
	            if (discounts.isEmpty() && rates.isEmpty()) {
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No rates or discounts found for courierContNo: " + courierContNo);
	            }
	            
	            RatesAndDiscountsResponse response = new RatesAndDiscountsResponse(discounts, rates);
	            
	            return ResponseEntity.ok(response);
	        } catch (Exception e) {
	            logger.error("An error occurred while retrieving rates and discounts", e);

	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred. Please try again.");
	        }
	    }


	    
	    
	    @PostMapping("/create/rates/discounts")
	     public ResponseEntity<String> createContractRatesAndDiscounts(@RequestBody MstCourierContractDto request,HttpServletRequest httpRequest){
		   
		  return mstCourierContractService.createContractRatesAndDiscounts(request,httpRequest);
		   
	   }

	    @DeleteMapping("/delete/rate/{courierContNo}")
	    public ResponseEntity<?> deleteContract(
	            @PathVariable String courierContNo,
	            @RequestParam(required = false) Double fromWtGms,
	            @RequestParam(required = false) Double toWtGms,
	            @RequestParam(required = false) Double fromDistanceKm,
	            @RequestParam(required = false) Double toDistanceKm,
	            HttpServletRequest httpRequest) {

	        // Pass the additional parameters to the service method
	        return mstCourierContractService.deleteContract(
	                courierContNo, fromWtGms, toWtGms, fromDistanceKm, toDistanceKm, httpRequest);
	    }

	    
	    @DeleteMapping("/delete/discount/{courierContNo}")
	    public ResponseEntity<?> deleteContract(
	            @PathVariable String courierContNo,
	            @RequestParam(required = false) Double fromMonthlyAmt,
	            @RequestParam(required = false) Double toMonthlyAmt,
	            HttpServletRequest httpRequest) {

	        // Pass the additional parameters to the service method
	        return mstCourierContractService.deleteContractdiscount(
	                courierContNo, fromMonthlyAmt, toMonthlyAmt, httpRequest);
	    }

	    

	    @PutMapping("/update-rate/{courierContNo}")
	    public ResponseEntity<?> updateCourierRate( @PathVariable String courierContNo,
	    		@RequestBody MstCourierContractRate updatedRate,
	    		   @RequestParam(required = false) Double fromWtGms,
		            @RequestParam(required = false) Double toWtGms,
		            @RequestParam(required = false) Double fromDistanceKm,
		            @RequestParam(required = false) Double toDistanceKm,
	    		HttpServletRequest httpRequest) {
	    	mstCourierContractService.updateCourierRateFields(courierContNo,updatedRate,fromWtGms,toWtGms,fromDistanceKm,toDistanceKm,httpRequest);
	      //  StatusCodeModal statusCodeModal = new StatusCodeModal();
//
//	        statusCodeModal.setStatus_code(HttpStatus.OK.value());
//            statusCodeModal.setStatus("contract rate successfully.");
	    	  try {
	    	        // Update logic here
	    	        return ResponseEntity.ok()
	    	            .contentType(MediaType.APPLICATION_JSON)
	    	            .body(Map.of("status", "Rate updated successfully.", "status_code", 200));
	    	    } catch (Exception e) {
	    	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	    	            .contentType(MediaType.APPLICATION_JSON)
	    	            .body(Map.of("error", e.getMessage()));
	    	    }
	    }

	    @PutMapping("/update-discount/{courierContNo}")
	    public ResponseEntity<?> updateCourierDiscount( @PathVariable String courierContNo,
	            @RequestParam(required = false) Double fromMonthlyAmt,
	            @RequestParam(required = false) Double toMonthlyAmt,
	            @RequestBody MstCourierContractDiscount updatedDiscount,
	            HttpServletRequest httpRequest) {
	    	mstCourierContractService.updateCourierDiscountFields(courierContNo,fromMonthlyAmt,toMonthlyAmt,updatedDiscount,httpRequest);
	    	  try {
	    	        // Update logic here
	    	        return ResponseEntity.ok()
	    	            .contentType(MediaType.APPLICATION_JSON)
	    	            .body(Map.of("status", "discount updated successfully.", "status_code", 200));
	    	    } catch (Exception e) {
	    	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	    	            .contentType(MediaType.APPLICATION_JSON)
	    	            .body(Map.of("error", e.getMessage()));
	    	    }	    }
	    
	}
 
	


