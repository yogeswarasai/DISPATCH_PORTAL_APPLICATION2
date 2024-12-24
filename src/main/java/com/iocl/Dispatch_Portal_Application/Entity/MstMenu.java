package com.iocl.Dispatch_Portal_Application.Entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

@Data
@Entity
@Table(name="mst_menu")
public class MstMenu {

@Id
@Column(name="menu_id", length=20,nullable = false)
   private String menuId;
@Column(name="menu_name", length=50,nullable = false)
   private String menuName;
@Column(name="parent_menu_id", length=20,nullable = false)
   private String parentMenuId = "#";
@Column(name="route", length=100,nullable = false)
   private String route;

@Column(name="material_icon", length=50,nullable = false)
private String icons;

@Column(name="menu_order")
private Integer menuOrder;

// New field for child menus
@Transient
private List<MstMenu> childMenus = new ArrayList<>();

// Add child menu
public void addChild(MstMenu childMenu) {
    this.childMenus.add(childMenu);
}

// Get child menus
public List<MstMenu> getChildMenus() {
    return childMenus;
}
}
