package com.lbeen.spring.sys.mapper;

import com.lbeen.spring.sys.bean.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper {
    List<Menu> selectTopList();
    List<Menu> selectList(String parentId);

    List<Menu> selectAllUsed();

    Menu selectById(String id);

    void insert(Menu menu);

    void update(Menu menu);
}
