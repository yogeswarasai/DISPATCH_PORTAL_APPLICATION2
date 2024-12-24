package com.iocl.Dispatch_Portal_Application.composite_pk;

import java.io.Serializable;

import javax.persistence.Column;

import lombok.Data;


public class MstCourierContractPK implements Serializable {

	 
	    private String locCode;
	    private String courierContNo;
		
		public String getLocCode() {
			return locCode;
		}
		public void setLocCode(String locCode) {
			this.locCode = locCode;
		}
		public String getCourierContNo() {
			return courierContNo;
		}
		public void setCourierContNo(String courierContNo) {
			this.courierContNo = courierContNo;
		}
    
   

  
}
