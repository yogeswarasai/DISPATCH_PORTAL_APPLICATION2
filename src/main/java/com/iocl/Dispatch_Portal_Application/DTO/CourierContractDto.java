package com.iocl.Dispatch_Portal_Application.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.iocl.Dispatch_Portal_Application.Entity.MstCourierContract;

import lombok.Data;

@Data
public class CourierContractDto {
    private String locCode;
    private String courierContNo;
    private LocalDate contractStartDate;
    private LocalDate contractEndDate;
    private String courierCode;
    private String status;
    private String createdBy;
    private LocalDate createdDate;
    private LocalDateTime lastUpdatedDate;

    public MstCourierContract toMstCourierContract() {
        MstCourierContract contract = new MstCourierContract();
        contract.setLocCode(this.locCode);
        contract.setCourierContNo(this.courierContNo);
        contract.setContractStartDate(this.contractStartDate);
        contract.setContractEndDate(this.contractEndDate);
        contract.setCourierCode(this.courierCode);
        contract.setStatus(this.status);
        contract.setCreatedBy(this.createdBy);
        contract.setCreatedDate(this.createdDate);
        contract.setLastUpdatedDate(this.lastUpdatedDate);
        return contract;
    }
}
