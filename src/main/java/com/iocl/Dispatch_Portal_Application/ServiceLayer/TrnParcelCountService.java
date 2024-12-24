package com.iocl.Dispatch_Portal_Application.ServiceLayer;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iocl.Dispatch_Portal_Application.Repositaries.TrnParcelInRepository;
import com.iocl.Dispatch_Portal_Application.Repositaries.TrnParcelOutRepository;

@Service
public class TrnParcelCountService {

	 @Autowired
	    private TrnParcelOutRepository parcelOutRepository;

	    @Autowired
	    private TrnParcelInRepository parcelInRepository;

	    public long totalCountParcelOut() {
	        return parcelOutRepository.count();
	    }

	    public long totalCountParcelIn() {
	        return parcelInRepository.count();
	    }
	    
	    
	    public long totalCountParcelOutByLocCode(String locCode) {
	        return parcelOutRepository.countBySenderLocCode(locCode);
	    }

	    // Method to get total count of parcels received (parcel in) for a specific locCode
	    public long totalCountParcelInByLocCode(String locCode) {
	        return parcelInRepository.countByRecipientLocCode(locCode);
	    }

	    public long totalCountParcelOutByDate(LocalDate date) {
	        return parcelOutRepository.countByCreatedDate(date);
	    }

	    public long totalCountParcelInByDate(LocalDate date) {
	        return parcelInRepository.countByReceivedDate(date);
	    }
	    
	    public Map<String, Object> getTotalsByLocCode(String locCode) {
	        Map<String, Object> totalsByLocCode = new HashMap<>();

	        // Fetch total count for parcels dispatched (parcel out) for a specific location code
	        long parcelOutCount = parcelOutRepository.countParcelOutBySenderLocCode(locCode);
	        totalsByLocCode.put("ParcelOut", parcelOutCount);

	        // Fetch total count for parcels received (parcel in) for a specific location code
	        long parcelInCount = parcelInRepository.countParcelInByRecipientLocCode(locCode);
	        totalsByLocCode.put("ParcelIn", parcelInCount);

	        // Calculate the total count of parcels (in + out) for the specific location code
	        long totalParcels = parcelInCount + parcelOutCount;
	        totalsByLocCode.put("totalParcels", totalParcels);

	        return totalsByLocCode;
	    }
	    
	    
	    public Map<String, Object> getTotalsByDateAndLocCode(LocalDate date, String locCode) {
	        Map<String, Object> totalsByDateAndLocCode = new HashMap<>();

	        // Fetch total count of parcels dispatched (parcel out) for a specific date and locCode
	        long parcelOutCount = parcelOutRepository.countParcelOutByCreatedDateAndSenderLocCode(date, locCode);
	        totalsByDateAndLocCode.put("ParcelOut", parcelOutCount);

	        // Fetch total count of parcels received (parcel in) for a specific date and locCode
	        long parcelInCount = parcelInRepository.countParcelInByReceivedDateAndRecipientLocCode(date, locCode);
	        totalsByDateAndLocCode.put("ParcelIn", parcelInCount);

	        // Calculate the total count of parcels (in + out) for the specific date and locCode
	        long totalParcels = parcelInCount + parcelOutCount;
	        totalsByDateAndLocCode.put("totalParcels", totalParcels);

	        return totalsByDateAndLocCode;
	    }

		
}
