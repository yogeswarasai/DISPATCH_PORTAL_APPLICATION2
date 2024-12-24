package com.iocl.Dispatch_Portal_Application.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iocl.Dispatch_Portal_Application.Entity.Unit;
import com.iocl.Dispatch_Portal_Application.ServiceLayer.UnitService;
@RestController
@RequestMapping("/units")
public class UnitController {

	  @Autowired
	    private UnitService unitService;

	    @GetMapping
	    public List<Unit> getAllUnits() {
	        return unitService.getAllUnits();
	    }

	    @GetMapping("/{unitId}")
	    public Unit getUnitById(@PathVariable String unitId) {
	        return unitService.getUnitById(unitId);
	    }

	    @PostMapping
	    public Unit createUnit(@RequestBody Unit unit) {
	        return unitService.saveUnit(unit);
	    }

	    @DeleteMapping("/{unitId}")
	    public void deleteUnit(@PathVariable String unitId) {
	        unitService.deleteUnit(unitId);
	    }
}
