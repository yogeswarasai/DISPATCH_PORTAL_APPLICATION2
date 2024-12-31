package com.iocl.Dispatch_Portal_Application.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.iocl.Dispatch_Portal_Application.composite_pk.CourierContractId;

@Entity
@Table(name = "mst_courier_contract")
@IdClass(CourierContractId.class)
public class MstCourierContract {

    @Id
    @Column(name = "loc_code", length = 6, nullable = false)
    private String locCode;

    @Id
    @Column(name = "courier_cont_no", length = 40, nullable = false)
    private String courierContNo;

    @Column(name = "contract_start_date", nullable = false)
    private LocalDate contractStartDate;

    @Column(name = "contract_end_date", nullable = false)
    private LocalDate contractEndDate;

    @Column(name = "courier_code", length = 10, nullable = false)
   private String courierCode;

    @Column(name = "status", length = 1, nullable = false, columnDefinition = "bpchar default 'A'")
    private String status = "A";

    @Column(name = "created_by", length = 10, nullable = false)
    private String createdBy;

    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate;
    
    @Column(name = "last_updated_date")
    private LocalDateTime lastUpdatedDate;
    
   
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

	public LocalDate getContractStartDate() {
		return contractStartDate;
	}

	public void setContractStartDate(LocalDate contractStartDate) {
		this.contractStartDate = contractStartDate;
	}

	public LocalDate getContractEndDate() {
		return contractEndDate;
	}

	public void setContractEndDate(LocalDate contractEndDate) {
		this.contractEndDate = contractEndDate;
	}

	public String getCourierCode() {
		return courierCode;
	}

	public void setCourierCode(String courierCode) {
		this.courierCode = courierCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDate getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}


    
}
