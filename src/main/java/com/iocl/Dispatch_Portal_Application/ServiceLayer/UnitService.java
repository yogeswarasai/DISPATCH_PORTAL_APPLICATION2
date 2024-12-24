package com.iocl.Dispatch_Portal_Application.ServiceLayer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iocl.Dispatch_Portal_Application.Entity.Unit;
import com.iocl.Dispatch_Portal_Application.Repositaries.UnitRepository;

@Service
public class UnitService {
	 @Autowired
	    private UnitRepository unitRepository;

	    public List<Unit> getAllUnits() {
	        return unitRepository.findAll();
	    }

	    public Unit getUnitById(String id) {
	        return unitRepository.findById(id).orElse(null);
	    }

	    public Unit saveUnit(Unit unit) {
	        return unitRepository.save(unit);
	    }

	    public void deleteUnit(String id) {
	        unitRepository.deleteById(id);
	    }
}
