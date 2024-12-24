package com.iocl.Dispatch_Portal_Application.DTO;

import java.util.List;

import com.iocl.Dispatch_Portal_Application.Entity.MstCourierContractDiscount;
import com.iocl.Dispatch_Portal_Application.Entity.MstCourierContractRate;

import lombok.Data;

@Data
public class RatesAndDiscountsResponse {

	  private List<MstCourierContractDiscount> discounts;
	    private List<MstCourierContractRate> rates;

	    public RatesAndDiscountsResponse(List<MstCourierContractDiscount> discounts, List<MstCourierContractRate> rates) {
	        this.discounts = discounts;
	        this.rates = rates;
	    }
	    


	}

