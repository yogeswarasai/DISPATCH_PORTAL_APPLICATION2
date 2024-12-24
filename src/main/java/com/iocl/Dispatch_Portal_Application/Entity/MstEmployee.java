package com.iocl.Dispatch_Portal_Application.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "emp_db", schema = "public")
@Data
public class MstEmployee {

	 @Id
	    @Column(name = "emp_code")
	    private int empCode;

	    @Column(name = "emp_ini")
	    private String empIni;

	    @Column(name = "emp_name")
	    private String empName;

	    @Column(name = "designation")
	    private String designation;

	    @Column(name = "curr_comp_code")
	    private String currCompCode;

	    @Column(name = "curr_comp")
	    private String currComp;

	    @Column(name = "pa_code")
	    private String paCode;

	    @Column(name = "pa")
	    private String pa;

	    @Column(name = "psa_code")
	    private String psaCode;

	    @Column(name = "psa")
	    private String psa;

	    @Column(name = "loc_code")
	    private String locCode;

	    @Column(name = "loc_name")
	    private String locName;

	    @Column(name = "email_id")
	    private String emailId;

	    @Column(name = "emp_status_code")
	    private String empStatusCode;

	    @Column(name = "emp_status")
	    private String empStatus;

}
