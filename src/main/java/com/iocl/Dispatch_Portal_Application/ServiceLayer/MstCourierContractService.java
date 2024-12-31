package com.iocl.Dispatch_Portal_Application.ServiceLayer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.iocl.Dispatch_Portal_Application.DTO.CourierContractDto;
import com.iocl.Dispatch_Portal_Application.DTO.MstCourierContractDiscountDTO;
import com.iocl.Dispatch_Portal_Application.DTO.MstCourierContractDto;
import com.iocl.Dispatch_Portal_Application.DTO.MstCourierContractRateDto;
import com.iocl.Dispatch_Portal_Application.Entity.MstCourierContract;
import com.iocl.Dispatch_Portal_Application.Entity.MstCourierContractDiscount;
import com.iocl.Dispatch_Portal_Application.Entity.MstCourierContractRate;
import com.iocl.Dispatch_Portal_Application.Repositaries.MstCourierContractDiscountRepository;
import com.iocl.Dispatch_Portal_Application.Repositaries.MstCourierContractRateRepository;
import com.iocl.Dispatch_Portal_Application.Repositaries.MstCourierContractRepository;
import com.iocl.Dispatch_Portal_Application.Security.JwtUtils;
import com.iocl.Dispatch_Portal_Application.composite_pk.CourierContractId;
import com.iocl.Dispatch_Portal_Application.composite_pk.MstCourierContractDiscountId;
import com.iocl.Dispatch_Portal_Application.composite_pk.MstCourierContractRateId;
import com.iocl.Dispatch_Portal_Application.modal.StatusCodeModal;

@Service
public class MstCourierContractService {
	
	private static final Logger logger=LoggerFactory.getLogger(MstCourierContractService.class);
	
	private MstCourierContractRepository mstCourierContractRepository;
	private MstCourierContractDiscountRepository mstCourierContractDiscountRepository;
	 private MstCourierContractRateRepository mstCourierContractRateRepository;
     private JwtUtils jwtUtils;
     private final ModelMapper modelMapper;
     
     


	public MstCourierContractService(MstCourierContractRepository mstCourierContractRepository,
			MstCourierContractDiscountRepository mstCourierContractDiscountRepository,
			MstCourierContractRateRepository mstCourierContractRateRepository, JwtUtils jwtUtils,
			ModelMapper modelMapper) {
		super();
		this.mstCourierContractRepository = mstCourierContractRepository;
		this.mstCourierContractDiscountRepository = mstCourierContractDiscountRepository;
		this.mstCourierContractRateRepository = mstCourierContractRateRepository;
		this.jwtUtils = jwtUtils;
		this.modelMapper = modelMapper;
	}

