package com.iocl.Dispatch_Portal_Application.Controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import com.iocl.Dispatch_Portal_Application.DTO.ParcelOutDto;
import com.iocl.Dispatch_Portal_Application.DTO.ParcelOutResponse;
import com.iocl.Dispatch_Portal_Application.Entity.TrnParcelOut;
import com.iocl.Dispatch_Portal_Application.Repositaries.TrnParcelOutRepository;
import com.iocl.Dispatch_Portal_Application.Security.JwtUtils;
import com.iocl.Dispatch_Portal_Application.ServiceLayer.TrnParcelOutService;
import com.iocl.Dispatch_Portal_Application.modal.StatusCodeModal;
@RestController
@RequestMapping("/parcels-out")
@CrossOrigin(origins = "http://localhost:4200")
public class TrnParcelOutController {

	@Autowired
    private TrnParcelOutService trnParcelOutService;
	@Autowired
	private TrnParcelOutRepository trnParcelOutRepository;
	@Autowired
	private JwtUtils jwtUtils;
	
 
	    @GetMapping
	    public List<TrnParcelOut> getAllParcelsOut() {
	        return trnParcelOutService.findAll();
	    }

    
	    
	    @GetMapping("/consignment/{number}/exists")
	    public ResponseEntity<Boolean> checkConsignmentExists(@PathVariable String number) {
	        boolean exists = trnParcelOutRepository.existsByConsignmentNumber(number);
	        return ResponseEntity.ok(exists);
	    }

	    
	    @GetMapping("/get-out-parcelsbyloccode")
	    public ResponseEntity<Page<ParcelOutDto>> getParcelsByLocationCode(
	            HttpServletRequest request,
	            @RequestParam(defaultValue = "0") int page,
	            @RequestParam(defaultValue = "8") int size) {

	        String token = jwtUtils.getJwtFromCookies(request);
	        String locCode = jwtUtils.getLocCodeFromJwtToken(token);

	        Pageable pageable = PageRequest.of(page, size);
	        Page<ParcelOutDto> parcels = trnParcelOutService.getparcelsByLocationCode(locCode, pageable);
	       
	        return ResponseEntity.ok(parcels);
	    }
	    
	    @PostMapping
	    public ResponseEntity<?> createParcelOut(@RequestBody ParcelOutDto parcelOutRequest, HttpServletRequest request) throws IOException {
	        return trnParcelOutService.createParcelOut(parcelOutRequest, request);
	    }

	    @PutMapping("/{senderLocCode}/{outTrackingId}")
	    public ResponseEntity<StatusCodeModal> updateParcelOut(@PathVariable String senderLocCode, @PathVariable Long outTrackingId, @RequestBody TrnParcelOut trnParcelOut) {
	        return trnParcelOutService.updateParcelOut(senderLocCode, outTrackingId, trnParcelOut);
	    }

	    @DeleteMapping("/{outTrackingId}")
	    public ResponseEntity<StatusCodeModal> deleteParcelOut(@PathVariable Long outTrackingId, HttpServletRequest request) {
	        return trnParcelOutService.deleteParcelOut(outTrackingId, request);
	    }

    
	    @GetMapping("/out/{consignmentNumber}")
	    public ResponseEntity<ParcelOutResponse> getParcelOutByConsignmentNumber(@PathVariable String consignmentNumber) {

	        return trnParcelOutService.getParcelOutByConsignmentNumber(consignmentNumber);

	    }

	    @GetMapping("/distance")
	    public ResponseEntity<?> getDistance(
	           
	            @RequestParam("recipientLocCode") String recipientLocCode,HttpServletRequest request) {
	        try {
	            Double distance = trnParcelOutService.getDistance(recipientLocCode,request);
	            return ResponseEntity.ok(distance);
	        } catch (RuntimeException e) {
	            return ResponseEntity.badRequest().body(e.getMessage());
	        } catch (Exception e) {
	            return ResponseEntity.status(500).body("Internal server error occurred.");
	        }
	    }

}


