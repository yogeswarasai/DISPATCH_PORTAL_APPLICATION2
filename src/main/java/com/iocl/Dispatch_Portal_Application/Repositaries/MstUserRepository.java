package com.iocl.Dispatch_Portal_Application.Repositaries;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iocl.Dispatch_Portal_Application.Entity.MstUser;
import com.iocl.Dispatch_Portal_Application.composite_pk.MstUserPK;


@Repository
public interface MstUserRepository extends JpaRepository<MstUser, MstUserPK>
{
	   Optional<MstUser> findByUserName(String username);
	   Optional<MstUser> findByMobileNumber(Long mobileNumber);
	   Optional<MstUser> findByUserId(String userId);
	   
	    boolean existsByMobileNumber(Long mobileNumber);
	    
	    Optional<MstUser> findByRoleIdAndLocCodeIgnoreCase(@Param("roleId") String roleId, @Param("locCode") String locCode);
	    
	    @Query("SELECT u FROM MstUser u WHERE LOWER(u.roleId) = LOWER(:roleId) AND u.locCode = :locCode")
	    List<MstUser> findByRoleIdAndLocCode(@Param("roleId") String roleId, @Param("locCode") String locCode);



}