	 public ResponseEntity<?> createCourierContract(CourierContractDto courierContractDto, HttpServletRequest request) {
	        MstCourierContract courierContract = courierContractDto.toMstCourierContract();
	        String token = jwtUtils.getJwtFromCookies(request);
	        String username=jwtUtils.getUserNameFromJwtToken(token);

	        // Extract locCode from token
	        String locCode = jwtUtils.getLocCodeFromJwtToken(token);
	        if (locCode == null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid JWT token");
	        }
	        courierContract.setLocCode(locCode.trim());  // Setting locCode from token
	        courierContract.setStatus("A");
	        courierContract.setCreatedBy(username);
	        courierContract.setCreatedDate(LocalDate.now());
	       
	                    String courierContNo=courierContractDto.getCourierContNo();
	        // Create a composite key CourierContractId
	       CourierContractId contractId = new CourierContractId(locCode, courierContract.getCourierContNo());
	         Optional<MstCourierContract> contract=mstCourierContractRepository.findById(contractId);
	        if (contract.isPresent()) {
	            StatusCodeModal statusCodeModal = new StatusCodeModal();
	            statusCodeModal.setStatus_code(HttpStatus.BAD_REQUEST.value());
	            statusCodeModal.setStatus("Courier contract already exists");
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCodeModal);
	        }
	        MstCourierContract createdContract = mstCourierContractRepository.save(courierContract);
	        StatusCodeModal statusCodeModal = new StatusCodeModal();
	        statusCodeModal.setStatus_code(HttpStatus.CREATED.value());
	        statusCodeModal.setStatus("Courier contract created successfully.");
	        return ResponseEntity.status(HttpStatus.CREATED).body(statusCodeModal);
	    }

	    public ResponseEntity<StatusCodeModal> updateCourierContract(String courierContNo, MstCourierContract mstCourierContract, HttpServletRequest request) {
	        String token = jwtUtils.getJwtFromCookies(request);
	        String locCode = jwtUtils.getLocCodeFromJwtToken(token);
	        String username=jwtUtils.getUserNameFromJwtToken(token);

	        // Create composite key CourierContractId
	        CourierContractId contractId = new CourierContractId(locCode, courierContNo);

	        Optional<MstCourierContract> existingContract = mstCourierContractRepository.findById(contractId);
	        StatusCodeModal statusCodeModal = new StatusCodeModal();

	        if (!existingContract.isPresent()) {
	            statusCodeModal.setStatus_code(HttpStatus.NOT_FOUND.value());
	            statusCodeModal.setStatus("Courier contract not found.");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(statusCodeModal);
	        }

	        MstCourierContract contractToUpdate = existingContract.get();
	        mstCourierContract.setLocCode(contractToUpdate.getLocCode());  // Ensure locCode doesn't change
	        mstCourierContract.setCourierContNo(courierContNo);
	        mstCourierContract.setCreatedBy(username);
	        mstCourierContract.setCreatedDate(LocalDate.now());
	        mstCourierContract.setLastUpdatedDate(LocalDateTime.now());

	        MstCourierContract updatedContract = mstCourierContractRepository.save(mstCourierContract);
	        statusCodeModal.setStatus_code(HttpStatus.OK.value());
	        statusCodeModal.setStatus("Courier contract updated successfully.");
	        return ResponseEntity.ok(statusCodeModal);
	    }

	 @Transactional
	    public ResponseEntity<StatusCodeModal> deleteCourierContract(String courierContNo, HttpServletRequest request) {
	        String token = jwtUtils.getJwtFromCookies(request);
	        String locCode = jwtUtils.getLocCodeFromJwtToken(token);

	        // Create composite key CourierContractId
	        CourierContractId contractId = new CourierContractId(locCode, courierContNo);

	        Optional<MstCourierContract> contractOptional = mstCourierContractRepository.findById(contractId);
	        StatusCodeModal statusCodeModal = new StatusCodeModal();

	        logger.info("before contract");
	        if (contractOptional.isPresent()) {
	            logger.info("after contract");

	            // Ensure the delete operation is performed within a transaction
	         //   mstCourierContractRepository.deleteByLocCodeAndCourierContNo(locCode, courierContNo);
	            mstCourierContractRepository.deleteById(contractId);
	            statusCodeModal.setStatus_code(HttpStatus.OK.value());
	            statusCodeModal.setStatus("Courier contract deleted successfully.");
	            return ResponseEntity.ok(statusCodeModal);
	        } else {
	            statusCodeModal.setStatus_code(HttpStatus.NOT_FOUND.value());
	            statusCodeModal.setStatus("Courier contract not found.");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(statusCodeModal);
	        }
	    }

	    public List<MstCourierContract> getCourierContractByContNo(HttpServletRequest request) {
	        String token = jwtUtils.getJwtFromCookies(request);
	        String locCode = jwtUtils.getLocCodeFromJwtToken(token);

	        // Create composite key CourierContractId
	       // CourierContractId contractId = new CourierContractId(locCode, courierContNo);
	        return mstCourierContractRepository.findByLocCode(locCode);
	    }

	    public ResponseEntity<List<MstCourierContract>> getContractsBasedOnCourierCodeAndLocCode(String courierCode, HttpServletRequest request) {
	        
	        // Extract locCode from JWT token
	        String token = jwtUtils.getJwtFromCookies(request);
	        String locCode = jwtUtils.getLocCodeFromJwtToken(token);

	        try {
	            // Fetch records from the database based on locCode and courierCode
	            List<MstCourierContract> contracts = mstCourierContractRepository.findByLocCodeAndCourierCode(locCode, courierCode);
	            return ResponseEntity.ok(contracts);
	        } catch (Exception e) {
	            // Handle exceptions, e.g., log the error or return a custom error message
	            return ResponseEntity.status(500).body(null); // Return an appropriate error response
	        }
	    }
	    
	    
	    
	    
	 
	    
	    public ResponseEntity<String> createContractRatesAndDiscounts(MstCourierContractDto request, HttpServletRequest httpRequest) {
			logger.debug("Received MstCourierContract Details:" +request);
			
			    String token = jwtUtils.getJwtFromCookies(httpRequest);
		        String locCode = jwtUtils.getLocCodeFromJwtToken(token);
		        String username = jwtUtils.getUserNameFromJwtToken(token);		
			String courierContNo=request.getCourierContNo();
			try {
				
				
				 if(request.getCourierRates() != null) {
	            	    for (MstCourierContractRateDto ratesDto : request.getCourierRates()) {
	            	        logger.debug("Received rate DTO: {}", ratesDto); // Log DTO values

	            	        MstCourierContractRate courierRate = new MstCourierContractRate();
	            	        courierRate.setLocCode(locCode);
	            	        courierRate.setCourierContNo(courierContNo);
	            	        courierRate.setFromWtGms(ratesDto.getFromWtGms());
	            	        courierRate.setToWtGms(ratesDto.getToWtGms());
	            	        courierRate.setFromDistanceKm(ratesDto.getFromDistanceKm());
	            	        courierRate.setToDistanceKm(ratesDto.getToDistanceKm());
	            	        courierRate.setRate(ratesDto.getRate());
	            	        courierRate.setStatus("A");
	            	        courierRate.setCreatedBy(username);
	            	        courierRate.setCreatedDate(LocalDate.now());

	            	        logger.debug("Saving courier rate: {}", courierRate); // Log before saving
	            	        mstCourierContractRateRepository.save(courierRate);
	            	    }
	            	} else {
//	            	    logger.error("Courier rates are null");
//	            	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rates object is not present.");
	            		 for (MstCourierContractRateDto ratesDto : request.getCourierRates()) {
		            	        logger.debug("Received rate DTO: {}", ratesDto); // Log DTO values
	            		  MstCourierContractRate courierRate = new MstCourierContractRate();
	            	        courierRate.setLocCode(locCode);
	            	        courierRate.setCourierContNo(courierContNo);
	            	        courierRate.setFromWtGms(ratesDto.getFromWtGms());
	            	        courierRate.setToWtGms(ratesDto.getToWtGms());
	            	        courierRate.setFromDistanceKm(ratesDto.getFromDistanceKm());
	            	        courierRate.setToDistanceKm(ratesDto.getToDistanceKm());
	            	        courierRate.setRate(ratesDto.getRate());
	            	        courierRate.setStatus("A");
	            	        courierRate.setCreatedBy(username);
	            	        courierRate.setCreatedDate(LocalDate.now());

	            	        logger.debug("Saving courier rate: {}", courierRate); // Log before saving
	            	        mstCourierContractRateRepository.save(courierRate);
	            	}

	            	}
	              if(request.getCourierDiscounts()!=null)  {
	            	  for (MstCourierContractDiscountDTO discountDTO : request.getCourierDiscounts()) {
						
	            		  
	            		  MstCourierContractDiscount discount=new MstCourierContractDiscount();
	                      discount.setLocCode(locCode);
	                      discount.setCourierContNo(courierContNo);
	                      discount.setToMonthlyAmt(discountDTO.getToMonthlyAmt());
	                      discount.setFromMonthlyAmt(discountDTO.getFromMonthlyAmt());
	                      discount.setDiscountPercentage(discountDTO.getDiscountPercentage());
	                      discount.setStatus("A");
	                      discount.setCreatedBy(username);
	                      discount.setCreatedDate(LocalDate.now());
	                      
	                      mstCourierContractDiscountRepository.save(discount);
					}
	              
	              }
	              else{
	            	//  return ResponseEntity.status(HttpStatus.NOT_FOUND).body("discount object is not there");
//	            	  for (MstCourierContractRateDto ratesDto : request.getCourierRates()) {
//	            	        logger.debug("Received rate DTO: {}", ratesDto); // Log DTO values
 for (MstCourierContractDiscountDTO discountDTO : request.getCourierDiscounts()) {
						
	            		  
	            		  MstCourierContractDiscount discount=new MstCourierContractDiscount();
	                      discount.setLocCode(locCode);
	                      discount.setCourierContNo(courierContNo);
	                      discount.setToMonthlyAmt(discountDTO.getToMonthlyAmt());
	                      discount.setFromMonthlyAmt(discountDTO.getFromMonthlyAmt());
	                      discount.setDiscountPercentage(discountDTO.getDiscountPercentage());
	                      discount.setStatus("A");
	                      discount.setCreatedBy(username);
	                      discount.setCreatedDate(LocalDate.now());
	                      
	                      mstCourierContractDiscountRepository.save(discount);
	              }
	              }
	              
	             
	           
	                logger.info("Courier contract with locCode {} and courierContNo {} has been successfully created", 
	                		locCode, courierContNo);
	                return ResponseEntity.status(HttpStatus.CREATED).body("Contract processed successfully.");
	              
	   
	        } catch (Exception e) {
	            logger.error("An error occurred while processing the courier contract", e);
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred. Please try again.");
	        }
			
		}
	    
	    

	    
	    public ResponseEntity<?> deleteContract(
	            String courierContNo,
	            Double fromWtGms, Double toWtGms,
	            Double fromDistanceKm, Double toDistanceKm,
	            HttpServletRequest httpRequest) {

	        String token = jwtUtils.getJwtFromCookies(httpRequest);
	        String locCode = jwtUtils.getLocCodeFromJwtToken(token);

	        try {
	            // Create ID objects with provided parameters
	                // Create Rate ID with provided parameters
	                MstCourierContractRateId rateId = new MstCourierContractRateId();
	                rateId.setLocCode(locCode);
	                rateId.setCourierContNo(courierContNo);
	                rateId.setFromWtGms(fromWtGms);
	                rateId.setToWtGms(toWtGms);
	                rateId.setFromDistanceKm(fromDistanceKm);
	                rateId.setToDistanceKm(toDistanceKm);

	                Optional<MstCourierContractRate> rate = 
	                        mstCourierContractRateRepository.findById(rateId);

	                if (rate.isPresent()) {
	                    // Delete the specific rate record
	                    mstCourierContractRateRepository.deleteById(rateId);
	                    return ResponseEntity.ok("Specific contract rate  deleted successfully.");
	                } else {
	                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rate not found for the contract.");
	                }
	           
	        } catch (Exception e) {
	            System.err.println("Error occurred while deleting the contract: " + e.getMessage());
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                                 .body("Error occurred while deleting the contract.");
	        }
	    }

		public ResponseEntity<?> deleteContractdiscount(String courierContNo, Double fromMonthlyAmt,
				Double toMonthlyAmt, HttpServletRequest httpRequest) {
			
			 String token = jwtUtils.getJwtFromCookies(httpRequest);
		        String locCode = jwtUtils.getLocCodeFromJwtToken(token);
		        
		        try {
		            // Create ID objects with provided parameters
		            MstCourierContractDiscountId discountId = new MstCourierContractDiscountId();
		            discountId.setLocCode(locCode);
		            discountId.setCourierContNo(courierContNo);
		            discountId.setFromMonthlyAmt(fromMonthlyAmt);
		            discountId.setToMonthlyAmt(toMonthlyAmt);
		           

		            Optional<MstCourierContractDiscount> discount = 
		                    mstCourierContractDiscountRepository.findById(discountId);

		            if (discount.isPresent()) {
		                // Delete the specific discount record
		                mstCourierContractDiscountRepository.deleteById(discountId);
	                    return ResponseEntity.ok("Specific contract discount deleted successfully.");


		            } else {
		                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Discount not found for the contract.");
		            }
		        } catch (Exception e) {
		            System.err.println("Error occurred while deleting the contract: " + e.getMessage());
		            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                                 .body("Error occurred while deleting the contract.");
		        }
		
			
		}

		@Transactional
		public void updateCourierRateFields(String courierContNo, MstCourierContractRate updatedRate, Double fromWtGms, Double toWtGms, Double fromDistanceKm, Double toDistanceKm, HttpServletRequest httpRequest) {
		    String token = jwtUtils.getJwtFromCookies(httpRequest);
		    String locCode = jwtUtils.getLocCodeFromJwtToken(token);
	        String username = jwtUtils.getUserNameFromJwtToken(token);		

		    // Step 1: Delete the old record using the original primary keys
		    MstCourierContractRateId oldRateId = new MstCourierContractRateId();
		    oldRateId.setLocCode(locCode);
		    oldRateId.setCourierContNo(courierContNo);
		    oldRateId.setFromWtGms(fromWtGms);
		    oldRateId.setToWtGms(toWtGms);
		    oldRateId.setFromDistanceKm(fromDistanceKm);
		    oldRateId.setToDistanceKm(toDistanceKm);
		    Optional<MstCourierContractRate> rateexists= mstCourierContractRateRepository.findById(oldRateId);
		    // Check if the record exists before deleting
//		    if (!mstCourierContractRateRepository.existsById(oldRateId)) {
//		        throw new EntityNotFoundException("Rate record not found for the provided ID: " + oldRateId);
//		    }
              if(rateexists.isPresent())
              {
		    // Delete the old rate record
		    logger.info("Deleting rate record with ID: {}", oldRateId);
		    mstCourierContractRateRepository.deleteById(oldRateId);

		    // Step 2: Create and save the new record with updated primary key
		    MstCourierContractRate newRate = new MstCourierContractRate();
		    newRate.setLocCode(locCode);
		    newRate.setCourierContNo(courierContNo.trim());
		    newRate.setFromWtGms(updatedRate.getFromWtGms());
		    newRate.setToWtGms(updatedRate.getToWtGms());
		    newRate.setFromDistanceKm(updatedRate.getFromDistanceKm());
		    newRate.setToDistanceKm(updatedRate.getToDistanceKm());
		    newRate.setRate(updatedRate.getRate());
		    newRate.setCreatedBy(username);
		    newRate.setStatus("A");
		    newRate.setCreatedDate(LocalDate.now());

		    // Save the new rate record with the updated primary key
		    logger.info("Saving new rate record: {}", newRate);
		    mstCourierContractRateRepository.save(newRate);
              }
		}


		    @Transactional
		    public void updateCourierDiscountFields(String courierContNo, Double fromMonthlyAmt, Double toMonthlyAmt, MstCourierContractDiscount updatedDiscount, HttpServletRequest httpRequest) {
		    	
		    	
		    	 String token = jwtUtils.getJwtFromCookies(httpRequest);
				    String locCode = jwtUtils.getLocCodeFromJwtToken(token);
			        String username = jwtUtils.getUserNameFromJwtToken(token);
		        // Step 1: Delete the old record using the original primary keys
		        MstCourierContractDiscountId oldDiscountId = new MstCourierContractDiscountId();
		        oldDiscountId.setLocCode(locCode);
		        oldDiscountId.setCourierContNo(courierContNo);
		        oldDiscountId.setFromMonthlyAmt(fromMonthlyAmt);
		        oldDiscountId.setToMonthlyAmt(toMonthlyAmt);
		        // Delete the old discount record
			    Optional<MstCourierContractDiscount> discountexists= mstCourierContractDiscountRepository.findById(oldDiscountId);
			    if(discountexists.isPresent())
	              {
		        mstCourierContractDiscountRepository.deleteById(oldDiscountId);

		        // Step 2: Create and save the new record with updated primary key
		        MstCourierContractDiscount newDiscount = new MstCourierContractDiscount();
		        newDiscount.setLocCode(locCode);
		        newDiscount.setCourierContNo(courierContNo);
		        newDiscount.setFromMonthlyAmt(updatedDiscount.getFromMonthlyAmt());
		        newDiscount.setToMonthlyAmt(updatedDiscount.getToMonthlyAmt());
		        newDiscount.setDiscountPercentage(updatedDiscount.getDiscountPercentage());
		        newDiscount.setStatus("A");
		        newDiscount.setCreatedBy(username);
		        newDiscount.setCreatedDate(LocalDate.now());

		        // Save the new discount record with the updated primary key
		        mstCourierContractDiscountRepository.save(newDiscount);
		    }
		    }
	        }
