package com.iocl.Dispatch_Portal_Application.Repositaries;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iocl.Dispatch_Portal_Application.Entity.MstDepartment;
import com.iocl.Dispatch_Portal_Application.composite_pk.MstDepartmentPK;


@Repository
public interface MstDepartmentRepository extends JpaRepository<MstDepartment, MstDepartmentPK>{
	
	List<MstDepartment> findByLocCode(String locCode);

	@Query("SELECT d.deptName FROM MstDepartment d JOIN MstLocation l ON d.locCode = l.locCode WHERE l.locName = :locName")
    List<String> findDepartmentsByLocationName(@Param("locName") String locName);
	
}
