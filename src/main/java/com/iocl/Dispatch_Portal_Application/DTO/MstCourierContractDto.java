package com.iocl.Dispatch_Portal_Application.DTO;

import java.util.List;

public class MstCourierContractDto {
   private String courierContNo;

   private List<MstCourierContractRateDto> courierRates;
   private List<MstCourierContractDiscountDTO> courierDiscounts;


public String getCourierContNo() {
	return courierContNo;
}
public void setCourierContNo(String courierContNo) {
	this.courierContNo = courierContNo;
}

public void setCourierDiscount(List<MstCourierContractDiscountDTO> courierDiscount) {
	this.courierDiscounts = courierDiscount;
}
public List<MstCourierContractRateDto> getCourierRates() {
	return courierRates;
}
public List<MstCourierContractDiscountDTO> getCourierDiscounts() {
	return courierDiscounts;
}
public void setCourierDiscounts(List<MstCourierContractDiscountDTO> courierDiscounts) {
	this.courierDiscounts = courierDiscounts;
}
public void setCourierRates(List<MstCourierContractRateDto> courierRates) {
	this.courierRates = courierRates;
}

    
}
