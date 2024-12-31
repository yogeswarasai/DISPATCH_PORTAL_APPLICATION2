package com.iocl.Dispatch_Portal_Application.composite_pk;
import java.io.Serializable;


public class MstCourierContractDiscountId implements Serializable {

    private String locCode;
    private String courierContNo;
    private Double fromMonthlyAmt;
    private Double toMonthlyAmt;
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
	public Double getFromMonthlyAmt() {
		return fromMonthlyAmt;
	}
	public void setFromMonthlyAmt(Double fromMonthlyAmt) {
		this.fromMonthlyAmt = fromMonthlyAmt;
	}
	public Double getToMonthlyAmt() {
		return toMonthlyAmt;
	}
	public void setToMonthlyAmt(Double toMonthlyAmt) {
		this.toMonthlyAmt = toMonthlyAmt;
	}
	
	
	 // Default constructor
    public MstCourierContractDiscountId() {}

    // Constructor with all fields
    public MstCourierContractDiscountId(String locCode, String courierContNo, 
                                        Double fromMonthlyAmt, Double toMonthlyAmt) {
        this.locCode = locCode;
        this.courierContNo = courierContNo;
        this.fromMonthlyAmt = fromMonthlyAmt;
        this.toMonthlyAmt = toMonthlyAmt;
    }
	
    
    
}
