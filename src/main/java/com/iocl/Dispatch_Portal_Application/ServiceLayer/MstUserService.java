package com.iocl.Dispatch_Portal_Application.ServiceLayer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.iocl.Dispatch_Portal_Application.DTO.MstUserDTO;
import com.iocl.Dispatch_Portal_Application.Entity.MstUser;
import com.iocl.Dispatch_Portal_Application.Repositaries.MstLocationRepository;
import com.iocl.Dispatch_Portal_Application.Repositaries.MstUserRepository;
import com.iocl.Dispatch_Portal_Application.Security.JwtUtils;
import com.iocl.Dispatch_Portal_Application.composite_pk.MstUserPK;
import com.iocl.Dispatch_Portal_Application.modal.StatusCodeModal;

@Service
public class MstUserService {

	
	 @Autowired
	    private MstUserRepository mstUserRepository;
	 
	 @Autowired
	 private MstLocationService mstLocationService;
	 @Autowired
	    private JwtUtils jwtUtils;
	 
	 @Autowired
	 private HttpServletRequest request;
	 
	 @Autowired
	 private MstLocationRepository mstlocationRepositary;
	  
	
	    public List<MstUser> findAll() {
	        return mstUserRepository.findAll();
	    }
	
	    
	    public Page<MstUserDTO> findAll(int page, int size) {
//	        Pageable pageable = PageRequest.of(page, size);
	        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));

	        Page<MstUser> users= mstUserRepository.findAll(pageable);
	        return users.map(user -> {
	            MstUserDTO dto = new MstUserDTO();
	            dto.setLocCode(user.getLocCode());
	            dto.setUserId(user.getUserId());
	            dto.setUserName(user.getUserName());
	            dto.setMobileNumber(user.getMobileNumber());
	            dto.setRoleId(user.getRoleId());
	            

	            // Fetch locName using the locCode and format it
	            String locCode = user.getLocCode();
	            if (locCode != null && !locCode.trim().isEmpty()) {
	                String locName = mstLocationService.getLocNameByCode(locCode);
	                if (locName != null && !locName.trim().isEmpty()) {
	                    dto.setLocCode(locName + " (" + locCode + ")");
	                }
	            }
	            dto.setStatus(user.getStatus());
	            dto.setCreatedBy(user.getCreatedBy());
	            dto.setCreatedDate(user.getCreatedDate());
	            dto.setLastUpdatedDate(user.getLastUpdatedDate());

	            return dto;
	        });
	    }
	
	    public ResponseEntity<?> createUser(MstUserDTO mstUserdto, HttpServletRequest request) {
	        MstUser mstUser = mstUserdto.tomstUser();
	        String token = jwtUtils.getJwtFromCookies(request);

	        // Validate and extract information from the JWT token
	        String username = jwtUtils.getUserNameFromJwtToken(token);
	        String role = jwtUtils.getRoleFromJwtToken(token);

	        if (username == null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid JWT token");
	        }
	        
	        if ("LOC_ADMIN".equalsIgnoreCase(role)) {
	            // Set locCode from the logged-in user's token for LOC_ADMIN role
	            String userLocCode = jwtUtils.getLocCodeFromJwtToken(token); // Assuming locCode is stored in JWT
	            mstUser.setLocCode(userLocCode); // Set locCode to logged-in user's locCode
	            mstUser.setCreatedBy(username);
		        mstUser.setCreatedDate(LocalDate.now());
	        }

	        mstUser.setCreatedBy(username);
	        mstUser.setCreatedDate(LocalDate.now());

	        if (!mstlocationRepositary.existsByLocCode(mstUser.getLocCode())) {
	        	 StatusCodeModal statusCodeModal = new StatusCodeModal();
		            statusCodeModal.setStatus_code(HttpStatus.BAD_REQUEST.value());
		            statusCodeModal.setStatus("loc Code not exists: " + mstUserdto.getLocCode());
		            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCodeModal);	        }

	        if (mstUserRepository.existsByMobileNumber(mstUser.getMobileNumber())) {
	        	StatusCodeModal statusCodeModal = new StatusCodeModal();
	            statusCodeModal.setStatus_code(HttpStatus.BAD_REQUEST.value());
	            statusCodeModal.setStatus("Mobile number already exists: " + mstUser.getMobileNumber());
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCodeModal);	        }

	        MstUser createUser = mstUserRepository.save(mstUser);
	        StatusCodeModal statusCodeModal = new StatusCodeModal();
	        if (createUser != null) {
	            statusCodeModal.setStatus_code(HttpStatus.CREATED.value());
	            statusCodeModal.setStatus("User inserted successfully.");
	            return ResponseEntity.status(HttpStatus.CREATED).body(statusCodeModal);
	        } else {
	            statusCodeModal.setStatus_code(HttpStatus.BAD_REQUEST.value());
	            statusCodeModal.setStatus("Failed to create user.");
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCodeModal);
	        }
	    }

	    
	    
	    
	    public ResponseEntity<StatusCodeModal> updateUser(String locCode, String empCode, MstUser mstUser) {
	        MstUserPK id = new MstUserPK(locCode, empCode);
	        Optional<MstUser> user =mstUserRepository.findById(id);
	        StatusCodeModal statusCodeModal = new StatusCodeModal();

	        if (!user.isPresent()) {
	            statusCodeModal.setStatus_code(HttpStatus.NOT_FOUND.value());
	            statusCodeModal.setStatus("User not found.");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(statusCodeModal);
	        }

	        MstUser userUpdateWith = user.get();
	        mstUser.setLocCode(locCode.trim());
	        mstUser.setUserId(empCode);
	        mstUser.setPassword(userUpdateWith.getPassword());
	        mstUser.setCreatedBy(userUpdateWith.getCreatedBy());
	        mstUser.setStatus(userUpdateWith.getStatus());
	        mstUser.setCreatedDate(userUpdateWith.getCreatedDate());
	        mstUser.setLastUpdatedDate(LocalDateTime.now());
	        mstUser.setRoleId(userUpdateWith.getRoleId());

	        MstUser updatedUser = mstUserRepository.save(mstUser);
	        if (updatedUser != null) {
	            statusCodeModal.setStatus_code(HttpStatus.OK.value());
	            statusCodeModal.setStatus("User updated successfully with empCode : " + updatedUser.getUserId());
	            return ResponseEntity.ok(statusCodeModal);
	        } else {
	            statusCodeModal.setStatus_code(HttpStatus.BAD_REQUEST.value());
	            statusCodeModal.setStatus("Failed to update user.");
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCodeModal);
	        }
	    }

	    
	    
	    
	    public ResponseEntity<StatusCodeModal> deleteUser(String locCode, String empCode) {
	        MstUserPK id = new MstUserPK(locCode, empCode);
	        Optional<MstUser> userOptional = mstUserRepository.findById(id);
	        StatusCodeModal statusCodeModal = new StatusCodeModal();

	        if (userOptional.isPresent()) {
	            MstUser mstUser = userOptional.get();
	            mstUser.setStatus("D");
	            mstUser.setLocCode(locCode);
	            mstUser.setUserId(empCode);

	            MstUser deletedUser = mstUserRepository.save(mstUser);
	            if (deletedUser != null) {
	                statusCodeModal.setStatus_code(HttpStatus.OK.value());
	                statusCodeModal.setStatus("User deleted successfully with empCode :" + deletedUser.getUserId());
	                return ResponseEntity.ok(statusCodeModal);
	            } else {
	                statusCodeModal.setStatus_code(HttpStatus.BAD_REQUEST.value());
	                statusCodeModal.setStatus("Failed to delete user.");
	                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCodeModal);
	            }
	        }

	        statusCodeModal.setStatus_code(HttpStatus.NO_CONTENT.value());
	        statusCodeModal.setStatus("User not found with empCode :" + empCode);
	        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(statusCodeModal);
	    }

	    public ResponseEntity<StatusCodeModal> delete(String locCode, String empCode) {
	        MstUserPK id = new MstUserPK(locCode, empCode);
	        Optional<MstUser> userOptional =mstUserRepository.findById(id);
	        StatusCodeModal statusCodeModal = new StatusCodeModal();

	        if (userOptional.isPresent()) {
	            mstUserRepository.deleteById(id);
	            statusCodeModal.setStatus_code(HttpStatus.OK.value());
	            statusCodeModal.setStatus("User deleted successfully with empCode: " + empCode);
	            return ResponseEntity.ok(statusCodeModal);
	        } else {
	            statusCodeModal.setStatus_code(HttpStatus.NOT_FOUND.value());
	            statusCodeModal.setStatus("User not found with empCode: " + empCode);
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(statusCodeModal);
	        }
	    }
	   

		public Optional<MstUser> getUserByUserId(String userId) {
				return mstUserRepository.findByUserId(userId);
		}

		public Optional<MstUser> findByUserId(String username) {
	        return mstUserRepository.findByUserId(username);

		}
		
		 public List<String> getAdminsAddedRoles() {
		        String token = jwtUtils.getJwtFromCookies(request);
		        System.out.println("Extracted token: " + token); // Debugging log

		        String userRole = jwtUtils.getRoleFromJwtToken(token);
		        System.out.println("Extracted user role: " + userRole); // Debugging log

		        List<String> roles = new ArrayList<>();

		        if ("admin".equalsIgnoreCase(userRole)) {
		            roles.add("LOC_ADMIN");
		            roles.add("DISPATCH");
		        } else if ("loc_admin".equalsIgnoreCase(userRole)) {
		            roles.add("DISPATCH");
		        }

		        System.out.println("Roles to be returned: " + roles); // Debugging log
		        return roles;
		    }
		 
		 
		 public List<MstUser> getDispatchUsersByLocCode(String locCode) {
			    return mstUserRepository.findByRoleIdAndLocCode("DISPATCH", locCode);
			}



	}

