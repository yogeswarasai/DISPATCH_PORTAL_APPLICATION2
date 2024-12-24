package com.iocl.Dispatch_Portal_Application.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;


@Entity
@Table(name = "ref_sequence")
@Data
public class RefSequence {

	 @Id
	    @Column(name = "loc_code", length = 6)
	    private String locCode;

	    @Column(name = "in_sequence_no")
	
	    private Integer inSequenceNo;

	    @Column(name = "out_sequence_no")
	    private Integer outSequenceNo;


}
