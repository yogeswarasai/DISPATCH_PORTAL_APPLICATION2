package com.iocl.Dispatch_Portal_Application.DTO;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.iocl.Dispatch_Portal_Application.Entity.TrnParcelOut;

import lombok.Data;

@Data
public class ParcelOutDto {

    private String senderLocCode;
    private Long outTrackingId;
    private String consignmentNumber;
    private Date consignmentDate;
    private String senderDepartment;
    private String senderName;
    private String recipientLocCode;
    private String recipientDepartment;
    private String recipientName;
    private String courierName;
    private Double weight;
    private String unit;
    private String recordStatus = "A";
    private String createdBy;
    private LocalDate createdDate;
    private LocalDateTime LastUpdatedDate;
    private Double distance;
    
    private String formattedrecLocation;  // For frontend display

    
    
    public void setformattedrecLocation(String formattedrecLocation) {
        this.formattedrecLocation = formattedrecLocation;
    }
    
    public TrnParcelOut toTrnParcelOut() {
        TrnParcelOut parcelOut = new TrnParcelOut();
        parcelOut.setSenderLocCode(this.senderLocCode);
        parcelOut.setOutTrackingId(this.outTrackingId);
        parcelOut.setConsignmentNumber(this.consignmentNumber);
        parcelOut.setConsignmentDate(this.consignmentDate);
        parcelOut.setSenderDepartment(this.senderDepartment);
        parcelOut.setSenderName(this.senderName);
        parcelOut.setRecipientLocCode(this.recipientLocCode);
        parcelOut.setRecipientDepartment(this.recipientDepartment);
        parcelOut.setRecipientName(this.recipientName);
        parcelOut.setCourierName(this.courierName);
        parcelOut.setWeight(this.weight);
        parcelOut.setUnit(this.unit);
        parcelOut.setRecordStatus(this.recordStatus);
        parcelOut.setCreatedBy(this.createdBy);
        parcelOut.setCreatedDate(this.createdDate);
        parcelOut.setLastUpdatedDate(this.LastUpdatedDate);
        parcelOut.setDistance(this.distance);
        return parcelOut;
    }
}
