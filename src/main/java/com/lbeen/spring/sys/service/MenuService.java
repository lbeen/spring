package com.lbeen.spring.sys.service;

import com.lbeen.spring.sys.bean.Menu;
import com.lbeen.spring.sys.mapper.MenuMapper;

import javax.annotation.Resource;
import java.util.List;

public interface MenuService {
    List<Menu> selectList(String parentId);

    List<Menu> selectAllUsed();

    Menu selectById(String id);

    void saveMenu(Menu menu);
}
