package com.iocl.Dispatch_Portal_Application.Repositaries;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iocl.Dispatch_Portal_Application.Entity.MstRole;


@Repository
public interface MstRoleRepository extends JpaRepository<MstRole, String>{

}
