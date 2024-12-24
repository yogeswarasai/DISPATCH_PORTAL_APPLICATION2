package com.iocl.Dispatch_Portal_Application.Repositaries;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iocl.Dispatch_Portal_Application.Entity.MstMenu;

public interface MstMenuRepositary extends JpaRepository<MstMenu, String>{




	@Query("SELECT m FROM MstMenu m WHERE m.menuId IN (SELECT r.menuId FROM MstRoleAccess r WHERE r.roleId = :roleId) ORDER BY m.parentMenuId, m.menuOrder ASC")
	List<MstMenu> findAllMenusByRoleIdOrderByParentMenuIdAndMenuOrder(String roleId);

	

}