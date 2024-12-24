package com.iocl.Dispatch_Portal_Application.composite_pk;

import java.io.Serializable;

import lombok.Data;

@Data
public class MstUserPK implements Serializable{

	
	 private String locCode;
	    private String userId;

	    // Default constructor
	    public MstUserPK() {
	    }

	    // Constructor
	    public MstUserPK(String locCode, String userId) {
	        this.locCode = locCode;
	        this.userId = userId;
	    }

}
