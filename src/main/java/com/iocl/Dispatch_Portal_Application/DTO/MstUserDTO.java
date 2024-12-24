package com.iocl.Dispatch_Portal_Application.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.iocl.Dispatch_Portal_Application.Entity.MstUser;

import lombok.Data;

@Data
public class MstUserDTO {

	 private String locCode;
	    private String userId;
	    private String userName;
	    private Long mobileNumber;
	    private String password;
	    private String roleId;
	    private String status = "A";
	    private String createdBy;
	    private LocalDate createdDate;
	    private LocalDateTime lastUpdatedDate;  
	    
	    
	     public MstUser tomstUser()
	     {
	    	 MstUser mstuser=new MstUser();
	    	 mstuser.setLocCode(this.locCode);
	    	 mstuser.setUserId(this.userId);
	    	 mstuser.setUserName(this.userName);
	    	 mstuser.setMobileNumber(this.mobileNumber);
	    	 mstuser.setPassword(this.password);
	    	 mstuser.setRoleId(this.roleId);
	    	 mstuser.setStatus(this.status);
	    	 mstuser.setCreatedBy(this.createdBy);
	    	 mstuser.setCreatedDate(this.createdDate);
	    	 mstuser.setLastUpdatedDate(this.lastUpdatedDate);
	    	 return mstuser;
	     }
}
