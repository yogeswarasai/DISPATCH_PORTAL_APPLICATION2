package com.iocl.Dispatch_Portal_Application.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iocl.Dispatch_Portal_Application.Entity.RefSequence;
import com.iocl.Dispatch_Portal_Application.Repositaries.RefSequenceRepository;
import com.iocl.Dispatch_Portal_Application.ServiceLayer.MstLocationService;
import com.iocl.Dispatch_Portal_Application.ServiceLayer.RefSequenceService;
import com.iocl.Dispatch_Portal_Application.modal.StatusCodeModal;

@RestController
@RequestMapping("/sequences")
@CrossOrigin(origins = "http://localhost:4200")
public class RefSequenceController {

	 @Autowired
	    private RefSequenceService refSequenceService;
	 
	 @Autowired
	 private RefSequenceRepository refSequenceRepository;
	 
	 
	 @Autowired
	 private MstLocationService locationService;
	 	 


	 
	    @GetMapping("/locCode/{locCode}/exists")
	    public ResponseEntity<Boolean> checkConsignmentExists(@PathVariable String locCode) {
	        boolean exists = refSequenceRepository.existsByLocCode(locCode);
	        return ResponseEntity.ok(exists);
	    } 
	 
	 @GetMapping
	    public Page<RefSequence> getAllSequences(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size
	    ) {
	        return refSequenceService.findAll(page, size);
	    }
	 
	
	 
	    @GetMapping("/{locCode}")
	    public ResponseEntity<RefSequence> getSequenceById(@PathVariable String locCode) {
	        Optional<RefSequence> sequence = refSequenceService.findById(locCode);
	        return sequence.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	    }
	    
	    @PostMapping
	    public ResponseEntity<StatusCodeModal> createSequence(@RequestBody RefSequence refSequence) {
	    	
	    	
	        StatusCodeModal statusCodeModal = new StatusCodeModal();
	        
	        if (!locationService.doesLocCodeExist(refSequence.getLocCode())) {
	             statusCodeModal.setStatus_code(HttpStatus.NOT_FOUND.value());
	            statusCodeModal.setStatus("Loc code not found: " + refSequence.getLocCode());
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCodeModal);
	        }

	    	 if (refSequenceRepository.existsByLocCode(refSequence.getLocCode())) {
	             statusCodeModal.setStatus_code(HttpStatus.NOT_FOUND.value());
	             statusCodeModal.setStatus("Loc code already exists: " + refSequence.getLocCode());
	             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCodeModal);
	         }
	    	 
	    	
	    	
	        RefSequence createdSequence = refSequenceService.save(refSequence);

	        if (createdSequence != null) {
	            statusCodeModal.setStatus_code(HttpStatus.CREATED.value());
	            statusCodeModal.setStatus("Application Implemented successfully with locCode: " + createdSequence.getLocCode());
	            return ResponseEntity.status(HttpStatus.CREATED).body(statusCodeModal);
	        } else {
	            statusCodeModal.setStatus_code(HttpStatus.BAD_REQUEST.value());
	            statusCodeModal.setStatus("Loc code already exists.");
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCodeModal);
	        }
	    }


	    @DeleteMapping("/{locCode}")
	    public ResponseEntity<StatusCodeModal> deleteSequence(@PathVariable String locCode) {
	        Optional<RefSequence> existingSequence = refSequenceService.findById(locCode);
	        StatusCodeModal statusCodeModal = new StatusCodeModal();

	        if (!existingSequence.isPresent()) {
	            statusCodeModal.setStatus_code(HttpStatus.NOT_FOUND.value());
	            statusCodeModal.setStatus("Sequence with locCode: " + locCode + " not found.");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(statusCodeModal);
	        }

	        refSequenceService.deleteById(locCode);
	        statusCodeModal.setStatus("Sequence deleted successfully with locCode: " + locCode);
	        statusCodeModal.setStatus_code(HttpStatus.NO_CONTENT.value());
	        return ResponseEntity.ok(statusCodeModal);
	    }

	}

