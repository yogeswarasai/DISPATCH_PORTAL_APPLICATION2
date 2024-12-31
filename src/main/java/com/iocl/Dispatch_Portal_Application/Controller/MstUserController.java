package com.iocl.Dispatch_Portal_Application.Controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import com.iocl.Dispatch_Portal_Application.DTO.MstUserDTO;
import com.iocl.Dispatch_Portal_Application.Entity.MstUser;
import com.iocl.Dispatch_Portal_Application.Repositaries.MstUserRepository;
import com.iocl.Dispatch_Portal_Application.Security.JwtUtils;
import com.iocl.Dispatch_Portal_Application.ServiceLayer.EmployeeService;
import com.iocl.Dispatch_Portal_Application.ServiceLayer.MstUserService;
import com.iocl.Dispatch_Portal_Application.modal.StatusCodeModal;
@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:4200")
public class MstUserController {

	 @Autowired
	    private MstUserService mstUserService;
	 
	 @Autowired
	 private MstUserRepository mstUserRepository;
	 @Autowired
	    private  EmployeeService employeeService;
	 
	 @Autowired
	 private JwtUtils jwtUtils;
	
	//    private static final Logger logger = LoggerFactory.getLogger(MstUserController.class);
	


	    @GetMapping
	    public Page<MstUserDTO> getAllUsers(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size
	    ) {
	        return mstUserService.findAll(page, size);
	    }
	    @GetMapping("/{userId}")
	    public ResponseEntity<Optional<MstUser>> getUserByUserId(@PathVariable String userId) {
	        Optional<MstUser> users = mstUserService.getUserByUserId(userId);
	        return ResponseEntity.ok(users);
	    }

	    @GetMapping("/dispatch")
	    public ResponseEntity<List<MstUser>> getDispatchUsers(HttpServletRequest request) {
	        // Extract the JWT token from the cookies
	        String token = jwtUtils.getJwtFromCookies(request);
	        System.out.println("Extracted token: " + token); // Debugging log

	        // Extract the locCode from the JWT token
	        String locCode = jwtUtils.getLocCodeFromJwtToken(token);
	        System.out.println("Extracted locCode: " + locCode); // Debugging log

	        // Fetch the dispatch users based on the locCode
	        List<MstUser> dispatchUsers = mstUserService.getDispatchUsersByLocCode(locCode);

	        // Return the response
	        return ResponseEntity.ok(dispatchUsers);
	    }

	    
	    @GetMapping("/check-mobile")
	    public ResponseEntity<Boolean> checkMobileNumberExists(@RequestParam Long mobileNumber) {
	        boolean exists = mstUserRepository.existsByMobileNumber(mobileNumber);
	        return ResponseEntity.ok(exists);
	    }




	    @PostMapping
	    public ResponseEntity<?> createUser(@RequestBody MstUserDTO mstUserdto, HttpServletRequest request) {
	        return mstUserService.createUser(mstUserdto, request);
	    }

	    @PutMapping("/{locCode}/{empCode}")
	    public ResponseEntity<StatusCodeModal> updateUser(@PathVariable String locCode, @PathVariable String empCode, @RequestBody MstUser mstUser) {
	        return mstUserService.updateUser(locCode, empCode, mstUser);
	    }


	    
	    @DeleteMapping("/{locCode}/{empCode}")
	    public ResponseEntity<StatusCodeModal> delete(@PathVariable String locCode, @PathVariable String empCode) {
	        return mstUserService.delete(locCode, empCode);

	    }
	    
	   
    }


