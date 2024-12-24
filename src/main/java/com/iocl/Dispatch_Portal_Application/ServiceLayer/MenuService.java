package com.iocl.Dispatch_Portal_Application.ServiceLayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iocl.Dispatch_Portal_Application.Entity.MstMenu;
import com.iocl.Dispatch_Portal_Application.Repositaries.MstMenuRepositary;

@Service
public class MenuService {



@Autowired
   private MstMenuRepositary mstMenuRepository;



public List<MstMenu> getMenusByRole(String roleId) {
    // Fetch all menus by role and order them by parentMenuId and menuOrder
    List<MstMenu> allMenus = mstMenuRepository.findAllMenusByRoleIdOrderByParentMenuIdAndMenuOrder(roleId);

    // Use a map to store parent menus and their corresponding child menus
    Map<String, MstMenu> parentMenuMap = new LinkedHashMap<>();  // To maintain insertion order

    // Loop through the fetched menus and organize them
    for (MstMenu menu : allMenus) {
        if (menu.getParentMenuId().equals("#")) {
            // It's a parent menu, so we add it to the parent map
            parentMenuMap.put(menu.getMenuId(), menu);
        } else {
            // It's a child menu, so we find its parent and add the child to the parent's list
            MstMenu parentMenu = parentMenuMap.get(menu.getParentMenuId());
            if (parentMenu != null) {
                parentMenu.addChild(menu);
            }
        }
    }

    // Now, sort child menus for each parent based on `menuOrder`
    for (MstMenu parentMenu : parentMenuMap.values()) {
        parentMenu.getChildMenus().sort(Comparator.comparingInt(MstMenu::getMenuOrder)); // Sort child menus by menuOrder
    }

    // Convert map values to a list, sort parent menus by menuOrder and return
    List<MstMenu> sortedParentMenus = new ArrayList<>(parentMenuMap.values());
    sortedParentMenus.sort(Comparator.comparingInt(MstMenu::getMenuOrder));  // Sort parent menus by menuOrder

    return sortedParentMenus;
}



}
