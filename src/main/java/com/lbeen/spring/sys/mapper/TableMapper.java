package com.lbeen.spring.sys.mapper;

import com.lbeen.spring.sys.bean.Table;
import com.lbeen.spring.sys.bean.TableColumn;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TableMapper {
    int count(Map<String, Object> param);

    List<Map<String, Object>> selectPage(Map<String, Object> param);

    Table selectOne(Table table);

    void insert(Table table);

    void update(Table table);

    void delete(String id);

    List<TableColumn> selectColumnsByTableId(String tableId);

    void deleteColumnsByTableId(String tableId);

    void insertColumns(List<TableColumn> columns);
}
