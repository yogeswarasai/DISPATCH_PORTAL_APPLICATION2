package com.iocl.Dispatch_Portal_Application.Controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import com.iocl.Dispatch_Portal_Application.DTO.ParcelInDto;
import com.iocl.Dispatch_Portal_Application.Entity.TrnParcelIn;
import com.iocl.Dispatch_Portal_Application.Repositaries.TrnParcelInRepository;
import com.iocl.Dispatch_Portal_Application.Repositaries.TrnParcelOutRepository;
import com.iocl.Dispatch_Portal_Application.Security.JwtUtils;
import com.iocl.Dispatch_Portal_Application.ServiceLayer.EmployeeService;
import com.iocl.Dispatch_Portal_Application.ServiceLayer.TrnParcelInService;
import com.iocl.Dispatch_Portal_Application.modal.StatusCodeModal;


@RestController
@RequestMapping("/parcels-in")
public class TrnParcelInController {

	 @Autowired
	    private TrnParcelInService trnParcelInService;
	 @Autowired
	 private TrnParcelInRepository trnParcelInRepository;
	 
	 @Autowired
	    private JwtUtils jwtUtils;
	   
	   private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

	 
	    @GetMapping
	    public List<TrnParcelIn> getAllParcelsIn() {
	        return trnParcelInService.findAll();
	    }


	    @GetMapping("/consignment/{number}/exists")
	    public ResponseEntity<Boolean> checkConsignmentExists(@PathVariable String number) {
	        boolean exists = trnParcelInRepository.existsByConsignmentNumber(number);
	        return ResponseEntity.ok(exists);
	    }

	    
	    @GetMapping("/get-in-parcelsbyloccode")
	    public ResponseEntity<Page<ParcelInDto>> getParcelsByLocationCode(
	            HttpServletRequest request,
	            @RequestParam(defaultValue = "0") int page,
	            @RequestParam(defaultValue = "8") int size) {

	        String token = jwtUtils.getJwtFromCookies(request);
	        String locCode = jwtUtils.getLocCodeFromJwtToken(token);

	        Pageable pageable = PageRequest.of(page, size);
	        Page<ParcelInDto> parcels = trnParcelInService.getparcelsByLocationCode(locCode, pageable);
	       
	        return ResponseEntity.ok(parcels);
	    }
	    
	    @PostMapping
	    public ResponseEntity<?> createParcelIn(@RequestBody ParcelInDto parcelInRequest, HttpServletRequest request) throws IOException, URISyntaxException, InterruptedException {
	        return trnParcelInService.createParcelIn(parcelInRequest, request);
	    }

	    @PutMapping("/{recipientLocCode}/{inTrackingId}")
	    public ResponseEntity<StatusCodeModal> updateParcelIn(@PathVariable String recipientLocCode, @PathVariable Long inTrackingId, @RequestBody TrnParcelIn trnParcelIn) {
	        return trnParcelInService.updateParcelIn(recipientLocCode, inTrackingId, trnParcelIn);
	    }

	    @DeleteMapping("/{inTrackingId}")
	    public ResponseEntity<StatusCodeModal> deleteParcelIn(@PathVariable Long inTrackingId, HttpServletRequest request) {
	        return trnParcelInService.deleteParcelIn(inTrackingId, request);
	    }
	  
	  
	}


