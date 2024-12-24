package com.iocl.Dispatch_Portal_Application.ServiceLayer;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iocl.Dispatch_Portal_Application.Entity.MstDepartment;
import com.iocl.Dispatch_Portal_Application.Repositaries.MstDepartmentRepository;
import com.iocl.Dispatch_Portal_Application.composite_pk.MstDepartmentPK;

@Service
public class MstDepartmentService {

	 @Autowired
	    private MstDepartmentRepository mstDepartmentRepository;

	    public List<MstDepartment> findAll() {
	        return mstDepartmentRepository.findAll();
	    }

	    public Optional<MstDepartment> findById(MstDepartmentPK id) {
	        return mstDepartmentRepository.findById(id);
	    }

	    public MstDepartment save(MstDepartment mstDepartment) {
	        return mstDepartmentRepository.save(mstDepartment);
	    }

	    public void deleteById(MstDepartmentPK id) {
	        mstDepartmentRepository.deleteById(id);
	    }
	   

		public List<MstDepartment> getDepartmentsByLocationCode(String locCode) {
	        return mstDepartmentRepository.findByLocCode(locCode);

		}

		 public List<String> getDepartmentsByLocationName(String locName) {
		        return mstDepartmentRepository.findDepartmentsByLocationName(locName);
		    }

		
	}

