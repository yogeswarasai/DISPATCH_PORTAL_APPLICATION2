package com.iocl.Dispatch_Portal_Application.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.iocl.Dispatch_Portal_Application.composite_pk.MstDepartmentPK;

import lombok.Data;


@Entity
@Table(name = "mst_department")
@Data
@IdClass(MstDepartmentPK.class)

public class MstDepartment {

	 @Id
	    @Column(name = "loc_code", length = 6)
	    private String locCode;

	    @Id
	    @Column(name = "dept_code", length = 6)
	    private String deptCode;

	    @Column(name = "dept_name", length = 40)
	    private String deptName;



}
