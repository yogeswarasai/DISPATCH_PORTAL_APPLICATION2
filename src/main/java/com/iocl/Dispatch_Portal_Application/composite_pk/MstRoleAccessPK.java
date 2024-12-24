package com.iocl.Dispatch_Portal_Application.composite_pk;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;

import lombok.Data;
@Data
public class MstRoleAccessPK implements Serializable{
	
	private String roleId;
	public MstRoleAccessPK()
	{
		
	}
	
	public MstRoleAccessPK(String roleId, String menuId) {
		super();
		this.roleId = roleId;
		this.menuId = menuId;
	}
	private String menuId;

}
