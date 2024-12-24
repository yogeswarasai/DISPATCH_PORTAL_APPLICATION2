package com.iocl.Dispatch_Portal_Application.Entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.iocl.Dispatch_Portal_Application.composite_pk.MstCourierContractDiscountId;

@Entity
@IdClass(MstCourierContractDiscountId.class)
@Table(name = "mst_courier_contract_discount")
public class MstCourierContractDiscount {

    @Id
    @Column(name = "loc_code", length = 6, nullable = false)
    private String locCode;

    @Id
    @Column(name = "courier_cont_no", length = 40, nullable = false)
    private String courierContNo;

    @Id
    @Column(name = "from_monthly_amt", nullable = false)
    private double fromMonthlyAmt;

    @Id
    @Column(name = "to_monthly_amt", nullable = false)
    private double toMonthlyAmt;

    @Column(name = "discount_percentage", nullable = false)
    private double discountPercentage;

    @Column(name = "status", length = 1, nullable = false)
    private String status;

    @Column(name = "created_by", length = 10, nullable = false)
    private String createdBy;

    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate;
    

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "loc_code", referencedColumnName = "loc_code", insertable = false, updatable = false),
        @JoinColumn(name = "courier_cont_no", referencedColumnName = "courier_cont_no", insertable = false, updatable = false)
    })
    @JsonBackReference // Mark as the back reference
    private MstCourierContract courierContract;


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


	public double getFromMonthlyAmt() {
		return fromMonthlyAmt;
	}


	public void setFromMonthlyAmt(double fromMonthlyAmt) {
		this.fromMonthlyAmt = fromMonthlyAmt;
	}


	public double getToMonthlyAmt() {
		return toMonthlyAmt;
	}


	public void setToMonthlyAmt(double toMonthlyAmt) {
		this.toMonthlyAmt = toMonthlyAmt;
	}


	public double getDiscountPercentage() {
		return discountPercentage;
	}


	public void setDiscountPercentage(double discountPercentage) {
		this.discountPercentage = discountPercentage;
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


	public MstCourierContract getCourierContract() {
		return courierContract;
	}


	public void setCourierContract(MstCourierContract courierContract) {
		this.courierContract = courierContract;
	}


	



	
}
