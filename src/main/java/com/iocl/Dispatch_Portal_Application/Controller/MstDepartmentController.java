package com.iocl.Dispatch_Portal_Application.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iocl.Dispatch_Portal_Application.Entity.MstDepartment;
import com.iocl.Dispatch_Portal_Application.ServiceLayer.MstDepartmentService;
import com.iocl.Dispatch_Portal_Application.composite_pk.MstDepartmentPK;

@RestController
@RequestMapping("/departments")
public class MstDepartmentController {

	
	@Autowired
    private MstDepartmentService mstDepartmentService;

    @GetMapping
    public List<MstDepartment> getAllDepartments() {
        return mstDepartmentService.findAll();
    }

    @GetMapping("/departments/{locCode}")
    public ResponseEntity<List<MstDepartment>> getDepartmentsByLocationCode(@PathVariable String locCode) {
        List<MstDepartment> departments = mstDepartmentService.getDepartmentsByLocationCode(locCode);
        return ResponseEntity.ok(departments);
    }

    @PostMapping
    public MstDepartment createDepartment(@RequestBody MstDepartment mstDepartment) {
        return mstDepartmentService.save(mstDepartment);
    }

    @PutMapping("/{locCode}/{deptCode}")
    public ResponseEntity<MstDepartment> updateDepartment(@PathVariable String locCode, @PathVariable String deptCode, @RequestBody MstDepartment mstDepartment) {
        MstDepartmentPK id = new MstDepartmentPK(locCode, deptCode);
        if (!mstDepartmentService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        mstDepartment.setLocCode(locCode);
        mstDepartment.setDeptCode(deptCode);
        return ResponseEntity.ok(mstDepartmentService.save(mstDepartment));
    }

    @DeleteMapping("/{locCode}/{deptCode}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable String locCode, @PathVariable String deptCode) {
        MstDepartmentPK id = new MstDepartmentPK(locCode, deptCode);
        if (!mstDepartmentService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        mstDepartmentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

