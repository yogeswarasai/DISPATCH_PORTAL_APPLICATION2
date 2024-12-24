package com.iocl.Dispatch_Portal_Application.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;


@Entity
@Table(name = "mst_location")
@Data
public class MstLocation {

	
	    @Id
	    @Column(name = "loc_code", length = 6)
	    private String locCode;

	    @Column(name = "loc_name", length = 50)
	    private String locName;

	    @Column(name = "state_office_code", length = 6)
	    private String stateOfficeCode;

	    @Column(name = "region_office_code", length = 6)
	    private String regionOfficeCode;

	    @Column(name = "loc_address", length = 500)
	    private String locAddress;

	    @Column(name = "loc_state", length = 500)
	    private String locState;

	    @Column(name = "loc_pin", length = 10)
	    private String locPin;
	    
	  

}
