package com.iocl.Dispatch_Portal_Application.Repositaries;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iocl.Dispatch_Portal_Application.Entity.TrnParcelOut;
import com.iocl.Dispatch_Portal_Application.composite_pk.TrnParcelOutPK;


public interface TrnParcelOutRepository extends JpaRepository<TrnParcelOut, TrnParcelOutPK>{

	
	   long countBySenderLocCode(String locCode);
	   
	    long countParcelOutBySenderLocCode(String locCode);
	   
	   
	    long countParcelOutByCreatedDateAndSenderLocCode(LocalDate date, String locCode);
	
	    
	    Page<TrnParcelOut> findBySenderLocCodeOrderByOutTrackingIdDesc(String senderLocCode, Pageable pageable); 


	
	//count parcels by today date
		Long countByCreatedDate(LocalDate createdDate);
	
		
		@Query("SELECT t FROM TrnParcelOut t WHERE t.createdDate BETWEEN :fromDate AND :toDate AND t.senderLocCode = :locCode AND t.recordStatus = 'A' ORDER BY t.outTrackingId DESC")
		List<TrnParcelOut> findByCreatedDateBetweenAndSenderLocCodeOrderByOutTrackingIdDesc(
		    @Param("fromDate") LocalDate fromDate, 
		    @Param("toDate") LocalDate toDate, 
		    @Param("locCode") String locCode
		);

	    
//	    List<TrnParcelOut> findByCreatedDateBetweenAndSenderNameAndSenderLocCode(
//	            LocalDate fromDate, LocalDate toDate, String senderName, String senderLocCode);

		
		@Query("SELECT t FROM TrnParcelOut t WHERE t.createdDate BETWEEN :fromDate AND :toDate AND t.senderName = :senderName AND t.senderLocCode = :senderLocCode AND t.recordStatus = 'A'")
		List<TrnParcelOut> findByCreatedDateBetweenAndSenderNameAndSenderLocCode(
		    @Param("fromDate") LocalDate fromDate, 
		    @Param("toDate") LocalDate toDate, 
		    @Param("senderName") String senderName, 
		    @Param("senderLocCode") String senderLocCode
		);

		
	    boolean existsByConsignmentNumber(String consignmentNumber);

	    
	    @Query("select max(tpo.outTrackingId) from TrnParcelOut tpo")
		Long fetchNextId();


	    
        Page<TrnParcelOut> findBySenderLocCodeAndSenderNameOrderByOutTrackingIdDesc(String locCode, String name, Pageable pageable);

	    Page<TrnParcelOut> findBySenderLocCodeAndSenderNameAndCreatedDateOrderByOutTrackingId(String locCode, String name, LocalDate sentDate, Pageable pageable);

		Optional<TrnParcelOut> findByConsignmentNumber(String consignmentNumber);


		
		
		@Query(value = "SELECT distance FROM trn_parcel_out WHERE sender_loc_code = :senderLocCode AND LOWER(recipient_loc_code) = LOWER(:recipientLocCode)", nativeQuery = true)
		Double findDistanceBySenderAndRecipientNative(@Param("senderLocCode") String senderLocCode, @Param("recipientLocCode") String recipientLocCode);


		
		@Query("SELECT t FROM TrnParcelOut t WHERE t.senderLocCode = :senderLocCode AND t.recipientLocCode = :recipientLocCode ORDER BY t.createdDate ASC")
		List<TrnParcelOut> findAllDistances(@Param("senderLocCode") String senderLocCode, @Param("recipientLocCode") String recipientLocCode);



}
  