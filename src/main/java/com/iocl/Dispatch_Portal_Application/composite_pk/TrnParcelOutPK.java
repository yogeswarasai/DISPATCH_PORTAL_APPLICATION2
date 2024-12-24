package com.iocl.Dispatch_Portal_Application.composite_pk;

import java.io.Serializable;

import lombok.Data;

@Data

public class TrnParcelOutPK implements Serializable{

	
	private String senderLocCode;
    private Long outTrackingId;

    // Default constructor
    public TrnParcelOutPK() {
    }

    // Constructor
    public TrnParcelOutPK(String senderLocCode, Long outTrackingId2) {
        this.senderLocCode = senderLocCode;
        this.outTrackingId = outTrackingId2;
    }
}
