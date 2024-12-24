package com.iocl.Dispatch_Portal_Application.Entity;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.iocl.Dispatch_Portal_Application.composite_pk.TrnParcelOutPK;

import lombok.Data;


@Entity
@Table(name = "trn_parcel_out")
@Data
@IdClass(TrnParcelOutPK.class)

public class TrnParcelOut {

	@Id
	@Column(name = "sender_loc_code", length = 6, nullable = false)
    private String senderLocCode;
	
	@Id
    @Column(name = "out_tracking_id")
    private Long outTrackingId;

    @Column(name = "consignment_number", length = 30)
    private String consignmentNumber;

    @Column(name = "consignment_date")
    private Date consignmentDate;

    @Column(name = "sender_department", length = 50)
    private String senderDepartment;

    @Column(name = "sender_name", length = 50)
    private String senderName;

    @Column(name = "recipient_loc_code", length = 6)
    private String recipientLocCode;

    @Column(name = "recipient_department", length = 50)
    private String recipientDepartment;

    @Column(name = "recipient_name", length = 50)
    private String recipientName;
    
    @Column(name = "courier_name", length = 50)
    private String courierName;

    @Column(name = "weight")
    private Double weight;
    
    @Column(name = "Unit")
    private String unit;
    
    @Column(name = "record_status", length = 1)
    private String recordStatus = "A";

    @Column(name = "created_by", length = 10)
    private String createdBy;

    @Column(name = "created_date")
    private LocalDate createdDate;
    
    @Column(name = "last_updated_date")
    private LocalDateTime LastUpdatedDate;
    @Column(name="distance")
    private Double distance;

    

}
