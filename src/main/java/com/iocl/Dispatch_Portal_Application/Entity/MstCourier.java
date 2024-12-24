package com.iocl.Dispatch_Portal_Application.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.iocl.Dispatch_Portal_Application.composite_pk.MstCourierPK;

import lombok.Data;


@Entity
@Table(name = "mst_courier")
@Data
@IdClass(MstCourierPK.class)
public class MstCourier {
	
	 @Id
     @Column(name = "loc_code", length = 6, nullable = false)
    private String locCode;

	@Id
    @Column(name = "courier_code", length = 10)
    private String courierCode;

    @Column(name = "courier_name", length = 40)
    private String courierName;
}
