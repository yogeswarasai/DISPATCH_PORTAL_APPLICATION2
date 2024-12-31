package com.iocl.Dispatch_Portal_Application.ServiceLayer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.iocl.Dispatch_Portal_Application.DTO.ParcelOutDto;
import com.iocl.Dispatch_Portal_Application.DTO.ParcelOutResponse;
import com.iocl.Dispatch_Portal_Application.Entity.MstEmployee;
import com.iocl.Dispatch_Portal_Application.Entity.MstUser;
import com.iocl.Dispatch_Portal_Application.Entity.TrnParcelOut;
import com.iocl.Dispatch_Portal_Application.Repositaries.EmployeeRepository;
import com.iocl.Dispatch_Portal_Application.Repositaries.MstUserRepository;
import com.iocl.Dispatch_Portal_Application.Repositaries.TrnParcelOutRepository;
import com.iocl.Dispatch_Portal_Application.Security.JwtUtils;
import com.iocl.Dispatch_Portal_Application.composite_pk.TrnParcelOutPK;
import com.iocl.Dispatch_Portal_Application.modal.StatusCodeModal;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class TrnParcelOutService {

	 @Autowired
	    private TrnParcelOutRepository trnParcelOutRepository;

	    @Autowired
	    private JwtUtils jwtUtils;

	    @Autowired
	    private MstUserRepository mstUserRepositary;

	    @Autowired
	    private EmployeeRepository employeeRepository;

	    @Autowired
	    private EmailService emailService;
	    
	    @Autowired
	    private MstLocationService mstLocationService;

	    private static final Logger logger = LoggerFactory.getLogger(TrnParcelOutService.class);
	    
	    public List<TrnParcelOut> findAll() {
	        return trnParcelOutRepository.findAll();
	    }
	    
	    
	    public ResponseEntity<?> createParcelOut(ParcelOutDto parcelOutRequest, HttpServletRequest request) throws IOException {
	        TrnParcelOut parcelOut = parcelOutRequest.toTrnParcelOut();
	        String token = jwtUtils.getJwtFromCookies(request);

	        // Validate and extract information from the JWT token
	        String locCode = jwtUtils.getLocCodeFromJwtToken(token);
	        String username = jwtUtils.getUserNameFromJwtToken(token);

	        if (locCode == null || username == null) {
	            StatusCodeModal statusCodeModal = new StatusCodeModal();
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(statusCodeModal);
	        }

	        parcelOut.setSenderLocCode(locCode);
	        parcelOut.setCreatedBy(username);
	        parcelOut.setCreatedDate(LocalDate.now());

	        if (trnParcelOutRepository.existsByConsignmentNumber(parcelOut.getConsignmentNumber())) {
	            StatusCodeModal statusCodeModal = new StatusCodeModal();
	            statusCodeModal.setStatus_code(HttpStatus.BAD_REQUEST.value());
	            statusCodeModal.setStatus("Consignment number already exists: " + parcelOut.getConsignmentNumber());
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCodeModal);
	        }

	        TrnParcelOut createdParcel = trnParcelOutRepository.save(parcelOut);

	        StatusCodeModal statusCodeModal = new StatusCodeModal();
	        if (createdParcel != null) {
	            // Generate PDF
	            byte[] pdfBytes = generatePdf(createdParcel);

	            // Fetch employee email based on locCode and recipient's name
	            Optional<MstEmployee> recipientEmployeeOpt = employeeRepository.findByLocCodeAndEmpName(locCode, parcelOut.getSenderName());
	            if (recipientEmployeeOpt.isPresent()) {
	                MstEmployee recipientEmployee = recipientEmployeeOpt.get();
	                String email = recipientEmployee.getEmailId();
	                String name = recipientEmployee.getEmpName();
	                if (email != null) {
	                	String subject = "Parcel Notification";
	                	String messageBody = "<p>Dear " + name + ",</p>" +
	                            "<p>You are sending a new parcel with Tracking ID: " + createdParcel.getConsignmentNumber() + ".</p>" +
	                            "<p>Please find the details in the attached PDF.</p>" +
	                            "<p>Best regards,<br>" +
	                            "Indian Oil Corporation Limited</p>";
	               //     emailService.sendEmail(email, subject, messageBody, pdfBytes);	                	
	              //      emailService.sendEmail(email, "Parcel Notification", "You have received a new parcel with Tracking ID: " + createdParcel.getConsignmentNumber(), pdfBytes);
	                }
	            }

	            statusCodeModal.setStatus_code(HttpStatus.CREATED.value());
	            statusCodeModal.setStatus("Parcel created successfully with id: " + createdParcel.getConsignmentNumber());
	            return ResponseEntity.status(HttpStatus.CREATED).body(statusCodeModal);
	        } else {
	            statusCodeModal.setStatus_code(HttpStatus.BAD_REQUEST.value());
	            statusCodeModal.setStatus("Failed to create parcel. Please try again.");
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCodeModal);
	        }
	    }

	    
	    
	    public ResponseEntity<StatusCodeModal> updateParcelOut(String senderLocCode, Long OutTrackingId, TrnParcelOut trnParcelOut) {
	        TrnParcelOutPK id = new TrnParcelOutPK(senderLocCode, OutTrackingId);
	        Optional<TrnParcelOut> parcelout = trnParcelOutRepository.findById(id);
	        if (!parcelout.isPresent()) {
	            return ResponseEntity.notFound().build();
	        }
	        TrnParcelOut updateparcelwith = parcelout.get();
	        trnParcelOut.setSenderLocCode(senderLocCode);
	        trnParcelOut.setOutTrackingId(OutTrackingId);
	        trnParcelOut.setRecordStatus(updateparcelwith.getRecordStatus());
	        trnParcelOut.setCreatedBy(updateparcelwith.getCreatedBy());
	        trnParcelOut.setLastUpdatedDate(LocalDateTime.now());

	        TrnParcelOut updatedParcel = trnParcelOutRepository.save(trnParcelOut);

	        StatusCodeModal statusCodeModal = new StatusCodeModal();
	        if (updatedParcel != null) {
	            statusCodeModal.setStatus_code(200);
	            statusCodeModal.setStatus("Parcel updated successfully with id: " + updatedParcel.getOutTrackingId());
	        } else {
	            statusCodeModal.setStatus_code(400);
	            statusCodeModal.setStatus("Parcel update failed. Try again.");
	        }

	        return ResponseEntity.ok(statusCodeModal);
	    }

	    
	    
	    public ResponseEntity<StatusCodeModal> deleteParcelOut(Long OutTrackingId, HttpServletRequest request) {
	        // Get JWT token from cookies
	        String token = jwtUtils.getJwtFromCookies(request);

	        // Validate and extract location code from JWT token
	        String locCode = jwtUtils.getLocCodeFromJwtToken(token);
	        if (locCode == null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	        }

	        TrnParcelOutPK id = new TrnParcelOutPK(locCode, OutTrackingId);
	        Optional<TrnParcelOut> parcelOptional = trnParcelOutRepository.findById(id);

	        if (parcelOptional.isPresent()) {
	            TrnParcelOut trnParcel = parcelOptional.get();
	            trnParcel.setRecordStatus("D");  
	            trnParcel.setLastUpdatedDate(LocalDateTime.now());	            
	            trnParcelOutRepository.save(trnParcel); // Use save() to update the record

	            // Generate PDF
	            byte[] pdfBytes;
	            try {
	                pdfBytes = generatePdf(trnParcel);
	            } catch (IOException e) {
	                e.printStackTrace();
	                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	            }

	            // Find loc_admin from user table with case-insensitive roleId
	            Optional<MstUser> locAdminUserOpt = mstUserRepositary.findByRoleIdAndLocCodeIgnoreCase("loc_admin", locCode);
	            if (locAdminUserOpt.isPresent()) {
	                MstUser locAdminUser = locAdminUserOpt.get();

	                // Remove leading zeroes from userId to match employee code format
	                String formattedEmpCode = removeLeadingZeros(locAdminUser.getUserId()).trim();
	                Optional<MstEmployee> locAdminEmpOpt = employeeRepository.findByEmpCode(Integer.parseInt(formattedEmpCode));
	                if (locAdminEmpOpt.isPresent()) {
	                    MstEmployee locAdmin = locAdminEmpOpt.get();
	                    String email = locAdmin.getEmailId();
	                    if (email != null) {
	                        try {
//	                            emailService.sendEmail(email, "Parcel Status Changed", "The status of the parcel with Tracking ID " + OutTrackingId + " has been changed to 'D'.", pdfBytes);
	                        } catch (Exception e) {
	                            e.printStackTrace();
	                        }
	                    }
	                }
	            }

	            StatusCodeModal statusCodeModal = new StatusCodeModal();
	            statusCodeModal.setStatus_code(200);
	            statusCodeModal.setStatus("Parcel deleted successfully");

	            return ResponseEntity.ok(statusCodeModal);
	        }

	        return ResponseEntity.noContent().build();
	    }

	    
	    
	    public String removeLeadingZeros(String id) {
	        // Remove leading zeroes
	        return id.replaceFirst("^0+(?!$)", "");
	    }

	    
	    
	    public byte[] generatePdf(TrnParcelOut parcel) throws IOException {
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
	        try {
	            PdfWriter.getInstance(document, baos);
	            document.open();
	            document.add(new Paragraph("Parcel Details"));
	            document.add(new Paragraph("Tracking ID: " + parcel.getOutTrackingId()));
	            document.add(new Paragraph("Consignment Number: " + parcel.getConsignmentNumber()));
	            document.add(new Paragraph("Consignment Date: " + parcel.getConsignmentDate()));
	            document.add(new Paragraph("Sender: " + parcel.getSenderName() + " (" + parcel.getSenderDepartment() + ")"));
	            document.add(new Paragraph("Recipient: " + parcel.getRecipientName() + " (" + parcel.getRecipientDepartment() + ")"));
	            document.add(new Paragraph("Courier: " + parcel.getCourierName()));
	            document.add(new Paragraph("Weight: " + parcel.getWeight()));
	            document.add(new Paragraph("Unit: " + parcel.getUnit()));
	            document.add(new Paragraph("Distance:" +parcel.getDistance()));
	            document.add(new Paragraph("Record Status: " + parcel.getRecordStatus()));
	        } catch (DocumentException e) {
	            e.printStackTrace();
	        } finally {
	            document.close();
	        }
	        return baos.toByteArray();
	    }

		

	    
	    public Page<ParcelOutDto> getparcelsByLocationCode(String senderLocCode, Pageable pageable) {
	        Page<TrnParcelOut> parcels = trnParcelOutRepository.findBySenderLocCodeOrderByOutTrackingIdDesc(senderLocCode, pageable);

	        return parcels.map(parcel -> {
	            ParcelOutDto dto = new ParcelOutDto();
	            dto.setSenderLocCode(parcel.getSenderLocCode());
	            dto.setOutTrackingId(parcel.getOutTrackingId());
	            dto.setConsignmentNumber(parcel.getConsignmentNumber());
	            dto.setConsignmentDate(parcel.getConsignmentDate());
	            dto.setSenderDepartment(parcel.getSenderDepartment());
	            dto.setSenderName(parcel.getSenderName());
	            String recipientlocode = parcel.getRecipientLocCode();
	            if (recipientlocode != null && !recipientlocode.trim().isEmpty()) {
	                recipientlocode = recipientlocode.trim();
	                String recLocName = mstLocationService.getLocNameByCode(recipientlocode);
	                dto.setRecipientLocCode(recLocName != null && !recLocName.trim().isEmpty()
	                    ? recLocName + " (" + recipientlocode + ")"
	                   // : "Unknown Location (" + recipientlocode + ")"
	                   : recipientlocode 
	                		);
	            } else {
	                dto.setRecipientLocCode("Unknown Location");
	            }
	          
	            dto.setRecipientDepartment(parcel.getRecipientDepartment());
	            dto.setRecipientName(parcel.getRecipientName());
	            dto.setCourierName(parcel.getCourierName());
	            dto.setWeight(parcel.getWeight());
	            dto.setUnit(parcel.getUnit());
	            dto.setDistance(parcel.getDistance());
	            dto.setRecordStatus(parcel.getRecordStatus());
	            dto.setCreatedBy(parcel.getCreatedBy());
	            dto.setCreatedDate(parcel.getCreatedDate());
	            dto.setLastUpdatedDate(parcel.getLastUpdatedDate());
	         
	        
	            
	    
	            return dto;
	        });
	    }
		 
		 

		
		 
		 public List<ParcelOutDto> findByDateRangeAndLocCode(LocalDate fromDate, LocalDate toDate, String locCode) {
			  List<TrnParcelOut> parcels= trnParcelOutRepository.findByCreatedDateBetweenAndSenderLocCodeOrderByOutTrackingIdDesc(fromDate, toDate, locCode);
			  
			  return parcels.stream().map(parcel -> {
		            ParcelOutDto dto = new ParcelOutDto();
		            dto.setSenderLocCode(parcel.getSenderLocCode());
		            dto.setOutTrackingId(parcel.getOutTrackingId());
		            dto.setConsignmentNumber(parcel.getConsignmentNumber());
		            dto.setConsignmentDate(parcel.getConsignmentDate());
		            dto.setSenderDepartment(parcel.getSenderDepartment());
		            dto.setSenderName(parcel.getSenderName());

		            
		            String recipientlocode = parcel.getRecipientLocCode();
		            if (recipientlocode != null && !recipientlocode.trim().isEmpty()) {
		                recipientlocode = recipientlocode.trim();
		                String recLocName = mstLocationService.getLocNameByCode(recipientlocode);
		                dto.setRecipientLocCode(recLocName != null && !recLocName.trim().isEmpty()
		                    ? recLocName + " (" + recipientlocode + ")"
		                   // : "Unknown Location (" + recipientlocode + ")"
		                   : recipientlocode 
		                		);
		            } else {
		                dto.setRecipientLocCode("Unknown Location");
		            }

		          
		            dto.setRecipientDepartment(parcel.getRecipientDepartment());
		            dto.setRecipientName(parcel.getRecipientName());
		            dto.setCourierName(parcel.getCourierName());
		            dto.setWeight(parcel.getWeight());
		            dto.setUnit(parcel.getUnit());
		            dto.setDistance(parcel.getDistance());
		            dto.setRecordStatus(parcel.getRecordStatus());
		            dto.setCreatedBy(parcel.getCreatedBy());
		            dto.setCreatedDate(parcel.getCreatedDate());
		            dto.setLastUpdatedDate(parcel.getLastUpdatedDate());

		         
		        
		            
		    
		            return dto;
		        }).collect(Collectors.toList());
		    }
		 
		 public List<ParcelOutDto> findByDateRangeAndSenderNameAndLocCode(LocalDate fromDate, LocalDate toDate, String senderName, String senderLocCode) {
			  List<TrnParcelOut> parcels= trnParcelOutRepository.findByCreatedDateBetweenAndSenderNameAndSenderLocCode(fromDate, toDate, senderName, senderLocCode);
		        
		        return parcels.stream().map(parcel -> {
		            ParcelOutDto dto = new ParcelOutDto();
		            dto.setSenderLocCode(parcel.getSenderLocCode());
		            dto.setOutTrackingId(parcel.getOutTrackingId());
		            dto.setConsignmentNumber(parcel.getConsignmentNumber());
		            dto.setConsignmentDate(parcel.getConsignmentDate());
		            dto.setSenderDepartment(parcel.getSenderDepartment());
		            dto.setSenderName(parcel.getSenderName());

		            
		            String recipientlocode = parcel.getRecipientLocCode();
		            if (recipientlocode != null && !recipientlocode.trim().isEmpty()) {
		                recipientlocode = recipientlocode.trim();
		                String recLocName = mstLocationService.getLocNameByCode(recipientlocode);
		                dto.setRecipientLocCode(recLocName != null && !recLocName.trim().isEmpty()
		                    ? recLocName + " (" + recipientlocode + ")"
		                   // : "Unknown Location (" + recipientlocode + ")"
		                   : recipientlocode 
		                		);
		            } else {
		                dto.setRecipientLocCode("Unknown Location");
		            }
		          
		            dto.setRecipientDepartment(parcel.getRecipientDepartment());
		            dto.setRecipientName(parcel.getRecipientName());
		            dto.setCourierName(parcel.getCourierName());
		            dto.setWeight(parcel.getWeight());
		            dto.setUnit(parcel.getUnit());
		            dto.setDistance(parcel.getDistance());
		            dto.setRecordStatus(parcel.getRecordStatus());
		            dto.setCreatedBy(parcel.getCreatedBy());
		            dto.setCreatedDate(parcel.getCreatedDate());
		            dto.setLastUpdatedDate(parcel.getLastUpdatedDate());

		         
		        
		            
		    
		            return dto;
		        }).collect(Collectors.toList());
		    }

		public Long fetchNextId() {
			return trnParcelOutRepository.fetchNextId();
		}
		

		
		
		public Page<ParcelOutDto> getParcelsByLocationCodeAndName(String locCode, String name, Pageable pageable) {
			  Page<TrnParcelOut> parcels= trnParcelOutRepository.findBySenderLocCodeAndSenderNameOrderByOutTrackingIdDesc(locCode, name, pageable);
		    
		    return parcels.map(parcel -> {
	            ParcelOutDto dto = new ParcelOutDto();
	            dto.setSenderLocCode(parcel.getSenderLocCode());
	            dto.setOutTrackingId(parcel.getOutTrackingId());
	            dto.setConsignmentNumber(parcel.getConsignmentNumber());
	            dto.setConsignmentDate(parcel.getConsignmentDate());
	            dto.setSenderDepartment(parcel.getSenderDepartment());
	            dto.setSenderName(parcel.getSenderName());
	            String recipientlocode = parcel.getRecipientLocCode();
	            if (recipientlocode != null && !recipientlocode.trim().isEmpty()) {
	                recipientlocode = recipientlocode.trim();
	                String recLocName = mstLocationService.getLocNameByCode(recipientlocode);
	                dto.setRecipientLocCode(recLocName != null && !recLocName.trim().isEmpty()
	                    ? recLocName + " (" + recipientlocode + ")"
	                   // : "Unknown Location (" + recipientlocode + ")"
	                   : recipientlocode 
	                		);
	            } else {
	                dto.setRecipientLocCode("Unknown Location");
	            }
	          
	            dto.setRecipientDepartment(parcel.getRecipientDepartment());
	            dto.setRecipientName(parcel.getRecipientName());
	            dto.setCourierName(parcel.getCourierName());
	            dto.setWeight(parcel.getWeight());
	            dto.setUnit(parcel.getUnit());
	            dto.setDistance(parcel.getDistance());
	            dto.setRecordStatus(parcel.getRecordStatus());
	            dto.setCreatedBy(parcel.getCreatedBy());
	            dto.setCreatedDate(parcel.getCreatedDate());
	            dto.setLastUpdatedDate(parcel.getLastUpdatedDate());

	         
	     
	    
	            return dto;
	        });
		}

		 public Page<ParcelOutDto> getTodaysParcelsByLocationCodeAndName(String locCode, String name, Pageable pageable) {
		        LocalDate today = LocalDate.now();
		        Page<TrnParcelOut> parcels= trnParcelOutRepository.findBySenderLocCodeAndSenderNameAndCreatedDateOrderByOutTrackingId(locCode, name, today, pageable);
		        
		        return parcels.map(parcel -> {
		            ParcelOutDto dto = new ParcelOutDto();
		            dto.setSenderLocCode(parcel.getSenderLocCode());
		            dto.setOutTrackingId(parcel.getOutTrackingId());
		            dto.setConsignmentNumber(parcel.getConsignmentNumber());
		            dto.setConsignmentDate(parcel.getConsignmentDate());
		            dto.setSenderDepartment(parcel.getSenderDepartment());
		            dto.setSenderName(parcel.getSenderName());
		            String recipientlocode = parcel.getRecipientLocCode();
		            if (recipientlocode != null && !recipientlocode.trim().isEmpty()) {
		                recipientlocode = recipientlocode.trim();
		                String recLocName = mstLocationService.getLocNameByCode(recipientlocode);
		                dto.setRecipientLocCode(recLocName != null && !recLocName.trim().isEmpty()
		                    ? recLocName + " (" + recipientlocode + ")"
		                   // : "Unknown Location (" + recipientlocode + ")"
		                   : recipientlocode 
		                		);
		            } else {
		                dto.setRecipientLocCode("Unknown Location");
		            }
		          
		            dto.setRecipientDepartment(parcel.getRecipientDepartment());
		            dto.setRecipientName(parcel.getRecipientName());
		            dto.setCourierName(parcel.getCourierName());
		            dto.setWeight(parcel.getWeight());
		            dto.setUnit(parcel.getUnit());
		            dto.setDistance(parcel.getDistance());
		            dto.setRecordStatus(parcel.getRecordStatus());
		            dto.setCreatedBy(parcel.getCreatedBy());
		            dto.setCreatedDate(parcel.getCreatedDate());
		            dto.setLastUpdatedDate(parcel.getLastUpdatedDate());

		         
		        
		            
		    
		            return dto;
		        });
		    }


		

		 
		 public ResponseEntity<ParcelOutResponse> getParcelOutByConsignmentNumber(String consignmentNumber) {

		        logger.info("Retrieving parcel out details for consignment number: {}", consignmentNumber);



		        try {

		            Optional<TrnParcelOut> parcelOutOpt = trnParcelOutRepository.findByConsignmentNumber(consignmentNumber);



		            if (parcelOutOpt.isEmpty()) {

		                logger.warn("No parcel found for consignment number: {}", consignmentNumber);

		                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

		            }



		            TrnParcelOut parcelOut = parcelOutOpt.get();

		            ParcelOutResponse response = new ParcelOutResponse();
		            
		            String senderLocCode=parcelOut.getSenderLocCode();
		            if (senderLocCode != null && !senderLocCode.trim().isEmpty()) {
		                senderLocCode = senderLocCode.trim();
		                String senderLocName = mstLocationService.getLocNameByCode(senderLocCode);
		                response.setSenderLocCode(senderLocName != null && !senderLocName.trim().isEmpty()
		                    ? senderLocName + " (" + senderLocCode + ")"
		                  //  : "Unknown Location (" + senderLocCode + ")"
		                    		 :senderLocCode
		                		);
		            } else {
		            	response.setSenderLocCode("Unknown Location");
		            }

//		            response.setSenderLocCode(parcelOut.getSenderLocCode());

		            response.setConsignmentDate(parcelOut.getConsignmentDate());


		            response.setSenderDepartment(parcelOut.getSenderDepartment());
		            response.setSenderName(parcelOut.getSenderName());
		            response.setRecipientDepartment(parcelOut.getRecipientDepartment());

		            response.setRecipientName(parcelOut.getRecipientName());

		            response.setCourierName(parcelOut.getCourierName());



		            logger.info("Parcel out details retrieved successfully for consignment number: {}", consignmentNumber);

		            return ResponseEntity.ok(response);



		        } catch (Exception e) {

		            logger.error("Error occurred while retrieving parcel out details for consignment number: {}", consignmentNumber, e);

		            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

		        }

		    }
		 

		 
		 public Double getDistance(String recipientLocCode, HttpServletRequest request) {
			    String token = jwtUtils.getJwtFromCookies(request);
			    String senderLocCode = jwtUtils.getLocCodeFromJwtToken(token);

			    List<TrnParcelOut> distances = trnParcelOutRepository.findAllDistances(senderLocCode, recipientLocCode);

			    if (distances.isEmpty()) {
			        logger.warn("No distances found for senderLocCode: {} and recipientLocCode: {}", senderLocCode, recipientLocCode);
			        return 0.0;
			    }

			    TrnParcelOut oldestEntry = distances.get(0); // The first one due to ORDER BY ASC
			    return oldestEntry.getDistance();
			}



	}

