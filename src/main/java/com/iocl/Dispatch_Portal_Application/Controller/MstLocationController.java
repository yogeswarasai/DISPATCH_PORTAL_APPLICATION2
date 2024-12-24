package com.iocl.Dispatch_Portal_Application.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iocl.Dispatch_Portal_Application.Entity.MstLocation;
import com.iocl.Dispatch_Portal_Application.ServiceLayer.MstLocationService;

@RestController
@RequestMapping("/locations")
public class MstLocationController {

	
	 @Autowired
	    private MstLocationService mstLocationService;

	    @GetMapping
	    public List<MstLocation> getAllLocations() {
	        return mstLocationService.findAll();
	    }

	    @GetMapping("/{locCode}")
	    public ResponseEntity<MstLocation> getLocationById(@PathVariable String locCode) {
	        Optional<MstLocation> location = mstLocationService.findById(locCode);
	        return location.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	    }

	    @PostMapping
	    public MstLocation createLocation(@RequestBody MstLocation mstLocation) {
	        return mstLocationService.save(mstLocation);
	    }

	    @PutMapping("/{locCode}")
	    public ResponseEntity<MstLocation> updateLocation(@PathVariable String locCode, @RequestBody MstLocation mstLocation) {
	        if (!mstLocationService.findById(locCode).isPresent()) {
	            return ResponseEntity.notFound().build();
	        }
	        mstLocation.setLocCode(locCode);
	        return ResponseEntity.ok(mstLocationService.save(mstLocation));
	    }
  
	    @DeleteMapping("/{locCode}")
	    public ResponseEntity<Void> deleteLocation(@PathVariable String locCode) {
	        if (!mstLocationService.findById(locCode).isPresent()) {
	            return ResponseEntity.notFound().build();
	        }
	        mstLocationService.deleteById(locCode);
	        return ResponseEntity.noContent().build();
	    }
	}

