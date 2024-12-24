package com.iocl.Dispatch_Portal_Application.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iocl.Dispatch_Portal_Application.Entity.MstMenu;
import com.iocl.Dispatch_Portal_Application.ServiceLayer.MenuService;

@RestController
@RequestMapping("/api/menus")
public class MenuController {


@Autowired
   private MenuService menuService;

   @GetMapping("/role/{roleId}")
   public ResponseEntity<List<MstMenu>> getMenusByRole(@PathVariable String roleId) {
       List<MstMenu> menus = menuService.getMenusByRole(roleId);
       return ResponseEntity.ok(menus);
   }
}
