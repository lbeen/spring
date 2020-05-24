package com.lbeen.spring.sys.service.impl;

import com.lbeen.spring.common.util.CommonUtil;
import com.lbeen.spring.sys.bean.Menu;
import com.lbeen.spring.sys.mapper.MenuMapper;
import com.lbeen.spring.sys.service.MenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {
    @Resource
    private MenuMapper menuMapper;

    @Override
    public List<Menu> selectList(String parentId) {
        if (StringUtils.isBlank(parentId)) {
            return menuMapper.selectTopList();
        }
        return menuMapper.selectList(parentId);
    }

    @Override
    public List<Menu> selectAllUsed() {
        return menuMapper.selectAllUsed();
    }

    @Override
    public Menu selectById(String id) {
        return menuMapper.selectById(id);
    }

    @Override
    public void saveMenu(Menu menu) {
        if (StringUtils.isBlank(menu.getParentId())) {
            menu.setParentId(null);
        }
        if (StringUtils.isBlank(menu.getId())) {
            menu.setId(CommonUtil.uuid());
            menuMapper.insert(menu);
        } else {
            menuMapper.update(menu);
        }
    }
}
