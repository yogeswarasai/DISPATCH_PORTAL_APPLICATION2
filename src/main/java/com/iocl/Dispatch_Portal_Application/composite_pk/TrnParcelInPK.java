package com.iocl.Dispatch_Portal_Application.composite_pk;

import java.io.Serializable;

import lombok.Data;

@Data
public class TrnParcelInPK implements Serializable{

	
	private String recipientLocCode;
    private Long inTrackingId;

    // Default constructor
    public TrnParcelInPK() {
    }

    // Constructor
    public TrnParcelInPK(String recipientLocCode, Long inTrackingId) {
        this.recipientLocCode = recipientLocCode;
        this.inTrackingId = inTrackingId;
    }
}
