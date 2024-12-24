package com.iocl.Dispatch_Portal_Application.composite_pk;

import java.io.Serializable;

public class MstCourierContractRateId implements Serializable {
    private String locCode;
    private String courierContNo;
    private Double fromWtGms;
    private Double toWtGms;
    private Double fromDistanceKm;
    private Double toDistanceKm;
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
	public Double getFromWtGms() {
		return fromWtGms;
	}
	public void setFromWtGms(Double fromWtGms) {
		this.fromWtGms = fromWtGms;
	}
	public Double getToWtGms() {
		return toWtGms;
	}
	public void setToWtGms(Double toWtGms) {
		this.toWtGms = toWtGms;
	}
	public Double getFromDistanceKm() {
		return fromDistanceKm;
	}
	public void setFromDistanceKm(Double fromDistanceKm) {
		this.fromDistanceKm = fromDistanceKm;
	}
	public Double getToDistanceKm() {
		return toDistanceKm;
	}
	public void setToDistanceKm(Double toDistanceKm) {
		this.toDistanceKm = toDistanceKm;
	}

    
	public MstCourierContractRateId() {}

    // Constructor with all fields
    public MstCourierContractRateId(String locCode, String courierContNo, Double fromWtGms, 
                                    Double toWtGms, Double fromDistanceKm, Double toDistanceKm) {
        this.locCode = locCode;
        this.courierContNo = courierContNo;
        this.fromWtGms = fromWtGms;
        this.toWtGms = toWtGms;
        this.fromDistanceKm = fromDistanceKm;
        this.toDistanceKm = toDistanceKm;
    }

    
  
}
