package com.lbeen.spring.sys.web;

import com.lbeen.spring.common.bean.Result;
import com.lbeen.spring.sys.bean.Menu;
import com.lbeen.spring.sys.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/menu/")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @RequestMapping("getMenus")
    public List<Menu> getMenus(String parentId) {
        return menuService.selectList(parentId);
    }

    @RequestMapping("getMenuTree")
    public List<Map<String, Object>> getMenuTree() {
        List<Map<String, Object>> top = new ArrayList<>();

        List<Menu> menus = menuService.selectAllUsed();
        if (!CollectionUtils.isEmpty(menus)) {
            Map<String, Map<String, Object>> map = new LinkedHashMap<>();
            for (Menu m : menus) {
                Map<String, Object> menu = new HashMap<>();
                menu.put("name", m.getName());
                menu.put("icon", m.getIcon());
                menu.put("subs", new ArrayList<>());
                menu.put("index", m.getUrl());
                menu.put("parentId", m.getParentId());
                map.put(m.getId(), menu);
            }
            for (Map<String, Object> menu : map.values()) {
                Object parentId = menu.get("parentId");
                if (parentId == null) {
                    top.add(menu);
                    continue;
                }
                Map<String, Object> parent = map.get(parentId.toString());
                if (parent == null) {
                    continue;
                }
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> subs = (List<Map<String, Object>>) parent.get("subs");
                subs.add(menu);
            }
        }

        return top;
    }

    @RequestMapping("getMenuById")
    public Menu getMenuById(String id) {
        return menuService.selectById(id);
    }

    @RequestMapping("saveMenu")
    public Result saveMenu(Menu menu) {
        menuService.saveMenu(menu);
        return Result.saveSuccess();
    }
}
