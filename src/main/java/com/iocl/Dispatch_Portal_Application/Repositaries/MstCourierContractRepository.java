package com.iocl.Dispatch_Portal_Application.Repositaries;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.iocl.Dispatch_Portal_Application.Entity.MstCourierContract;
import com.iocl.Dispatch_Portal_Application.composite_pk.CourierContractId;
import com.iocl.Dispatch_Portal_Application.composite_pk.MstCourierContractPK;

@Repository
public interface MstCourierContractRepository extends JpaRepository<MstCourierContract, CourierContractId> {


	boolean existsById(CourierContractId contractId);

	Optional<MstCourierContract> findById(CourierContractId contractId);

	 @Transactional
	    void deleteByLocCodeAndCourierContNo(String locCode, String courierContNo);

	List<MstCourierContract> findByLocCode(String locCode);

	
	List<MstCourierContract> findByLocCodeAndCourierCode(String locCode, String courierCode);

	MstCourierContract findByLocCodeAndCourierContNo(String locCode, String courierContNo);

}

