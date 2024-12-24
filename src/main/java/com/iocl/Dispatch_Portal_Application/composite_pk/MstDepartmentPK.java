package com.iocl.Dispatch_Portal_Application.composite_pk;

import java.io.Serializable;

import lombok.Data;

@Data
public class MstDepartmentPK implements Serializable{

	
	 private String locCode;
	    private String deptCode;

	    // Default constructor
	    public MstDepartmentPK() {
	    }

	    // Constructor
	    public MstDepartmentPK(String locCode, String deptCode) {
	        this.locCode = locCode;
	        this.deptCode = deptCode;
	    }
}
