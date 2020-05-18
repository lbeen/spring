package com.lbeen.spring.database.mapper;

import com.lbeen.spring.database.bean.Table;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TableMapper {
    int count(Map<String, Object> param);

    List<Map<String, Object>> selectPage(Map<String, Object> param);

    Table selectOne(String id);

    void insert(Table table);

    void update(Table table);

    void delete(String id);
}
