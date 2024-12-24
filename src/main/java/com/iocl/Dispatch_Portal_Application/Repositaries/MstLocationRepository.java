package com.iocl.Dispatch_Portal_Application.Repositaries;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iocl.Dispatch_Portal_Application.Entity.MstLocation;

@Repository
public interface MstLocationRepository extends JpaRepository<MstLocation, String>{

	boolean existsByLocCode(String locCode);
	
	 @Query("SELECT l.locCode FROM MstLocation l")
	    List<String> findAllLocationCodes();
	 @Query("SELECT l.locName FROM MstLocation l")
	List<String> findAllLocationNames();

	 @Query("SELECT l.locName FROM MstLocation l WHERE l.locCode = :locCode")
	String findByLocCode(@Param("locCode") String locCode);

	 

}
