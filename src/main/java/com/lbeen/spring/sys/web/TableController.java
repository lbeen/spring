package com.lbeen.spring.sys.web;

import com.alibaba.fastjson.JSONObject;
import com.lbeen.spring.common.bean.Page;
import com.lbeen.spring.common.bean.Result;
import com.lbeen.spring.common.util.CommonUtil;
import com.lbeen.spring.common.util.MongoUtil;
import com.lbeen.spring.sys.bean.Table;
import com.lbeen.spring.sys.bean.TableColumn;
import com.lbeen.spring.sys.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/table/")
public class TableController {
    @Autowired
    private TableService tableService;

    @RequestMapping("getPage")
    public Page getPage(Integer skip, Integer limit, String dbId, String tableName, String tableDesc) {
        return tableService.selectPage(skip, limit, dbId, tableName, tableDesc);
    }

    @RequestMapping("getTableById")
    public Table getTableById(String id) {
        return tableService.selectById(id);
    }

    @RequestMapping("saveTable")
    public Result saveTable(Table table) {
        return tableService.saveTable(table);
    }

    @RequestMapping("deleteTable")
    public Result deleteTable(String id) {
        return tableService.delete(id);
    }

    @RequestMapping("getColumnsByTableId")
    public List<TableColumn> getColumnsByTableId(String tableId) {
        return tableService.selectColumnsByTableId(tableId);
    }

    @RequestMapping("insertColumns")
    public Result insertColumns(@RequestBody JSONObject json) {
        String tableId = json.getString("tableId");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> tableColumns = (List<Map<String, Object>>) json.get("tableColumns");

        List<TableColumn> columns = new ArrayList<>();
        for (Map<String, Object> map : tableColumns) {
            TableColumn column = new TableColumn();
            column.setTableId(tableId);
            column.setColumnName(CommonUtil.tosString(map.get("columnName")));
            column.setColumnDesc(CommonUtil.tosString(map.get("columnDesc")));
            column.setColumnType(CommonUtil.tosString(map.get("columnType")));
            column.setSort(CommonUtil.toInt(map.get("sort")));
            columns.add(column);
        }
        return tableService.insertColumns(tableId, columns);
    }

    @RequestMapping("refreshTableCache")
    public Result refreshTableCache() {
        MongoUtil.loadCache();
        return Result.successMsg("刷新成功");
    }
}
