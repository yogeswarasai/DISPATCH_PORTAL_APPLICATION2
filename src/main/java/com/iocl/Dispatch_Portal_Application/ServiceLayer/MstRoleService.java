package com.iocl.Dispatch_Portal_Application.ServiceLayer;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iocl.Dispatch_Portal_Application.Entity.MstRole;
import com.iocl.Dispatch_Portal_Application.Repositaries.MstRoleRepository;

@Service
public class MstRoleService {

	 @Autowired
	    private MstRoleRepository mstRoleRepository;

	    public List<MstRole> findAll() {
	        return mstRoleRepository.findAll();
	    }

	    public Optional<MstRole> findById(String roleId) {
	        return mstRoleRepository.findById(roleId);
	    }

	    public MstRole save(MstRole mstRole) {
	        return mstRoleRepository.save(mstRole);
	    }

	    public void deleteById(String roleId) {
	        mstRoleRepository.deleteById(roleId);
	    }
}
