package com.lbeen.spring.sys.service.impl;

import com.lbeen.spring.common.bean.Page;
import com.lbeen.spring.common.bean.Result;
import com.lbeen.spring.common.util.CommonUtil;
import com.lbeen.spring.common.util.SqlUtil;
import com.lbeen.spring.sys.bean.Table;
import com.lbeen.spring.sys.bean.TableColumn;
import com.lbeen.spring.sys.mapper.TableMapper;
import com.lbeen.spring.sys.service.TableService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TableServiceImpl implements TableService {
    @Resource
    private TableMapper tableMapper;

    @Override
    public Page selectPage(Integer skip, Integer limit, String dbId, String tableName, String tableDesc) {
        Map<String, Object> param = new HashMap<>();
        CommonUtil.putIfNotBlank(param, "dbId", dbId);
        CommonUtil.putIfNotBlank(param, "tableName", tableName);
        CommonUtil.putIfNotBlank(param, "tableDesc", tableDesc);
        return SqlUtil.queryPage(tableMapper::count, tableMapper::selectPage, param, skip, limit);
    }

    @Override
    public Table selectById(String id) {
        Table param = new Table();
        param.setId(id);
        return tableMapper.selectOne(param);
    }

    @Override
    public Result saveTable(Table table) {
        Table exist = tableMapper.selectOne(table);
        if (StringUtils.isBlank(table.getId())) {
            if (exist != null) {
                return Result.error(table.getTableName() + "已存在");
            }
            table.setId(CommonUtil.uuid());
            tableMapper.insert(table);
        } else {
            if (exist != null && !table.getId().equals(exist.getId())) {
                return Result.error(table.getTableName() + "已存在");
            }
            tableMapper.update(table);
        }

        return Result.saveSuccess();
    }

    @Override
    public Result delete(String id) {
        tableMapper.delete(id);
        tableMapper.deleteColumnsByTableId(id);
        return Result.successMsg("删除成功");
    }

    @Override
    public List<TableColumn> selectColumnsByTableId(String tableId) {
        return tableMapper.selectColumnsByTableId(tableId);
    }

    @Override
    public Result insertColumns(String tableId, List<TableColumn> columns) {
        for (TableColumn column : columns) {
            column.setId(CommonUtil.uuid());
            column.setTableId(tableId);
        }
        tableMapper.deleteColumnsByTableId(tableId);
        tableMapper.insertColumns(columns);
        return Result.saveSuccess();
    }
}
