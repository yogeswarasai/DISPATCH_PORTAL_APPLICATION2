package com.iocl.Dispatch_Portal_Application.Repositaries;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iocl.Dispatch_Portal_Application.Entity.MstCourierContractDiscount;
import com.iocl.Dispatch_Portal_Application.composite_pk.MstCourierContractDiscountId;

public interface MstCourierContractDiscountRepository  extends JpaRepository<MstCourierContractDiscount,MstCourierContractDiscountId>{

	
	@Query("SELECT cd FROM MstCourierContractDiscount cd where cd.locCode=:locCode AND cd.courierContNo=:courierContNo")
	MstCourierContractDiscount findByLocCodeAndCourierContNo(@Param("locCode")  String locCode, @Param("courierContNo") String courierContNo);
	
	
	List<MstCourierContractDiscount> findByCourierContNo(String courierContNo);

}
