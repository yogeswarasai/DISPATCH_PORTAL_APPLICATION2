package com.iocl.Dispatch_Portal_Application.Repositaries;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iocl.Dispatch_Portal_Application.Entity.TrnParcelIn;
import com.iocl.Dispatch_Portal_Application.composite_pk.TrnParcelInPK;


@Repository
public interface TrnParcelInRepository extends JpaRepository<TrnParcelIn, TrnParcelInPK>{
    @Query("SELECT COALESCE(MAX(tpi.inTrackingId), 0) FROM TrnParcelIn tpi")
	Long fetchNextId();

    
    long countByRecipientLocCode(String locCode);

    long countParcelInByRecipientLocCode(String locCode);
    
    long countParcelInByReceivedDateAndRecipientLocCode(LocalDate date, String locCode);
	//count parcels by todays date
		 long countByReceivedDate(LocalDate date);
		    
		 @Query("SELECT t FROM TrnParcelIn t WHERE t.receivedDate BETWEEN :fromDate AND :toDate AND t.recipientLocCode = :locCode AND t.recordStatus = 'A' ORDER BY t.inTrackingId DESC")
		 List<TrnParcelIn> findByReceivedDateBetweenAndRecipientLocCodeOrderByInTrackingIdDesc(
		     @Param("fromDate") LocalDate fromDate, 
		     @Param("toDate") LocalDate toDate, 
		     @Param("locCode") String locCode
		 );

		    

		 @Query("SELECT t FROM TrnParcelIn t WHERE t.receivedDate BETWEEN :fromDate AND :toDate AND t.recipientName = :recipientName AND t.recipientLocCode = :recipientLocCode AND t.recordStatus = 'A'")
		 List<TrnParcelIn> findByReceivedDateBetweenAndRecipientNameAndRecipientLocCode(
		     @Param("fromDate") LocalDate fromDate, 
		     @Param("toDate") LocalDate toDate, 
		     @Param("recipientName") String recipientName, 
		     @Param("recipientLocCode") String recipientLocCode
		 );

		    
		    
		    Page<TrnParcelIn> findByRecipientLocCodeOrderByInTrackingIdDesc(String recipientLocCode , Pageable pageable);

    boolean existsByConsignmentNumber(String consignmentNumber);

	
    
    Page<TrnParcelIn> findByRecipientLocCodeAndRecipientNameOrderByInTrackingIdDesc(String recipientLocCode, String recipientName , Pageable pageable);   

    Page<TrnParcelIn> findByRecipientLocCodeAndRecipientNameAndCreatedDateOrderByInTrackingIdDesc(String locCode, String name, LocalDate receivedDate, Pageable pageable);

}
