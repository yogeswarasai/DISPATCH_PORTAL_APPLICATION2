package com.iocl.Dispatch_Portal_Application.Repositaries;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iocl.Dispatch_Portal_Application.Entity.MstRoleAccess;
import com.iocl.Dispatch_Portal_Application.composite_pk.MstRoleAccessPK;

public interface MstRoleAccessRepositary extends JpaRepository<MstRoleAccess, MstRoleAccessPK> {

}