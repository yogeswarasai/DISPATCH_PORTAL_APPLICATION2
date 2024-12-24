package com.iocl.Dispatch_Portal_Application.Entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "units")
@Data
public class Unit {
	  @Id
	    private String unitId;

	    private String name;
}
