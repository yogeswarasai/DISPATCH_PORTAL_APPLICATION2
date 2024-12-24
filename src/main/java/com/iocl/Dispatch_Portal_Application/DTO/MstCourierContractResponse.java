package com.iocl.Dispatch_Portal_Application.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;
@Data
public class MstCourierContractResponse {

    private String locCode;
    private String courierContNo;
    private LocalDate contractStartDate;
    private LocalDate contractEndDate;
    private String courierCode;
    private String status;
    private String createdBy;
    private LocalDate createdDate;
    private LocalDateTime lastUpdatedDate;

}
