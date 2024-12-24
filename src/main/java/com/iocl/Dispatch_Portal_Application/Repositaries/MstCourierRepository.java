package com.iocl.Dispatch_Portal_Application.Repositaries;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iocl.Dispatch_Portal_Application.Entity.MstCourier;
import com.iocl.Dispatch_Portal_Application.composite_pk.MstCourierPK;

@Repository
public interface MstCourierRepository extends JpaRepository<MstCourier,MstCourierPK> {

	List<MstCourier> findByLocCode(String locCode);

	List<MstCourier> findByLocCode(HttpServletRequest request);

}
