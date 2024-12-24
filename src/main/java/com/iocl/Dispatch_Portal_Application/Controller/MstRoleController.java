package com.iocl.Dispatch_Portal_Application.Controller;

import java.util.List;
import java.util.Optional;

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

import com.iocl.Dispatch_Portal_Application.Entity.MstRole;
import com.iocl.Dispatch_Portal_Application.ServiceLayer.MstRoleService;

@RestController
@RequestMapping("/roles")
public class MstRoleController {

	@Autowired
    private MstRoleService mstRoleService;

    @GetMapping
    public List<MstRole> getAllRoles() {
        return mstRoleService.findAll();
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<MstRole> getRoleById(@PathVariable String roleId) {
        Optional<MstRole> role = mstRoleService.findById(roleId);
        return role.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public MstRole createRole(@RequestBody MstRole mstRole) {
        return mstRoleService.save(mstRole);
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<MstRole> updateRole(@PathVariable String roleId, @RequestBody MstRole mstRole) {
        if (!mstRoleService.findById(roleId).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        mstRole.setRoleId(roleId);
        return ResponseEntity.ok(mstRoleService.save(mstRole));
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<Void> deleteRole(@PathVariable String roleId) {
        if (!mstRoleService.findById(roleId).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        mstRoleService.deleteById(roleId);
        return ResponseEntity.noContent().build();
    }
}

