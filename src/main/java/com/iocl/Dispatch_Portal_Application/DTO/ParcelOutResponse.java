package com.iocl.Dispatch_Portal_Application.DTO;
import java.sql.Date;

import lombok.Data;

@Data
public class ParcelOutResponse {

    private String senderLocCode;

    private Date consignmentDate;

    private String senderDepartment;
    
    private String senderName;

    private String recipientName;
    
    private String recipientDepartment;

    private String courierName;



   

}