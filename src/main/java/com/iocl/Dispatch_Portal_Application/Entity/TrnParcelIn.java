package com.iocl.Dispatch_Portal_Application.Entity;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.iocl.Dispatch_Portal_Application.composite_pk.TrnParcelInPK;

import lombok.Data;



@Entity
@Table(name = "trn_parcel_in")
@Data
@IdClass(TrnParcelInPK.class)

public class TrnParcelIn {

	
	@Id
    @Column(name = "recipient_loc_code", length = 6)
    private String recipientLocCode;

    @Id
    @Column(name = "in_tracking_id", precision = 9)
    private Long inTrackingId;

    @Column(name = "consignment_number", length = 30)
    private String consignmentNumber;

    @Column(name = "consignment_date")
    private Date consignmentDate;

    @Column(name = "received_date")
    private LocalDate receivedDate;

    @Column(name = "sender_loc_code", length = 6)
    private String senderLocCode;

    @Column(name = "sender_department", length = 50)
    private String senderDepartment;

    @Column(name = "sender_name", length = 50)
    private String senderName;

    @Column(name = "recipient_department", length = 50)
    private String recipientDepartment;

    @Column(name = "recipient_name", length = 50)
    private String recipientName;

    @Column(name = "courier_name", length = 50)
    private String courierName;

    @Column(name = "record_status", length = 1)
    private String recordStatus = "A";

    @Column(name = "created_by", length = 10)
    private String createdBy;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "last_updated_date")
    private LocalDateTime LastUpdatedDate;
   

}
