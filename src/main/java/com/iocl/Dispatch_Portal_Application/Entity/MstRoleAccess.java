package com.iocl.Dispatch_Portal_Application.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.iocl.Dispatch_Portal_Application.composite_pk.MstRoleAccessPK;

@Entity
@Table(name="mst_role_access")
@IdClass(MstRoleAccessPK.class)
public class MstRoleAccess {

@Id
@Column(name="role_id" , length=10, nullable = false)
private String roleId;

@Id
@Column(name="menu_id" , length=20, nullable = false)
private String menuId;


}

