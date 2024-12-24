package com.iocl.Dispatch_Portal_Application.ServiceLayer;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iocl.Dispatch_Portal_Application.Entity.MstLocation;
import com.iocl.Dispatch_Portal_Application.Repositaries.MstLocationRepository;

@Service
public class MstLocationService {

	 @Autowired
	    private MstLocationRepository mstLocationRepository;

	    public List<MstLocation> findAll() {
	        return mstLocationRepository.findAll();
	    }

	    public Optional<MstLocation> findById(String locCode) {
	        return mstLocationRepository.findById(locCode);
	    }

	    public MstLocation save(MstLocation mstLocation) {
	        return mstLocationRepository.save(mstLocation);
	    }

	    public void deleteById(String locCode) {
	        mstLocationRepository.deleteById(locCode);
	    }

		public List<MstLocation> getAllLocations() {
			 return mstLocationRepository.findAll();
		}
		
		 public boolean doesLocCodeExist(String locCode) {
		        return mstLocationRepository.existsByLocCode(locCode);
		    }
		 
		 public List<String> findAllLocationCodes() {
		        return mstLocationRepository.findAllLocationCodes();
		    }


		 
		 public List<String> findAllLocationNamesWithCodes() {
		        return mstLocationRepository.findAll()
		                                    .stream()
		                                    .map(location -> location.getLocName() + " (" + location.getLocCode().trim() + ")")
		                                    .collect(Collectors.toList());
		    }
		 
		  public String getLocNameByCode(String locCode) {
		        String location = mstLocationRepository.findByLocCode(locCode);
		        return location;
		    }

	}

