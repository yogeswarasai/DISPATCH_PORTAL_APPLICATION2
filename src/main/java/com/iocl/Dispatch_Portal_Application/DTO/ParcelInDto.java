package com.iocl.Dispatch_Portal_Application.DTO;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.iocl.Dispatch_Portal_Application.Entity.TrnParcelIn;

import lombok.Data;

@Data
public class ParcelInDto {


    private String recipientLocCode;
    private Long inTrackingId;
    private String consignmentNumber;
    private Date consignmentDate;
    private LocalDate receivedDate;
    private String senderLocCode;
    private String senderDepartment;
    private String senderName;
    private String recipientDepartment;
    private String recipientName;
    private String courierName;
    private String recordStatus = "A";
    private String createdBy;
    private LocalDate createdDate;
    private LocalDateTime LastUpdatedDate;

    private String formattedSenderLocation;  // For frontend display

    
    // Method to set formatted sender location
    public void setFormattedSenderLocation(String formattedSenderLocation) {
        this.formattedSenderLocation = formattedSenderLocation;
    }
//    

    public TrnParcelIn toTrnParcelIn() {
        TrnParcelIn parcelIn = new TrnParcelIn();
        parcelIn.setRecipientLocCode(this.recipientLocCode);
        parcelIn.setInTrackingId(this.inTrackingId);
        parcelIn.setConsignmentNumber(this.consignmentNumber);
        parcelIn.setConsignmentDate(this.consignmentDate);
        parcelIn.setReceivedDate(this.receivedDate);
        parcelIn.setSenderLocCode(this.senderLocCode);
        parcelIn.setSenderDepartment(this.senderDepartment);
        parcelIn.setSenderName(this.senderName);
        parcelIn.setRecipientDepartment(this.recipientDepartment);
        parcelIn.setRecipientName(this.recipientName);
        parcelIn.setCourierName(this.courierName);
        parcelIn.setRecordStatus(this.recordStatus);
        parcelIn.setCreatedBy(this.createdBy);
        parcelIn.setCreatedDate(this.createdDate);
        parcelIn.setLastUpdatedDate(this.LastUpdatedDate);
        
        System.out.println(parcelIn);
        return parcelIn;
    }
    
   
    
}
