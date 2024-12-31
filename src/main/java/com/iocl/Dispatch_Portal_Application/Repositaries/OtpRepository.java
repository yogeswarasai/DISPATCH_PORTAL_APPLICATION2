package com.iocl.Dispatch_Portal_Application.Repositaries;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iocl.Dispatch_Portal_Application.Entity.OtpEntity;

@Repository
public interface OtpRepository extends JpaRepository<OtpEntity, UUID>{

    OtpEntity findFirstByEmpCodeOrderByCreatedAtDesc(String empCode);


}
