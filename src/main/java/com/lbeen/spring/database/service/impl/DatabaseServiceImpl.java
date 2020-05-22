package com.lbeen.spring.database.service.impl;

import com.lbeen.spring.common.bean.Page;
import com.lbeen.spring.common.util.CommonUtil;
import com.lbeen.spring.common.util.R;
import com.lbeen.spring.common.util.SqlUtil;
import com.lbeen.spring.database.bean.Database;
import com.lbeen.spring.database.bean.Table;
import com.lbeen.spring.database.mapper.DatabaseMapper;
import com.lbeen.spring.database.mapper.TableMapper;
import com.lbeen.spring.database.service.DatabaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DatabaseServiceImpl implements DatabaseService {
    @Resource
    private DatabaseMapper databaseMapper;

    @Resource
    private TableMapper tableMapper;

    @Override
    public Page getDbPage(Integer skip, Integer limit, String dbDesc, String dbType, String used) {
        Map<String, Object> param = new HashMap<>();
        CommonUtil.putIfNotBlank(param, "dbDesc", dbDesc);
        CommonUtil.putIfNotBlank(param, "dbType", dbType);
        CommonUtil.putIfNotBlank(param, "used", used);
        return SqlUtil.queryPage(databaseMapper::count, databaseMapper::selectPage, param, skip, limit);
    }

    @Override
    public Database getOneDb(String id) {
        return databaseMapper.selectOne(id);
    }

    @Override
    public void saveDatabase(Database database) {
        database.setDbDesc(database.getIp() + "_" + database.getPort() + "_" + database.getDbName());
        if (StringUtils.isBlank(database.getId())) {
            database.setId(R.uuid());
            databaseMapper.insert(database);
        } else {
            databaseMapper.update(database);
        }
    }

    @Override
    public void deleteDatabase(String id) {
        databaseMapper.delete(id);
    }

    @Override
    public Page getTablePage(Integer skip, Integer limit, String dbId, String tableName, String tableDesc) {
        Map<String, Object> param = new HashMap<>();
        CommonUtil.putIfNotBlank(param, "dbId", dbId);
        CommonUtil.putIfNotBlank(param, "tableName", tableName);
        CommonUtil.putIfNotBlank(param, "tableDesc", tableDesc);
        return SqlUtil.queryPage(tableMapper::count, tableMapper::selectPage, param, skip, limit);
    }

    @Override
    public Table getOneTable(String id) {
        return tableMapper.selectOne(id);
    }

    @Override
    public void saveTable(Table table) {
        if (StringUtils.isBlank(table.getId())) {
            table.setId(R.uuid());
            tableMapper.insert(table);
        } else {
            tableMapper.update(table);
        }
    }

    @Override
    public void deleteTable(String id) {
        tableMapper.delete(id);
    }


    @Override
    public List<Database> getUsedMongoDbs() {
        return databaseMapper.getUsedMongoDbs();
    }

    @Override
    public List<Table> getUsedMongoTables() {
        return databaseMapper.getUsedMongoTables();
    }
}
