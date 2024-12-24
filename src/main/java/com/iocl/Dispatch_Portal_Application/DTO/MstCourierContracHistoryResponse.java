package com.iocl.Dispatch_Portal_Application.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
@Data
public class MstCourierContracHistoryResponse {
	
	private String courierContNo;
    private LocalDate contractStartDate;
    private LocalDate contractEndDate;
    private String courierCode;
    private String status ;
    private String createdBy;
    private LocalDate createdDate;
    private LocalDateTime lastUpdatedDate;
    private List<MstCourierContractDiscountDTO> discounts;
    private List<MstCourierContractRateDto> rates;
}
