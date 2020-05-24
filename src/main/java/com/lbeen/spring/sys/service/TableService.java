package com.lbeen.spring.sys.service;

import com.lbeen.spring.common.bean.Page;
import com.lbeen.spring.common.bean.Result;
import com.lbeen.spring.sys.bean.Table;
import com.lbeen.spring.sys.bean.TableColumn;

import java.util.List;

public interface TableService {
    Page selectPage(Integer skip, Integer limit, String dbId, String tableName, String tableDesc);

    Table selectById(String id);

    Result saveTable(Table table);

    Result delete(String id);

    List<TableColumn> selectColumnsByTableId(String tableId);

    Result insertColumns(String tableId, List<TableColumn> columns);
}
